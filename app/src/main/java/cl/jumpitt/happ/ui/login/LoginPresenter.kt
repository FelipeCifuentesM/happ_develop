package cl.jumpitt.happ.ui.login

import android.app.Activity
import cl.jumpitt.happ.network.request.LoginAccessTokenRequest
import cl.jumpitt.happ.network.response.LoginAccessTokenResponse
import cl.jumpitt.happ.network.response.ProfileResponse

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

    override fun postLoginAccessTokenOutputError() {

    }

    override fun getProfileOutput(dataLoginResponse: ProfileResponse, accessToken: String) {
        mInteractor.saveRegisterProfile(dataLoginResponse, accessToken)
        mRouter.navigateMain()
    }

    override fun getProfileOutputError() {

    }

}