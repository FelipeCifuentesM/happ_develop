package com.jumpitt.happ.ui.registerPermissions

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.jumpitt.happ.App
import com.jumpitt.happ.R
import com.jumpitt.happ.ble.BleManagerImpl
import com.jumpitt.happ.ble.TcnGeneratorImpl
import com.jumpitt.happ.network.request.RegisterRequest
import com.jumpitt.happ.network.request.TokenFCMRequest
import com.jumpitt.happ.network.response.PingActiveUserResponse
import com.jumpitt.happ.network.response.RegisterResponse
import com.jumpitt.happ.realm.RegisterData
import com.jumpitt.happ.utils.isPermissionBackgroundLocation
import com.jumpitt.happ.utils.qualifyResponseErrorDefault
import io.sentry.Sentry
import retrofit2.Response


class RegisterPermissionsPresenter constructor(private val activity: Activity): RegisterPermissionsContract.Presenter, RegisterPermissionsContract.InteractorOutputs{
    private var mInteractor: RegisterPermissionsContract.Interactor = RegisterPermissionsInteractor()
    private var mView: RegisterPermissionsContract.View = activity as RegisterPermissionsContract.View
    private var mRouter: RegisterPermissionsContract.Router = RegisterPermissionsRouter(activity)
    private var fromLogin: Boolean = false
    private var fromRegister: Boolean = false

    override fun initializeView() {
        mView.showInitializeView()
    }

    override fun navigateMainActivity() {
        mRouter.navigateMainActivity()
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

    override fun validateTcn() {
        val isRunning = isMyServiceRunning(BleManagerImpl::class.java)
        if(!isRunning) {
            val tcnGenerator = TcnGeneratorImpl(context = activity)
            val bleManagerImpl = BleManagerImpl(
                app = activity.applicationContext,
                tcnGenerator = tcnGenerator
            )
            bleManagerImpl.startService()
        }
    }

    override fun runPingActiveUser() {
        fromLogin = true
        mInteractor.getAccessToken(this)
    }

    override fun getRegisterDataOutput(registerData: RegisterRequest?) {
        registerData?.let {
            mInteractor.postRegister(registerData, this)
        }?: run{
            mView.hideLoader()
            mView.showRegisterError(activity.resources.getString(R.string.snkTryAgainLater))
        }
    }


    override fun postRegisterOutput(dataRegisterResponse: RegisterResponse) {
        fromRegister = true
        val isRunning = isMyServiceRunning(BleManagerImpl::class.java)
        if(!isRunning) {
            val tcnGenerator = TcnGeneratorImpl(context = activity)
            val bleManagerImpl = BleManagerImpl(
                app = activity.applicationContext,
                tcnGenerator = tcnGenerator
            )
            bleManagerImpl.startService()
        }

        val userRealm = RegisterData(dataRegisterResponse.profile?.rut ,dataRegisterResponse.profile?.names, dataRegisterResponse.profile?.lastName,
                dataRegisterResponse.profile?.email, dataRegisterResponse.profile?.phone, dataRegisterResponse.profile?.company?.id, dataRegisterResponse.profile?.company?.name,
                dataRegisterResponse.profile?.home?.id, dataRegisterResponse.profile?.work?.id, dataRegisterResponse.accessToken, dataRegisterResponse.refreshToken)
        mInteractor.saveRegisterProfile(userRealm)


        //Get device id for notifications
        FirebaseInstanceId.getInstance().instanceId
            .addOnSuccessListener(activity, OnSuccessListener<InstanceIdResult> { instanceIdResult ->
                val mToken: String? = instanceIdResult.token
                mToken?.let {deviceToken ->
                    val tokenFCMRequest = TokenFCMRequest(deviceToken)
                    mInteractor.postRegisterTokenFCM("${dataRegisterResponse.accessToken}", tokenFCMRequest, this)
                }?: run {
//                    mRouter.navigateRegisterSuccess()
                    mInteractor.getPingUserActive("${userRealm.accessToken}", this)
                }
            })
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

    override fun postRegisterTokenFCMFailureError(accessToken: String) {
//        mRouter.navigateRegisterSuccess()
        mInteractor.getPingUserActive(accessToken, this)
    }

    override fun postRegisterTokenFCMOutput(accessToken: String) {
//        mRouter.navigateRegisterSuccess()
        mInteractor.getPingUserActive(accessToken, this)
    }

    override fun getAccessTokenOutput(accessToken: String) {
        mInteractor.getPingUserActive(accessToken, this)
    }

    override fun getPingUserActiveOutput(dataPingResponse: PingActiveUserResponse) {
        runPing(dataPingResponse)
        if(fromLogin) mRouter.navigateMainActivity()
        if(fromRegister){
            mView.hideLoader()
            mRouter.navigateRegisterSuccess()
        }
    }

    override fun getPingUserActiveOutputError(dataPingResponse: PingActiveUserResponse, responseCode: Int) {
        runPing(dataPingResponse)
        if(fromLogin) mRouter.navigateMainActivity()
        if(fromRegister) {
            mView.hideLoader()
            mRouter.navigateRegisterSuccess()
        }
        //sentry
        if(responseCode == 200)
            Sentry.capture(activity.resources.getString(R.string.errSentryLoadApiPingNull))
        else
            Sentry.capture(String.format(activity.resources.getString(R.string.errSentryLoadApiPingError), responseCode))
    }

    override fun getPingUserActiveFailureError(dataPingResponse: PingActiveUserResponse) {
        runPing(dataPingResponse)
        if(fromLogin) mRouter.navigateMainActivity()
        if(fromRegister){
            mView.hideLoader()
            mRouter.navigateRegisterSuccess()
        }
        Sentry.capture(activity.resources.getString(R.string.errSentryLoadApiPingFailure))
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

    private fun runPing(dataPingResponse: PingActiveUserResponse){
        var requestTime: Long = 3600 * 1000 //miliseconds
        dataPingResponse.refresh?.let { dataRequestTime -> requestTime = (dataRequestTime * 1000).toLong() }

        App.handler?.let { mHandler ->
            mHandler.postDelayed({
                fromLogin = false
                fromRegister = false
                mInteractor.getAccessToken(this)
            }, requestTime)
        }
    }

}