package cl.jumpitt.happ.ui.login

import android.app.Activity
import cl.jumpitt.happ.R
import cl.jumpitt.happ.network.request.LoginAccessTokenRequest
import cl.jumpitt.happ.network.response.ErrorResponse
import cl.jumpitt.happ.network.response.LoginAccessTokenResponse
import cl.jumpitt.happ.network.response.ProfileResponse
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

    override fun postLoginAccessToken(loginRequest: LoginAccessTokenRequest) {
        mInteractor.postLoginAccessToken(loginRequest, this)
    }

    override fun postLoginAccessTokenOutput(dataResponseToken: LoginAccessTokenResponse) {
        mInteractor.getProfile(dataResponseToken, this)
    }

    override fun postLoginAccessTokenOutputError(errorCode: Int, response: Response<LoginAccessTokenResponse>) {
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


    override fun getProfileOutput(dataLoginResponse: ProfileResponse, accessToken: String) {
        mInteractor.saveRegisterProfile(dataLoginResponse, accessToken)
        mRouter.navigateMain()
    }

    override fun getProfileOutputError(errorCode: Int, response: Response<ProfileResponse>) {
        val messageError = response.qualifyResponseErrorDefault(errorCode, activity)
        mView.showValidateLoginError(messageError)
    }


    override fun LoginFailureError() {
        mView.showValidateLoginError(activity.resources.getString(R.string.snkDefaultApiError))
    }

}