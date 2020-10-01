package com.jumpitt.happ.ui.login

import android.app.Activity
import android.app.ActivityManager
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.os.Handler
import android.util.Log
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
import kotlinx.android.synthetic.main.activity_main.*
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
                    .addOnSuccessListener(activity, OnSuccessListener<InstanceIdResult> { instanceIdResult ->
                        val mToken: String? = instanceIdResult.token
                        mToken?.let {deviceToken ->
                            val tokenFCMRequest = TokenFCMRequest(deviceToken)
                            mInteractor.postRegisterTokenFCM("${ConstantsApi.BEARER} ${userRealm.accessToken}", tokenFCMRequest, this)
                        }?: run {
                            mInteractor.getPingUserActive("${ConstantsApi.BEARER} ${userRealm.accessToken}", this)
                        }
                    })
            }else{
                mRouter.navigatePermissionBluetooth()
            }
        }?: run {
            mView.showValidateLoginError(activity.resources.getString(R.string.snkBluetoothNotAvailable))
        }
    }


    override fun navigateRegisterStepOne() {
        mRouter.navigateRegisterStepOne()
    }


    override fun postLoginAccessToken(loginRequest: LoginAccessTokenRequest, requestPermissions: Boolean) {
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

    override fun postLoginAccessTokenOutputError(errorCode: Int, response: Response<LoginAccessTokenResponse>) {
        mView.hideLoader()
        when(errorCode){
            200 -> {
                mView.showValidateLoginError(activity.resources.getString(R.string.snkDataNullError))
            }
            401 -> {
                val errorResponse = response.parseErrJsonResponse<ErrorResponse>()
                errorResponse.errorMessage?.let {messageError ->
                    mView.showValidateLoginError(messageError)
                }?: run{
                    mView.showValidateLoginError(activity.resources.getString(R.string.snkDefaultApiError))
                }
            }
            422 -> {
                val errorResponse = response.parseErrJsonResponse<ErrorResponse>()
                errorResponse.errorMessage?.let {messageError ->
                    mView.showValidateLoginError(messageError)
                }?: run{
                    mView.showValidateLoginError(activity.resources.getString(R.string.snkDefaultApiError))
                }

            }
            else -> mView.showValidateLoginError(activity.resources.getString(R.string.snkDefaultApiError))
        }
    }


    override fun getProfileOutput(dataLoginResponse: ProfileResponse, accessToken: String, refreshToken: String) {
        mView.hideLoader()
        val userRealm = RegisterData(dataLoginResponse.rut ,dataLoginResponse.names, dataLoginResponse.lastName,
            dataLoginResponse.email, dataLoginResponse.phone, dataLoginResponse.home?.id, dataLoginResponse.work?.id,
            accessToken, refreshToken)
        validateBluetoothState(userRealm)
    }

    override fun getProfileOutputError(errorCode: Int, response: Response<ProfileResponse>) {
        mView.hideLoader()
        val messageError = response.qualifyResponseErrorDefault(errorCode, activity)
        mView.showValidateLoginError(messageError)
    }

    override fun postRegisterTokenFCMFailureError(accessToken: String) {
        Log.e("Borrar", "MAIN 1 $navigateMain")
        mInteractor.getPingUserActive(accessToken, this)
        if(navigateMain) mRouter.navigateMain()
    }

    override fun postRegisterTokenFCMOutput(accessToken: String) {
        Log.e("Borrar", "MAIN 2 $navigateMain")
        mInteractor.getPingUserActive(accessToken, this)
        if(navigateMain) mRouter.navigateMain()
    }

    override fun loginFailureError() {
        mView.hideLoader()
        mView.showValidateLoginError(activity.resources.getString(R.string.snkDefaultApiError))
    }

    override fun getPingUserActiveOutput(dataPingResponse: PingActiveUserResponse) {
        runPing(dataPingResponse)
        Log.e("Borrar", "MAIN 3 $navigateMain")
        if(navigateMain) mRouter.navigateMain()
    }

    override fun getPingUserActiveOutputError(dataPingResponse: PingActiveUserResponse) {
        runPing(dataPingResponse)
        Log.e("Borrar", "MAIN 4 $navigateMain")
        if(navigateMain) mRouter.navigateMain()
    }

    override fun getPingUserActiveFailureError(dataPingResponse: PingActiveUserResponse) {
        runPing(dataPingResponse)
        Log.e("Borrar", "MAIN 5 $navigateMain")
        if(navigateMain) mRouter.navigateMain()
    }

    override fun getAccessTokenOutput(accessToken: String) {
        mInteractor.getPingUserActive(accessToken, this)
    }

    private fun runPing(dataPingResponse: PingActiveUserResponse){
        Log.e("Borrar", "PING <-------------------------")
        var requestTime: Long = 3600 * 1000
        dataPingResponse.requestTime?.let { dataRequestTime -> requestTime = (dataRequestTime * 1000).toLong() }
        Log.e("Borrar", "Request time: $requestTime milisegundos")

        App.handler?.let { mHandler ->
            mHandler.postDelayed({
                Log.e("Borrar", "PING VOLVER A PEDIR")
                navigateMain = false
                mInteractor.getAccessToken(this)
            }, requestTime)
        }

    }

}