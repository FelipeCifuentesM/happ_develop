package com.jumpitt.happ.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jumpitt.happ.R
import com.jumpitt.happ.network.response.RegisterResponse
import com.jumpitt.happ.realm.RegisterData
import com.jumpitt.happ.ui.TracingLogActivity
import com.jumpitt.happ.ui.changePassword.ChangePasswordActivity
import com.jumpitt.happ.ui.main.MainActivity
import com.jumpitt.happ.utils.*
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

        btnCloseSesion.setSafeOnClickListener {
            mPresenter.deleteProfileData()
        }

        btnEnterChangePass.setSafeOnClickListener {
            mPresenter.navigateChangePassword(this, activity)
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
        btnEnterChangePass.containedStyle(ColorIdResource.WHITE, ColorIdResource.PRIMARY)
        btnCloseSesion.containedStyle(ColorIdResource.WHITE, ColorIdResource.BLACK)

        mPresenter.getUserProfileData()
    }

    override fun showUnwrappingValues(userData: RegisterData?) {
        tvRoundedToolbar.text = resources.getString(R.string.tbProfile)
        userData?.names?.let {names -> tvProfileName.text = names.capitalize() }
        userData?.lastName?.let {lastName -> tvProfileName.text =  "${tvProfileName.text} ${lastName.capitalize()}"  }
        userData?.rut?.let {rut -> tvProfileRut.text = rut.rutFormat() }
        userData?.email?.let {email -> tvProfileMail.text = email }
    }

    override fun showSnackBar(message: String) {
        activity?.showSnackbar(containerFragmentProfile, message, ColorIdResource.PRIMARY, ColorIdResource.WHITE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RequestCode.FROM_PROFILE_FRAGMENT){
            showSnackBar(resources.getString(R.string.snkChangePassSuccess))
        }
    }

}
