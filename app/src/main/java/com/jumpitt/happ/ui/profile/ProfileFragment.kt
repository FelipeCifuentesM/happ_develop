package com.jumpitt.happ.ui.profile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startForegroundService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jumpitt.happ.App
import com.jumpitt.happ.R
import com.jumpitt.happ.model.ProfileMenu
import com.jumpitt.happ.ping.EndlessService
import com.jumpitt.happ.ping.ServiceState
import com.jumpitt.happ.ping.getServiceState
import com.jumpitt.happ.realm.RegisterData
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

//        btnCloseSesion.setSafeOnClickListener {
//            mPresenter.deleteProfileData()
//        }

        btnEnterChangePass.setSafeOnClickListener {
            mPresenter.navigateChangePassword(this, activity)
        }

//        btnPrivacyPolicies.setSafeOnClickListener {
//            mPresenter.navigatePrivacyPolicies()
//        }
//
//        btnFrequentQuestions.setSafeOnClickListener {
//            mPresenter.navigateFrequentQuestions()
//        }

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

    override fun initDataListProfile() {
        val profileMenuListObject: ArrayList<ProfileMenu> = ArrayList()
        profileMenuListObject.add(ProfileMenu(ProfileMenuOptions.PRIVACY_POLICIES.optionIcon, ProfileMenuOptions.PRIVACY_POLICIES.optionText))
        profileMenuListObject.add(ProfileMenu(ProfileMenuOptions.FREQUENT_QUESTIONS.optionIcon, ProfileMenuOptions.FREQUENT_QUESTIONS.optionText))
        profileMenuListObject.add(ProfileMenu(ProfileMenuOptions.LOGOUT.optionIcon, ProfileMenuOptions.LOGOUT.optionText))

        mPresenter.showInitializeView(profileMenuListObject)
    }

    override fun showInitializeView(profileMenuListObject: ArrayList<ProfileMenu>) {
        tvRoundedToolbar.containedStyle(Labelstext.H6, ColorIdResource.BLACK, font = R.font.dmsans_medium)
        tvProfileName.containedStyle(Labelstext.H6, ColorIdResource.BLACK, font = R.font.dmsans_medium)
        tvProfileRut.containedStyle(Labelstext.SUBTITLE1, ColorIdResource.BLACK)
        tvProfileMail.containedStyle(Labelstext.SUBTITLE1, ColorIdResource.BLACK)
        btnEnterChangePass.containedStyle(ColorIdResource.WHITE, ColorIdResource.PRIMARY)
        tvHappDoesNotCollectData.containedStyle(Labelstext.BODY1, ColorIdResource.BLACK)
//        btnPrivacyPolicies.containedStyle(ColorIdResource.WHITE, ColorIdResource.PRIMARY)
//        btnFrequentQuestions.containedStyle(ColorIdResource.WHITE, ColorIdResource.PRIMARY)
//        btnCloseSesion.containedStyle(ColorIdResource.WHITE, ColorIdResource.PRIMARY)

        val layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
        rvProfileMenu.layoutManager = layoutManager
        rvProfileMenu.adapter = AdapterProfileMenu(requireActivity(), profileMenuListObject, object: AdapterProfileMenu.ClickListener{
            override fun itemClickProfileMenu(position: Int) {

                if (position == 2){
                    actionOnService(Actions.STOP)
                }
                mPresenter.clickListenerItemProfileMenu(position)
            }
        })

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

    override fun stopHandlerPingActiveUser(){
        App.handler?.let { mHandler ->
            mHandler.removeCallbacksAndMessages(null)
        }
    }


    private fun actionOnService(action: Actions) {
        if (getServiceState(activity!!) == ServiceState.STOPPED && action == Actions.STOP) return
        Intent(activity!!, EndlessService::class.java).also {
            it.action = action.name
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                log("Starting the service in >=26 Mode")
                activity!!.startForegroundService(it)
                return
            }
            log("Starting the service in < 26 Mode")
            activity!!.startService(it)
        }
    }
}
