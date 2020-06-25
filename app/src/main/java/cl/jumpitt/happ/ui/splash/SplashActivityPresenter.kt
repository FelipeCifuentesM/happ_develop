package cl.jumpitt.happ.ui.splash

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import cl.jumpitt.happ.ble.BleManagerImpl
import cl.jumpitt.happ.ble.TcnGeneratorImpl
import cl.jumpitt.happ.network.response.RegisterResponse
import cl.jumpitt.happ.utils.isPermissionLocation

class SplashActivityPresenter constructor(private val activity: Activity): SplashActivityContract.Presenter, SplashActivityContract.InteractorOutputs {
    private var mInteractor: SplashActivityContract.Interactor = SplashActivityInteractor(this)
    private var mView: SplashActivityContract.View = activity as SplashActivityContract.View
    private var mRouter: SplashActivityContract.Router = SplashActivityRouter(activity)

    override fun getUserProfileData(requestPermissions: Boolean) {
        mInteractor.getUserProfileData(requestPermissions)
    }

    override fun getUserProfileDataOutput(userData: RegisterResponse?, requestPermissions: Boolean) {
        if(userData!=null){
            //review permissions
            if(requestPermissions){



                val permissionGranted = activity.isPermissionLocation()
                if(permissionGranted){
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
                }
            }else{
                mRouter.navigateMainActivity()
            }
        }else{
            mRouter.navigateOnBoard()
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