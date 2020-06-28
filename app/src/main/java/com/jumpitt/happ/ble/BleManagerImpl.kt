package com.jumpitt.happ.ble

import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.jumpitt.happ.network.RestClient
import com.jumpitt.happ.network.request.TracingRequest
import com.jumpitt.happ.network.response.RegisterResponse
import com.jumpitt.happ.network.response.TracingResponse
import com.jumpitt.happ.ui.main.MainActivity
import com.orhanobut.hawk.Hawk
import org.tcncoalition.tcnclient.TcnKeys
import org.tcncoalition.tcnclient.bluetooth.BluetoothStateListener
import org.tcncoalition.tcnclient.bluetooth.TcnBluetoothService
import org.tcncoalition.tcnclient.bluetooth.TcnBluetoothServiceCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


interface BleManager {
    fun startService()
    fun stopService()
}

class BleManagerImpl(
    private val app: Context,
    private val tcnGenerator: TcnGenerator
): BleManager, BluetoothStateListener {

    private val intent get() = Intent(app, TcnBluetoothService::class.java)
    private var service: TcnBluetoothService? = null

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            this@BleManagerImpl.service = (service as TcnBluetoothService.LocalBinder).service.apply {
                val notification = foregroundNotification()
                startForegroundNotificationIfNeeded(NOTIFICATION_ID, notification)
                setBluetoothStateListener(this@BleManagerImpl)
                startTcnExchange(BluetoothServiceCallback())
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {}
    }
    inner class BluetoothServiceCallback : TcnBluetoothServiceCallback

    {
        override fun generateTcn(): ByteArray =
            tcnGenerator.generateTcn().bytes

        override fun onTcnFound(tcn: ByteArray, myTcn: ByteArray?, estimatedDistance: Double?) {
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date())
            Log.e("TcnClient", "myTcn: ${myTcn?.toHex()}  tcn found: ${tcn.toHex()} date: $currentDate distance: $estimatedDistance" )

            val userData = Hawk.get<RegisterResponse>("userProfileData")
            RestClient.instance.postTCN("http://tracing.keepsafe.jumpittlabs.cl/traces/","Bearer "+userData.accessToken, tcnRequest = TracingRequest(tcn = myTcn!!.toHex(),tcnFounded = tcn.toHex(),distance = estimatedDistance)).
            enqueue(object: Callback<TracingResponse> {
                override fun onFailure(call: Call<TracingResponse>, t: Throwable) {
                }

                override fun onResponse(call: Call<TracingResponse>, response: Response<TracingResponse>) {


                }
            })
        }
    }

    override fun startService() {
        app.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        app.startService(intent)
    }

    override fun stopService() {
        app.stopService(intent)
    }

    override fun bluetoothStateChanged(bluetoothOn: Boolean) {
    }

    private fun foregroundNotification(): Notification {
        createNotificationChannelIfNeeded()

        val notificationIntent = Intent(app, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            app, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(app, CHANNEL_ID)
            .setContentTitle("Happ se está ejecutando")
            .setContentIntent(pendingIntent)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
    }

    /**
     * This notification channel is only required for android versions above
     * android O. This creates the necessary notification channel for the foregroundService
     * to function.
     */
    private fun createNotificationChannelIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
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



