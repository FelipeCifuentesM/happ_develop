package cl.jumpitt.happ.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cl.jumpitt.happ.R
import cl.jumpitt.happ.network.response.RegisterResponse
import cl.jumpitt.happ.utils.ColorIdResource
import cl.jumpitt.happ.utils.Labelstext
import cl.jumpitt.happ.utils.containedStyle
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.item_rounded_toolbar.*


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
            mPresenter.deleteProfileData()
        }

        btnEnterChangePass.setOnClickListener {
            mPresenter.navigateChangePassword()
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
        tvRoundedToolbar.containedStyle(Labelstext.H6, ColorIdResource.BLACK, font = R.font.dmsans_medium)
        tvProfileName.containedStyle(Labelstext.H6, ColorIdResource.BLACK, font = R.font.dmsans_medium)
        tvProfileRut.containedStyle(Labelstext.SUBTITLE1, ColorIdResource.BLACK)
        tvProfileMail.containedStyle(Labelstext.SUBTITLE1, ColorIdResource.BLACK)
        btnEnterChangePass.containedStyle(ColorIdResource.WHITE, ColorIdResource.BLUE)
        btnCloseSesion.containedStyle(ColorIdResource.WHITE, ColorIdResource.BLACK)

        mPresenter.getUserProfileData()
    }

    override fun showUnwrappingValues(userData: RegisterResponse) {
        tvRoundedToolbar.text = resources.getString(R.string.tbProfile)
        userData.profile?.names?.let {names -> tvProfileName.text = names.capitalize() }
        userData.profile?.lastName?.let {lastName -> tvProfileName.text =  "${tvProfileName.text} ${lastName.capitalize()}"  }
        userData.profile?.rut?.let {rut -> tvProfileRut.text = rut }
        userData.profile?.email?.let {email -> tvProfileMail.text = email }
    }


}
