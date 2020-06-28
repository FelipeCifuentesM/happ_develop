package com.jumpitt.happ.ui.registerStepTwo

import com.jumpitt.happ.network.request.RegisterRequest
import com.orhanobut.hawk.Hawk

class RegisterStepTwoInteractor: RegisterStepTwoContract.Interactor{

    override fun saveRegisterData(registerDataObject: RegisterRequest) {
        val registerData = Hawk.get<RegisterRequest>("registerData")
        registerData.names = registerDataObject.names
        registerData.lastName = registerDataObject.lastName
        registerData.email = registerDataObject.email
        registerData.password = registerDataObject.password
        Hawk.put("registerData", registerData)
    }

}