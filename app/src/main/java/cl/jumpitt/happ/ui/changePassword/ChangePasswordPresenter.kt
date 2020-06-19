package cl.jumpitt.happ.ui.changePassword

import android.app.Activity

class ChangePasswordPresenter constructor(private val activity: Activity): ChangePasswordContract.Presenter, ChangePasswordContract.InteractorOutputs {
    private var mInteractor: ChangePasswordContract.Interactor = ChangePasswordInteractor()
    private var mView: ChangePasswordContract.View = activity as ChangePasswordContract.View
    private var mRouter: ChangePasswordContract.Router = ChangePasswordRouter(activity)

    override fun initializeView() {
        mView.showInitializeView()
    }
}