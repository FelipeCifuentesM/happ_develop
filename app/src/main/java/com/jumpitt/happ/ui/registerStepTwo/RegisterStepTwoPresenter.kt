package com.jumpitt.happ.ui.registerStepTwo

import android.app.Activity
import com.jumpitt.happ.network.request.RegisterRequest

class RegisterStepTwoPresenter constructor(private val activity: Activity): RegisterStepTwoContract.Presenter, RegisterStepTwoContract.InteractorOutputs {
    private var mInteractor: RegisterStepTwoContract.Interactor = RegisterStepTwoInteractor()
    private var mView: RegisterStepTwoContract.View = activity as RegisterStepTwoContract.View
    private var mRouter: RegisterStepTwoContract.Router = RegisterStepTwoRouter(activity)

    override fun initializeView() {
        mView.showInitializeView()
    }

    override fun navigateRegisterLivingPlace(registerDataObject: RegisterRequest){
        mInteractor.saveRegisterData(registerDataObject)
        mRouter.navigateRegisterLivingPlace()
    }

}