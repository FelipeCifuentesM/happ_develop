package com.jumpitt.happ.ble

import android.annotation.SuppressLint
import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.app.ServiceCompat.stopForeground
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.jumpitt.happ.App
import com.jumpitt.happ.R
import com.jumpitt.happ.network.RestClient
import com.jumpitt.happ.network.request.TracingRequest
import com.jumpitt.happ.network.response.TracingResponse
import com.jumpitt.happ.realm.RegisterData
import com.jumpitt.happ.realm.RiskTime
import com.jumpitt.happ.realm.TraceProximityNotification
import com.jumpitt.happ.ui.main.MainActivity
import com.jumpitt.happ.ui.registerPermissions.RegisterPermissions
import com.jumpitt.happ.utils.Constants
import com.jumpitt.happ.utils.dateDifferenceHMS
import com.jumpitt.happ.utils.dateDifferenceSeconds
import com.jumpitt.happ.utils.generateNotification
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import org.tcncoalition.tcnclient.TcnKeys
import org.tcncoalition.tcnclient.bluetooth.BluetoothStateListener
import org.tcncoalition.tcnclient.bluetooth.TcnBluetoothService
import org.tcncoalition.tcnclient.bluetooth.TcnBluetoothServiceCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


interface BleManager {
    fun startService()
    fun stopService()
}

class BleManagerImpl(
    private val app: Context,
    private val tcnGenerator: TcnGenerator,
    private val delegateDemo: TcnBluetoothServiceCallbackDemo? = null
): BleManager, BluetoothStateListener {

    private val intent get() = Intent(app, TcnBluetoothService::class.java)
    private var service: TcnBluetoothService? = null
    private lateinit var runnable: Runnable

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            this@BleManagerImpl.service = (service as TcnBluetoothService.LocalBinder).service.apply {
                val notification = foregroundNotification("Happ se está ejecutando", 1)
                startForegroundNotificationIfNeeded(NOTIFICATION_ID_SERVICE, notification)
                setBluetoothStateListener(this@BleManagerImpl)
                startTcnExchange(BluetoothServiceCallback())

//                runnable = Runnable {
//                    // Insert custom code here
//                    val isRunning = isMyServiceRunning(BleManagerImpl::class.java)
////                    if(isRunning){
//                        updateNotification()
//                        // Repeat every 25 minutes
//                        val timeRepeatMilliseconds:Long = 30000 * 1 // 25 minutes
//                        App.handlerNoti?.let { mHandlerNoti ->
//                            mHandlerNoti.postDelayed(runnable, timeRepeatMilliseconds)
//                        }
////                    }else{
////                        App.handlerNoti?.removeCallbacks(runnable)
////                    }
//                }


            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            val isRunning = isMyServiceRunning(BleManagerImpl::class.java)
            if(isRunning){
                this@BleManagerImpl.service?.let { service -> service.stopTcnExchange() }
                this@BleManagerImpl.service?.let { stopForeground(it, ServiceCompat.STOP_FOREGROUND_REMOVE) }
                app.stopService(intent)
                App.handlerNoti?.removeCallbacks(runnable)
            }
        }
    }
    inner class BluetoothServiceCallback : TcnBluetoothServiceCallback

    {
        override fun generateTcn(): ByteArray =
            tcnGenerator.generateTcn().bytes

        override fun onTcnFound(tcn: ByteArray, myTcn: ByteArray?, estimatedDistance: Double?) {
            var approximateDistance: Double? = null
            estimatedDistance?.let{ approximateDistance = (estimatedDistance * 100).roundToInt() / 100.0 }

            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date())
            Log.e("TcnClient", "myTcn: ${myTcn?.toHex()}  tcn found: ${tcn.toHex()} date: $currentDate distance: $approximateDistance" )
//            delegateDemo?.onTcnFound(tcn, myTcn, approximateDistance)

            val realm = Realm.getDefaultInstance()
            val userData = realm.where(RegisterData::class.java).findFirst()

            userData?.accessToken?.let {
                approximateDistance?.let { distance ->
                    if(distance <= 1.5) { saveRegisterNotification(currentDate) }
                }
//                saveRiskTime(myTcn?.toHex(), tcn.toHex(), currentDate, approximateDistance)
                RestClient.instance.postTCN("http://tracing.keepsafe.jumpittlabs.cl/traces/","Bearer "+userData.accessToken, tcnRequest = TracingRequest(tcn = myTcn!!.toHex(),tcnFounded = tcn.toHex(),distance = approximateDistance)).
                enqueue(object: Callback<TracingResponse> {
                    override fun onFailure(call: Call<TracingResponse>, t: Throwable) {
                    }

                    override fun onResponse(call: Call<TracingResponse>, response: Response<TracingResponse>) {


                    }
                })
            }?: run {
                service?.let { service -> service.stopTcnExchange() }
                service?.let { stopForeground(it, ServiceCompat.STOP_FOREGROUND_REMOVE) }
            }

        }
    }

    override fun startService() {
        app.bindService(intent, serviceConnection, Context.BIND_NOT_FOREGROUND)
        app.startService(intent)
    }

    override fun stopService() {
        app.stopService(intent)
    }

    override fun bluetoothStateChanged(bluetoothOn: Boolean) {
        if(bluetoothOn){
            Log.e("Borrar", "BLUETOOTH ON")
            val intent1 = Intent("action.finish")
            LocalBroadcastManager.getInstance(app).sendBroadcast(intent1)
        }else{
            Log.e("Borrar", "BLUETOOTH OFF")
            val bluetoothDisabledIntent = Intent(app, RegisterPermissions::class.java)
            bluetoothDisabledIntent.putExtra("validateReturnWhitOutPermission", true)
            bluetoothDisabledIntent.putExtra("fromService", true)
            bluetoothDisabledIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            app.applicationContext.startActivity(bluetoothDisabledIntent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun foregroundNotification(title: String, requestCode: Int): Notification {
        createNotificationChannelIfNeeded()

        val notificationIntent = Intent(app, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            app, requestCode, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(app, CHANNEL_ID)
            .setContentTitle(title)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationManager.IMPORTANCE_MAX)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
    }

    /**
     * This notification channel is only required for android versions above
     * android O. This creates the necessary notification channel for the foregroundService
     * to function.
     */
    @SuppressLint("WrongConstant")
    private fun createNotificationChannelIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_MAX
            )
            val manager: NotificationManager? = ContextCompat.getSystemService(
                app, NotificationManager::class.java
            )
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "CoEpiBluetoothContactChannel"
        const val NOTIFICATION_ID = 1
        const val NOTIFICATION_ID_SERVICE = 211
    }

    fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = app.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    private fun saveRiskTime(myTcn: String?, tcnFound: String?, currentDate: String, distance: Double?){
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val realm = Realm.getDefaultInstance()
        val riskTime: RealmResults<RiskTime> = realm.where(RiskTime::class.java).equalTo("myTcn",myTcn).equalTo("tcnFound",tcnFound).sort("dateLastContact", Sort.DESCENDING).findAll()
        realm.beginTransaction()

        if(riskTime.size>0) {
            var lastSavedDate:Date? = null
            var firstSavedDate:Date? = null
            riskTime[0]?.dateLastContact?.let { lastSavedDate = sdf.parse(it) }
            val currentFormatDate = sdf.parse(currentDate)
            if(lastSavedDate!=null && currentFormatDate!=null){
                val secondsDifference = dateDifferenceSeconds(lastSavedDate!!, currentFormatDate)
                if(secondsDifference <= Constants.MAXIMUM_TIME_APART_SECONDS){
                    //Realm update data
                    riskTime[0]?.dateFirstContact?.let { firstSavedDate = sdf.parse(it) }
                    firstSavedDate?.let { firstSaveDate ->
                        val totalTime = dateDifferenceHMS(firstSaveDate, currentFormatDate)
                        riskTime[0]?.totalTime = totalTime
                    }
                    riskTime[0]?.dateLastContact = currentDate
                }else{
                    //Realm New Row
                    var firstSavedDate:Date? = null
                    riskTime[0]?.dateFirstContact?.let { firstSavedDate = sdf.parse(it) }
                    firstSavedDate?.let {firstSaveDate ->
                        val riskTimeNew = RiskTime(myTcn, tcnFound, currentDate, currentDate, "00:00:00")
                        realm.insertOrUpdate(riskTimeNew)
                    }
                }
            }
        }else{
            //Realm first row
            val riskTimeNew = RiskTime(myTcn, tcnFound, currentDate, currentDate, "00:00:00")
            realm.insertOrUpdate(riskTimeNew)
        }

        realm.commitTransaction()
        realm.close()

    }

    private fun saveRegisterNotification(currentDate: String){
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val realm = Realm.getDefaultInstance()
        val traceProximityNotification: TraceProximityNotification? = realm.where(TraceProximityNotification::class.java).sort("firstRegisterTrace", Sort.DESCENDING).findFirst()
        realm.beginTransaction()
        if(traceProximityNotification != null){
            var firstSavedDate:Date? = null
            var lastSavedDate:Date? = null
            traceProximityNotification.lastRegisterTrace?.let { lastSavedDate = sdf.parse(it) }
            val currentFormatDate = sdf.parse(currentDate)
            if(lastSavedDate!=null && currentFormatDate!=null){
                val secondsDifference = dateDifferenceSeconds(lastSavedDate!!, currentFormatDate)
                //compare separate time
                if(secondsDifference <= Constants.MAXIMUM_TIME_APART_SECONDS){
                    traceProximityNotification.firstRegisterTrace?.let { firstSavedDate = sdf.parse(it) }
                    firstSavedDate?.let { _firstSaveDate ->
                        val totalTimeSeconds = dateDifferenceSeconds(_firstSaveDate, currentFormatDate)
                        //compare continuous time with another person
                        if(totalTimeSeconds >= Constants.MAXIMUM_TIME_SECONDS_PROXIMITY){
                            //reset table notification
                            traceProximityNotification.firstRegisterTrace = currentDate
                            traceProximityNotification.lastRegisterTrace = currentDate
                            realm.insertOrUpdate(traceProximityNotification)
                            //generate notification
                            generateNotification(app.resources.getString(R.string.notiSocialDistancingTitle), app.resources.getString(R.string.notiSocialDistancingMessage))
                        }else{
                            traceProximityNotification.lastRegisterTrace = currentDate
                            realm.insertOrUpdate(traceProximityNotification)
                        }
                    }
                }else{
                    //reset table notification
                    traceProximityNotification.firstRegisterTrace = currentDate
                    traceProximityNotification.lastRegisterTrace = currentDate
                    realm.insertOrUpdate(traceProximityNotification)
                }
            }
        }else{
            val traceProximityResetTime = TraceProximityNotification(1,currentDate, currentDate)
            realm.delete(TraceProximityNotification::class.java)
            realm.insertOrUpdate(traceProximityResetTime)
        }

        realm.commitTransaction()
        realm.close()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateNotification() {
        val notification = foregroundNotification("Happ se está ejecutando ahora",0)
        Log.e("Borrar", "ACTUALIZAR NOTIFICACION")
        val manager: NotificationManager? = ContextCompat.getSystemService(app, NotificationManager::class.java)
        manager?.notify(NOTIFICATION_ID, notification)
    }
}

data class Tcn(val bytes: ByteArray) {
    fun toHex(): String = bytes.toHex()

    override fun toString(): String = toHex()

    override fun hashCode(): Int =
        toHex().hashCode()

    override fun equals(other: Any?): Boolean =
        other is Tcn && toHex() == other.toHex()
}

private val HEX_ARRAY = "0123456789ABCDEF".toCharArray()
fun ByteArray.toHex(): String {
    val hexChars = CharArray(size * 2)
    for (j in indices) {
        val v: Int = this[j].toInt() and 0xFF
        hexChars[j * 2] = HEX_ARRAY[v ushr 4]
        hexChars[j * 2 + 1] = HEX_ARRAY[v and 0x0F]
    }
    return String(hexChars)
}

interface TcnGenerator {
    fun generateTcn(): Tcn
}

class TcnGeneratorImpl(context: Context) : TcnGenerator {
    private val tcnKeys: TcnKeys = TcnKeys(context)

    override fun generateTcn(): Tcn =
        Tcn(tcnKeys.generateTcn())
}

interface TcnBluetoothServiceCallbackDemo{
    fun onTcnFound(tcn: ByteArray, myTcn: ByteArray?, estimatedDistance: Double?)
}

