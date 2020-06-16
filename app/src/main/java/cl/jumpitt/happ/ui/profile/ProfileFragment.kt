package cl.jumpitt.happ.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cl.jumpitt.happ.R
import cl.jumpitt.happ.network.response.RegisterResponse
import cl.jumpitt.happ.ui.login.Login
import cl.jumpitt.happ.utils.ColorIdResource
import cl.jumpitt.happ.utils.Labelstext
import cl.jumpitt.happ.utils.containedStyle
import cl.jumpitt.happ.utils.goToActivity
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment(), ProfileFragmentContract.View {
    private lateinit var mPresenter: ProfileFragmentContract.Presenter
    private var clickCounter = 0
    private val isClickProfileImage = false
    private val aValidateHiddenPassword = booleanArrayOf(false, false)

    companion object {
        fun newInstance(): ProfileFragment = ProfileFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_profile, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mPresenter = ProfileFragmentPresenter(this)
        mPresenter.initializeView()

        btnCloseSesion.setOnClickListener {
            Hawk.delete("userProfileData")
            requireActivity().goToActivity<Login>("") {
                this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        }

        cvProfileUserData.setOnClickListener {
            clickCounter++
            if(clickCounter > 5){
                aValidateHiddenPassword[0] = true
            }
        }

        ivUserProfile.setOnClickListener {
            if(aValidateHiddenPassword[0]){
                mPresenter.navigateTracingLog()
            }
        }

    }

    override fun showInitializeView() {
        tvProfileName.containedStyle(Labelstext.H6, ColorIdResource.BLACK, font = R.font.dmsans_medium)
        tvProfileRut.containedStyle(Labelstext.SUBTITLE1, ColorIdResource.BLACK)
        tvProfileMail.containedStyle(Labelstext.SUBTITLE1, ColorIdResource.BLACK)
        btnCloseSesion.containedStyle(ColorIdResource.WHITE, ColorIdResource.BLUE)

        mPresenter.getUserProfileData()
    }

    override fun showUnwrappingValues(userData: RegisterResponse) {
        userData.profile?.names?.let {names -> tvProfileName.text = names.capitalize() }
        userData.profile?.lastName?.let {lastName -> tvProfileName.text =  "${tvProfileName.text} ${lastName.capitalize()}"  }
        userData.profile?.rut?.let {rut -> tvProfileRut.text = rut }
        userData.profile?.email?.let {email -> tvProfileMail.text = email }
    }


}
