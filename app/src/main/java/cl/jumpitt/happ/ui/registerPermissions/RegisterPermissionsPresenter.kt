package cl.jumpitt.happ.ui.registerPermissions

import android.app.Activity
import cl.jumpitt.happ.R
import cl.jumpitt.happ.network.request.RegisterRequest
import cl.jumpitt.happ.network.response.ErrorResponse
import cl.jumpitt.happ.network.response.RegisterResponse
import cl.jumpitt.happ.utils.parseErrJsonResponse
import cl.jumpitt.happ.utils.qualifyResponseErrorDefault
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

    override fun getRegisterData() {
        mInteractor.getRegisterData(this)
    }

    override fun getRegisterDataOutput(registerData: RegisterRequest) {
        mInteractor.postRegister(registerData, this)
    }


    override fun postRegisterOutput(dataRegisterResponse: RegisterResponse) {
        mInteractor.saveRegisterProfile(dataRegisterResponse)
        mRouter.navigateRegisterSuccess()
    }

    override fun postRegisterOutputError(errorCode: Int, response: Response<RegisterResponse>) {
        val messageError = response.qualifyResponseErrorDefault(errorCode, activity)
        mView.showRegisterError(messageError)
    }

    override fun postRegisterFailureError() {
        mView.showRegisterError(activity.resources.getString(R.string.snkDefaultApiError))
    }

}