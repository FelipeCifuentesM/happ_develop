package com.jumpitt.happ.ui.splash

import android.app.Activity
import com.jumpitt.happ.network.response.RegisterResponse
import com.jumpitt.happ.utils.isPermissionLocation

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
                    mRouter.navigateMainActivity()
                }
            }else{
                mRouter.navigateMainActivity()
            }
        }else{
            mRouter.navigateOnBoard()
        }
    }
}