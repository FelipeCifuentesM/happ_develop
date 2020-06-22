package cl.jumpitt.happ.ui.splash

import cl.jumpitt.happ.network.response.RegisterResponse
import com.orhanobut.hawk.Hawk

class SplashActivityInteractor(private val mIOutput: SplashActivityContract.InteractorOutputs): SplashActivityContract.Interactor{

    override fun getUserProfileData(requestPermissions: Boolean) {
        val userData = Hawk.get<RegisterResponse>("userProfileData")
        mIOutput.getUserProfileDataOutput(userData, requestPermissions)
    }

}