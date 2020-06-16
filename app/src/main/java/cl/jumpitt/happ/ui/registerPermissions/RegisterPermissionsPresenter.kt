package cl.jumpitt.happ.ui.registerPermissions

import android.app.Activity
import cl.jumpitt.happ.network.request.RegisterRequest
import cl.jumpitt.happ.network.response.RegisterResponse


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

    override fun postRegisterOutputError() {
        mView.showRegisterError()
    }


}