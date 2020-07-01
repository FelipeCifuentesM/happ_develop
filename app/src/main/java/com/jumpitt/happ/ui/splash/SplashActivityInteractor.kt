package com.jumpitt.happ.ui.splash

import com.jumpitt.happ.network.response.RegisterResponse
import com.jumpitt.happ.realm.RegisterData
import com.orhanobut.hawk.Hawk
import io.realm.Realm

class SplashActivityInteractor(private val mIOutput: SplashActivityContract.InteractorOutputs): SplashActivityContract.Interactor{

    override fun getUserProfileData(requestPermissions: Boolean) {
//        val userData = Hawk.get<RegisterResponse>("userProfileData")

        val realm = Realm.getDefaultInstance()
        val userData = realm.where(RegisterData::class.java).findFirst()

        mIOutput.getUserProfileDataOutput(userData, requestPermissions)
    }

}