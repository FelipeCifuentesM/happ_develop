package com.jumpitt.happ.ui.registerSuccess

import android.app.Activity

class RegisterSuccessPresenter constructor(private val activity: Activity): RegisterSuccessContract.Presenter, RegisterSuccessContract.InteractorOutputs {
    private var mInteractor: RegisterSuccessContract.Interactor = RegisterSuccessInteractor()
    private var mView: RegisterSuccessContract.View = activity as RegisterSuccessContract.View
    private var mRouter: RegisterSuccessContract.Router = RegisterSuccessRouter(activity)

}