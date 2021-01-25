package com.jumpitt.happ.ui.login

import android.app.Activity
import android.app.ActivityManager
import android.bluetooth.BluetoothAdapter
import android.content.Context
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.jumpitt.happ.App
import com.jumpitt.happ.R
import com.jumpitt.happ.ble.BleManagerImpl
import com.jumpitt.happ.ble.TcnGeneratorImpl
import com.jumpitt.happ.network.request.LoginAccessTokenRequest
import com.jumpitt.happ.network.request.TokenFCMRequest
import com.jumpitt.happ.network.response.*
import com.jumpitt.happ.realm.RegisterData
import com.jumpitt.happ.utils.*
import io.sentry.Sentry
import retrofit2.Response


class LoginPresenter constructor(private val activity: Activity): LoginContract.Presenter, LoginContract.InteractorOutputs {
    private var mInteractor: LoginContract.Interactor = LoginInteractor()
    private var mView: LoginContract.View = activity as LoginContract.View
    private var mRouter: LoginContract.Router = LoginRouter(activity)
    private var navigateMain: Boolean = true

    override fun initializeView() {
        mView.showInitializeView()
    }


    override fun validateBluetoothState(userRealm: RegisterData) {
        val mBluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        mBluetoothAdapter?.let { bluetoothAdapter ->
            mInteractor.saveRegisterProfile(userRealm)
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

                //Get device id for notifications
                FirebaseInstanceId.getInstance().instanceId
                    .addOnSuccessListener(
                        activity,
                        OnSuccessListener<InstanceIdResult> { instanceIdResult ->
                            val mToken: String? = instanceIdResult.token
                            mToken?.let { deviceToken ->
                                val tokenFCMRequest = TokenFCMRequest(deviceToken)
                                mInteractor.postRegisterTokenFCM(
                                    "${userRealm.accessToken}",
                                    tokenFCMRequest,
                                    this
                                )
                            } ?: run {
                                mInteractor.getPingUserActive("${userRealm.accessToken}", this)
                            }
                        })
            }else{
                mView.hideLoader()
                mRouter.navigatePermissionBluetooth()
            }
        }?: run {
            mView.hideLoader()
            mView.showValidateLoginError(activity.resources.getString(R.string.snkBluetoothNotAvailable))
        }
    }

    override fun deleteProfileData() {
        mInteractor.deleteProfileData()
    }


    override fun navigateRegisterStepOne() {
        mRouter.navigateRegisterStepOne()
    }


    override fun postLoginAccessToken(
        loginRequest: LoginAccessTokenRequest,
        requestPermissions: Boolean
    ) {
        //review permissions
        navigateMain = true
        if(requestPermissions){
            val permissionGranted = activity.isPermissionBackgroundLocation()
            if(permissionGranted){
                mView.showLoader()
                mInteractor.postLoginAccessToken(loginRequest, this)
            }
        }else{
            mView.showLoader()
            mInteractor.postLoginAccessToken(loginRequest, this)
        }

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

    override fun navigateRecoverPass() {
        mRouter.navigateRecoverPass()
    }

    override fun postLoginAccessTokenOutput(dataResponseToken: LoginAccessTokenResponse) {
        mInteractor.getProfile(dataResponseToken, this)
    }

    override fun postLoginAccessTokenOutputError(
        errorCode: Int,
        response: Response<LoginAccessTokenResponse>
    ) {
        mView.hideLoader()
        when(errorCode){
            200 -> {
                mView.showValidateLoginError(activity.resources.getString(R.string.snkDataNullError))
            }
            401 -> {
                val errorResponse = response.parseErrJsonResponse<ErrorResponse>()
                errorResponse.errorMessage?.let { messageError ->
                    mView.showValidateLoginError(messageError)
                } ?: run {
                    mView.showValidateLoginError(activity.resources.getString(R.string.snkDefaultApiError))
                }
            }
            422 -> {
                val errorResponse = response.parseErrJsonResponse<ErrorResponse>()
                errorResponse.errorMessage?.let { messageError ->
                    mView.showValidateLoginError(messageError)
                } ?: run {
                    mView.showValidateLoginError(activity.resources.getString(R.string.snkDefaultApiError))
                }

            }
            else -> mView.showValidateLoginError(activity.resources.getString(R.string.snkDefaultApiError))
        }
    }


    override fun getProfileOutput(
        dataLoginResponse: ProfileResponse,
        accessToken: String,
        refreshToken: String
    ) {
//        mView.hideLoader()
        val userRealm = RegisterData(
            dataLoginResponse.rut,
            dataLoginResponse.names,
            dataLoginResponse.lastName,
            dataLoginResponse.email,
            dataLoginResponse.phone,
            dataLoginResponse.company?.id,
            dataLoginResponse.company?.name,
            dataLoginResponse.home?.id,
            dataLoginResponse.work?.id,
            accessToken,
            refreshToken
        )
        validateBluetoothState(userRealm)
    }

    override fun getProfileOutputError(errorCode: Int, response: Response<ProfileResponse>) {
        mView.hideLoader()
        val messageError = response.qualifyResponseErrorDefault(errorCode, activity)
        mView.showValidateLoginError(messageError)
    }

    override fun postRegisterTokenFCMFailureError(accessToken: String) {
        mInteractor.getPingUserActive(accessToken, this)
    }

    override fun postRegisterTokenFCMOutput(accessToken: String) {
        mInteractor.getPingUserActive(accessToken, this)
    }

    override fun loginFailureError() {
        mView.hideLoader()
        mView.showValidateLoginError(activity.resources.getString(R.string.snkDefaultApiError))
    }

    override fun getPingUserActiveOutput(dataPingResponse: PingActiveUserResponse) {
        runPing(dataPingResponse)
        if(navigateMain) {
            mView.hideLoader()
            mRouter.navigateMain()
        }

    }

    override fun getPingUserActiveOutputError(
        dataPingResponse: PingActiveUserResponse,
        responseCode: Int
    ) {
        runPing(dataPingResponse)
        if(navigateMain) {
            mView.hideLoader()
            mRouter.navigateMain()
        }
        //Sentry
        if(responseCode == 200)
            Sentry.capture(activity.resources.getString(R.string.errSentryLoadApiPingNull))
        else
            Sentry.capture(
                String.format(
                    activity.resources.getString(R.string.errSentryLoadApiPingError),
                    responseCode
                )
            )
    }

    override fun getPingUserActiveFailureError(dataPingResponse: PingActiveUserResponse) {
        runPing(dataPingResponse)
        if(navigateMain) {
            mView.hideLoader()
            mRouter.navigateMain()
        }
        Sentry.capture(activity.resources.getString(R.string.errSentryLoadApiPingFailure))
    }

    override fun getAccessTokenOutput(accessToken: String) {
        mInteractor.getPingUserActive(accessToken, this)
    }

    private fun runPing(dataPingResponse: PingActiveUserResponse){
        var requestTime: Long = 3600 * 1000 //miliseconds
        dataPingResponse.refresh?.let { dataRequestTime -> requestTime = (dataRequestTime * 1000).toLong() }

        App.handler?.let { mHandler ->
            mHandler.postDelayed({
                navigateMain = false
                mInteractor.getAccessToken(this)
            }, requestTime)
        }

    }

}