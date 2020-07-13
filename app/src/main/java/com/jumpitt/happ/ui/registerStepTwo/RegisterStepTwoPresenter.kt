package com.jumpitt.happ.ui.registerStepTwo

import android.app.Activity
import com.jumpitt.happ.R
import com.jumpitt.happ.network.request.RegisterRequest

class RegisterStepTwoPresenter constructor(private val activity: Activity): RegisterStepTwoContract.Presenter, RegisterStepTwoContract.InteractorOutputs {
    private var mInteractor: RegisterStepTwoContract.Interactor = RegisterStepTwoInteractor(this)
    private var mView: RegisterStepTwoContract.View = activity as RegisterStepTwoContract.View
    private var mRouter: RegisterStepTwoContract.Router = RegisterStepTwoRouter(activity)

    override fun initializeView() {
        mView.showInitializeView()
    }

    override fun navigateRegisterLivingPlace(registerDataObject: RegisterRequest){
        mInteractor.getRegisterData(registerDataObject)
    }

    override fun getRegisterDataOutput(registerData: RegisterRequest?, registerDataObject: RegisterRequest) {
        registerData?.let {_registerData ->
            mInteractor.saveRegisterData(_registerData, registerDataObject)
            mRouter.navigateRegisterLivingPlace()
        }?: run{
            mView.showRegisterError(activity.resources.getString(R.string.snkTryAgainLater))
        }
    }

}