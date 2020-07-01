package com.jumpitt.happ.ui.registerPermissions

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import com.jumpitt.happ.R
import com.jumpitt.happ.ble.BleManagerImpl
import com.jumpitt.happ.ble.TcnGeneratorImpl
import com.jumpitt.happ.network.request.RegisterRequest
import com.jumpitt.happ.network.response.RegisterResponse
import com.jumpitt.happ.realm.RegisterData
import com.jumpitt.happ.utils.isPermissionBackgroundLocation
import com.jumpitt.happ.utils.qualifyResponseErrorDefault
import retrofit2.Response


class RegisterPermissionsPresenter constructor(private val activity: Activity): RegisterPermissionsContract.Presenter, RegisterPermissionsContract.InteractorOutputs{
    private var mInteractor: RegisterPermissionsContract.Interactor = RegisterPermissionsInteractor()
    private var mView: RegisterPermissionsContract.View = activity as RegisterPermissionsContract.View
    private var mRouter: RegisterPermissionsContract.Router = RegisterPermissionsRouter(activity)

    override fun initializeView() {
        mView.showInitializeView()
    }

    override fun navigateRegisterSuccess() {
        mRouter.navigateRegisterSuccess()
    }

    override fun getRegisterData(requestPermissions: Boolean) {
        //review permissions
        if(requestPermissions){
            val permissionGranted = activity.isPermissionBackgroundLocation()
            if(permissionGranted){
                mView.showLoader()
                mInteractor.getRegisterData(this)
            }
        }else{
            mView.showLoader()
            mInteractor.getRegisterData(this)
        }
    }

    override fun getRegisterDataOutput(registerData: RegisterRequest) {
        mInteractor.postRegister(registerData, this)
    }


    override fun postRegisterOutput(dataRegisterResponse: RegisterResponse) {

        val isRunning = isMyServiceRunning(BleManagerImpl::class.java)
        if(!isRunning) {
            val tcnGenerator = TcnGeneratorImpl(context = activity)
            val bleManagerImpl = BleManagerImpl(
                app = activity.applicationContext,
                tcnGenerator = tcnGenerator
            )
            bleManagerImpl.startService()
        }

        mView.hideLoader()
        val userRealm = RegisterData(dataRegisterResponse.profile?.rut ,dataRegisterResponse.profile?.names, dataRegisterResponse.profile?.lastName,
                dataRegisterResponse.profile?.email, dataRegisterResponse.profile?.phone, dataRegisterResponse.profile?.home?.id, dataRegisterResponse.profile?.work?.id,
                dataRegisterResponse.accessToken, dataRegisterResponse.refreshToken)
        mInteractor.saveRegisterProfile(userRealm)
        mRouter.navigateRegisterSuccess()
    }

    override fun postRegisterOutputError(errorCode: Int, response: Response<RegisterResponse>) {
        mView.hideLoader()
        val messageError = response.qualifyResponseErrorDefault(errorCode, activity)
        mView.showRegisterError(messageError)
    }

    override fun postRegisterFailureError() {
        mView.hideLoader()
        mView.showRegisterError(activity.resources.getString(R.string.snkDefaultApiError))
    }

    private fun isMyServiceRunning(
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