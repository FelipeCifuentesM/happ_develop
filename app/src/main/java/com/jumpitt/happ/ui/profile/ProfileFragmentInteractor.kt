package com.jumpitt.happ.ui.profile

import com.jumpitt.happ.network.response.RegisterResponse
import com.orhanobut.hawk.Hawk

class ProfileFragmentInteractor(private val mIOutput: ProfileFragmentContract.InteractorOutputs): ProfileFragmentContract.Interactor {

    override fun getUserProfileData() {
        val userData = Hawk.get<RegisterResponse>("userProfileData")
        mIOutput.getUserProfileDataOutput(userData)
    }

    override fun deleteProfileData() {
        Hawk.delete("userProfileData")
    }


}