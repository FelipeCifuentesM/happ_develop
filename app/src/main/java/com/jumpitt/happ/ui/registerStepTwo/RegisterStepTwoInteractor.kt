package com.jumpitt.happ.ui.registerStepTwo

import com.jumpitt.happ.network.request.RegisterRequest
import com.jumpitt.happ.ui.triage.TriageActivityContract
import com.orhanobut.hawk.Hawk

class RegisterStepTwoInteractor(private val mIOutput: RegisterStepTwoContract.InteractorOutputs): RegisterStepTwoContract.Interactor{

    override fun getRegisterData(registerDataObject: RegisterRequest) {
        val registerData:RegisterRequest?  = Hawk.get<RegisterRequest>("registerData")
        mIOutput.getRegisterDataOutput(registerData, registerDataObject)
    }

    override fun saveRegisterData(registerData: RegisterRequest, registerDataObject: RegisterRequest) {
        registerData.names = registerDataObject.names
        registerData.lastName = registerDataObject.lastName
        registerData.email = registerDataObject.email
        registerData.password = registerDataObject.password
        Hawk.put("registerData", registerData)
    }

}