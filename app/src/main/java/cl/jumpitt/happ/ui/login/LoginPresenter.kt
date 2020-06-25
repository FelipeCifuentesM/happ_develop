package cl.jumpitt.happ.ui.login

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.util.Log
import cl.jumpitt.happ.R
import cl.jumpitt.happ.ble.BleManagerImpl
import cl.jumpitt.happ.ble.TcnGeneratorImpl
import cl.jumpitt.happ.network.request.LoginAccessTokenRequest
import cl.jumpitt.happ.network.response.ErrorResponse
import cl.jumpitt.happ.network.response.LoginAccessTokenResponse
import cl.jumpitt.happ.network.response.ProfileResponse
import cl.jumpitt.happ.utils.isPermissionLocation
import cl.jumpitt.happ.utils.parseErrJsonResponse
import cl.jumpitt.happ.utils.qualifyResponseErrorDefault
import retrofit2.Response

class LoginPresenter constructor(private val activity: Activity): LoginContract.Presenter, LoginContract.InteractorOutputs {
    private var mInteractor: LoginContract.Interactor = LoginInteractor()
    private var mView: LoginContract.View = activity as LoginContract.View
    private var mRouter: LoginContract.Router = LoginRouter(activity)

    override fun initializeView() {
        mView.showInitializeView()
    }

    override fun navigateRegisterStepOne() {
        mRouter.navigateRegisterStepOne()
    }


    override fun postLoginAccessToken(loginRequest: LoginAccessTokenRequest, requestPermissions: Boolean) {
        //review permissions
        if(requestPermissions){
            val permissionGranted = activity.isPermissionLocation()
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
        mInteractor.saveRegisterProfile(dataLoginResponse, accessToken, refreshToken)
        val isRunning = isMyServiceRunning(BleManagerImpl::class.java)
        Log.e("entro0","eda1")
        if(!isRunning) {
            Log.e("entro0","eda")
            val tcnGenerator = TcnGeneratorImpl(context = activity)
            val bleManagerImpl = BleManagerImpl(
                app = activity.applicationContext,
                tcnGenerator = tcnGenerator
            )
            bleManagerImpl.startService()
        }
        mRouter.navigateMain()
    }

    override fun getProfileOutputError(errorCode: Int, response: Response<ProfileResponse>) {
        mView.hideLoader()
        val messageError = response.qualifyResponseErrorDefault(errorCode, activity)
        mView.showValidateLoginError(messageError)
    }


    override fun LoginFailureError() {
        mView.hideLoader()
        mView.showValidateLoginError(activity.resources.getString(R.string.snkDefaultApiError))
    }

}