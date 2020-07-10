package com.jumpitt.happ.ui.splash

import android.app.Activity
import android.app.ActivityManager
import android.bluetooth.BluetoothAdapter
import android.content.Context
import com.jumpitt.happ.ble.BleManagerImpl
import com.jumpitt.happ.ble.TcnGeneratorImpl
import com.jumpitt.happ.network.response.RegisterResponse
import com.jumpitt.happ.realm.RegisterData
import com.jumpitt.happ.utils.isPermissionBackgroundLocation
import com.jumpitt.happ.utils.isPermissionLocation

class SplashActivityPresenter constructor(private val activity: Activity): SplashActivityContract.Presenter, SplashActivityContract.InteractorOutputs {
    private var mInteractor: SplashActivityContract.Interactor = SplashActivityInteractor(this)
    private var mView: SplashActivityContract.View = activity as SplashActivityContract.View
    private var mRouter: SplashActivityContract.Router = SplashActivityRouter(activity)

    override fun getUserProfileData(requestPermissions: Boolean) {
        mInteractor.getUserProfileData(requestPermissions)
    }

    override fun getUserProfileDataOutput(userData: RegisterData?, requestPermissions: Boolean) {
        if(userData!=null){
            //review permissions
            if(requestPermissions){
                val permissionGranted = activity.isPermissionBackgroundLocation()
                if(permissionGranted){
                    validateBluetoothState()
                }
            }else{
                validateBluetoothState()
            }
        }else{
            mRouter.navigateOnBoard()
        }
    }

    override fun validateBluetoothState() {
        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter.isEnabled){
            val isRunning = isMyServiceRunning(BleManagerImpl::class.java)
            if(!isRunning) {
                val tcnGenerator = TcnGeneratorImpl(context = activity)
                val bleManagerImpl = BleManagerImpl(
                    app = activity.applicationContext,
                    tcnGenerator = tcnGenerator
                )
                bleManagerImpl.startService()
            }
            mRouter.navigateMainActivity()
        }else{
            mRouter.navigatePermissionBluetooth()
        }

    }

    fun isMyServiceRunning(
        serviceClass: Class<*>
    ): Boolean {
        val manager =
            activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    
}