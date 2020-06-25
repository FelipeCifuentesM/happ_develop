package cl.jumpitt.happ.ui.profile

import cl.jumpitt.happ.App
import cl.jumpitt.happ.ble.BleManagerImpl
import cl.jumpitt.happ.ble.TcnGeneratorImpl
import cl.jumpitt.happ.network.response.RegisterResponse
import com.orhanobut.hawk.Hawk

class ProfileFragmentInteractor(private val mIOutput: ProfileFragmentContract.InteractorOutputs): ProfileFragmentContract.Interactor {

    override fun getUserProfileData() {
        val userData = Hawk.get<RegisterResponse>("userProfileData")
        mIOutput.getUserProfileDataOutput(userData)
    }

    override fun deleteProfileData() {
        Hawk.delete("userProfileData")
        val tcnGenerator = TcnGeneratorImpl(context = App.ctx!!)
        val bleManagerImpl = BleManagerImpl(
            app = App.ctx!!,
            tcnGenerator = tcnGenerator
        )
        bleManagerImpl.stopService()
    }


}