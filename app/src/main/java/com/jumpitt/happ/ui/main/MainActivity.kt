package com.jumpitt.happ.ui.main

import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.google.firebase.messaging.FirebaseMessaging
import com.jumpitt.happ.R
import com.jumpitt.happ.network.response.TriageAnswerResponse
import com.jumpitt.happ.ui.*
import com.jumpitt.happ.ui.happHome.HappHomeFragment
import com.jumpitt.happ.ui.notifications.NotificationsFragment
import com.jumpitt.happ.ui.profile.ProfileFragment
import com.jumpitt.happ.utils.*
import io.sentry.Sentry
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MainActivityContract.View {
    private lateinit var mPresenter: MainActivityContract.Presenter
    private var healthCareStatusCopy: TriageAnswerResponse? = null
    private var isShowRiskFragment = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mPresenter = MainActivityPresenter(this)
        mPresenter.loadFragmentHappHome()
//        mPresenter.getAccessToken()

        //Notificacion borrar__.._
//        FirebaseMessaging.getInstance().subscribeToTopic("demo-topic2")

//        FirebaseInstanceId.getInstance().instanceId
//            .addOnSuccessListener(this, OnSuccessListener<InstanceIdResult> { instanceIdResult ->
//                    val mToken = instanceIdResult.token
//                    Log.e("Borrar", "Token dispositivo: "+mToken)
//                })


        var bAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        bAdapter?.let { _bAdapter ->

        }?: run {
            showSnackbar(bottomNavigation, resources.getString(R.string.snkBluetoothNotAvailable), ColorIdResource.PRIMARY, ColorIdResource.WHITE)
        }

        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        if(!mPresenter.validatePressingDifferent(bottomNavigation, menuItem.itemId)){
            //Bottom click different
            when (menuItem.itemId) {
                R.id.navigationHappHome ->{
                    isShowRiskFragment = false
                    this.replaceFragment(HappHomeFragment.newInstance(), R.id.mainPager, "1")
                    hideSkeleton()
                    hideLoader()
                    mainPager.visibility = View.VISIBLE
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigationRisk -> {
                    isShowRiskFragment = true
                    showSkeleton()
                    showLoader()
                    healthCareStatusCopy?.let {
                        loadFragmentMyRisk(it, false)
                    }
                    mPresenter.getAccessToken(false)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigationNotification ->{
                    isShowRiskFragment = false
                    this.replaceFragment(NotificationsFragment.newInstance(), R.id.mainPager, "1")
                    hideSkeleton()
                    hideLoader()
                    mainPager.visibility = View.VISIBLE
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigationProfile -> {
                    isShowRiskFragment = false
                    this.replaceFragment(ProfileFragment.newInstance(), R.id.mainPager, "1")
                    hideSkeleton()
                    hideLoader()
                    mainPager.visibility = View.VISIBLE
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }else{
            //Bottom click same
            false
        }
    }

    override fun loadFragmentHappHome() {
        this.replaceFragment(HappHomeFragment.newInstance(), R.id.mainPager, "1")
    }

    override fun loadFragmentMyRisk(healthCareStatus: TriageAnswerResponse, isButtonEnabled: Boolean) {
        healthCareStatusCopy = healthCareStatus

        if(isShowRiskFragment){
            when(healthCareStatus.triageStatus){
                TriageStatus.WITHOUT_TRIAGE -> this.replaceFragment(MyRiskWithoutTriage.newInstance(), R.id.mainPager, "0")
                TriageStatus.TRIAGE_NOT_STARTED -> this.replaceFragment(MyRiskAnswerFragment.newInstance(isButtonEnabled), R.id.mainPager, "0")
                TriageStatus.TRIAGE_PENDING -> this.replaceFragment(MyRiskPendingFragment.newInstance(isButtonEnabled), R.id.mainPager, "0")
                TriageStatus.TRIAGE_COMPLETED -> {
                    healthCareStatus.risk?.level?.let {level ->
                        if( level == SemaphoreTriage.RISK_HIGH.name)
                            this.replaceFragment(MyRiskValueHighFragment.newInstance(), R.id.mainPager, "0")
                        else
                            this.replaceFragment(MyRiskValueFragment.newInstance(), R.id.mainPager, "0")
                    }?: run{
                        this.replaceFragment(MyRiskWithoutTriage.newInstance(), R.id.mainPager, "0")
                        Sentry.capture(resources.getString(R.string.errSentryMyRiskLevelNull))
                    }
                }
                else -> {
                    this.replaceFragment(MyRiskWithoutTriage.newInstance(), R.id.mainPager, "0")
                    Sentry.capture(String.format(resources.getString(R.string.errSentryUnknownStatus), healthCareStatus.triageStatus))
                }
            }
            Handler(Looper.getMainLooper()).postDelayed({
                mainPager.visibility = View.VISIBLE
                hideSkeleton()
                hideLoader()
            }, 1000)
        }
    }

    override fun loadFragmentMyRiskFailure(errorCode: Int) {
        healthCareStatusCopy = TriageAnswerResponse(TriageStatus.WITHOUT_TRIAGE)
        if(isShowRiskFragment){
            this.replaceFragment(MyRiskWithoutTriage.newInstance(), R.id.mainPager, "0")
            Sentry.capture(String.format(resources.getString(R.string.errSentryLoadApiFailedMyRisk), errorCode))
            hideSkeleton()
            hideLoader()
            mainPager.visibility = View.VISIBLE
        }
    }

    override fun showTriageAnswerError(messageError: String) {
        showSnackbar(mainPager, messageError, ColorIdResource.PRIMARY, ColorIdResource.WHITE)
    }

    override fun showSkeleton() {
        mainPager.visibility = View.GONE
        shimmerMyRisk.startShimmer()
        shimmerMyRisk.visibility = View.VISIBLE
    }

    override fun hideSkeleton() {
        shimmerMyRisk.stopShimmer()
        shimmerMyRisk.visibility = View.GONE
    }

    override fun showLoader() {
        pbMainCircle.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        pbMainCircle.visibility = View.GONE
    }

    override fun onBackPressed() {
        if(bottomNavigation.menu.getItem(0).isChecked){
            finish()
        }else{
//            isShowRiskFragment = true
            bottomNavigation.menu.getItem(0).isChecked = true
//            healthCareStatusCopy?.let {
//                loadFragmentMyRisk(it, false)
//            }
//            mainPager.visibility = View.GONE
            mPresenter.loadFragmentHappHome()
        }


//        if (supportFragmentManager.backStackEntryCount > 1){
//            val backEntry: FragmentManager.BackStackEntry = supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 2)
//            val stackId: Int? = backEntry.name?.toInt()
//            stackId?.let { bottomNavigation.menu.getItem(stackId).isChecked = true }
//            supportFragmentManager.popBackStack()
//        }
//        else
//            finish()
    }


    override fun onPause() {
        super.onPause()
        isShowRiskFragment = false
    }

    override fun onRestart() {
        super.onRestart()
        if(bottomNavigation.menu.findItem(R.id.navigationRisk).isChecked ){
            isShowRiskFragment = true
            mainPager.visibility = View.GONE
            mPresenter.getAccessToken()
        }
    }

}
