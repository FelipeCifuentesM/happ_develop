package com.jumpitt.happ.ui.profile

import com.jumpitt.happ.App
import com.jumpitt.happ.ble.BleManagerImpl
import com.jumpitt.happ.ble.TcnGeneratorImpl
import com.jumpitt.happ.realm.RegisterData
import com.jumpitt.happ.realm.TriageReturnValue
import io.realm.Realm

class ProfileFragmentInteractor(private val mIOutput: ProfileFragmentContract.InteractorOutputs): ProfileFragmentContract.Interactor {

    override fun getUserProfileData() {
        val realm = Realm.getDefaultInstance()
        val userData = realm.where(RegisterData::class.java).findFirst()
        mIOutput.getUserProfileDataOutput(userData)
    }

    override fun deleteProfileData() {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.delete(RegisterData::class.java)
        realm.delete(TriageReturnValue::class.java)
        realm.commitTransaction()
        realm.close()

        val tcnGenerator = TcnGeneratorImpl(context = App.ctx!!)
        val bleManagerImpl = BleManagerImpl(
            app = App.ctx!!,
            tcnGenerator = tcnGenerator
        )
        bleManagerImpl.stopService()
    }


}