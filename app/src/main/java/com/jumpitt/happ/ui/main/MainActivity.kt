package com.jumpitt.happ.ui.main

import android.app.ActivityManager
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jumpitt.happ.R
import com.jumpitt.happ.network.response.TriageAnswerResponse
import com.jumpitt.happ.ping.EndlessService
import com.jumpitt.happ.ping.ServiceState
import com.jumpitt.happ.ping.getServiceState
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
    private var isMyRiskTabSelected = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mPresenter = MainActivityPresenter(this)
        intent.extras?.getBoolean("fromTriageActivityResult")?.let { isMyRiskTabSelected = it }?: run { isMyRiskTabSelected = false }
        mPresenter.loadFragment(isMyRiskTabSelected)
//        mPresenter.getAccessToken()

        //Notificacion borrar__.._
//        FirebaseMessaging.getInstance().subscribeToTopic("demo-topic2")

//        FirebaseInstanceId.getInstance().instanceId
//            .addOnSuccessListener(this, OnSuccessListener<InstanceIdResult> { instanceIdResult ->
//                    val mToken = instanceIdResult.token
//                    Log.e("Borrar", "Token dispositivo: "+mToken)
//                })


        actionOnService(Actions.START)

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
                TriageStatus.TRIAGE_PENDING -> {
                    healthCareStatus.resultType?.let {resultType ->
                        when(resultType){
                            TriageResultType.SCORE_SCREEN -> this.replaceFragment(MyRiskPendingFragment.newInstance(isButtonEnabled), R.id.mainPager, "0")
                            else -> this.replaceFragment(MyRiskAnswerFragment.newInstance(isButtonEnabled), R.id.mainPager, "0")
                        }
                    }?: run {
                        this.replaceFragment(MyRiskPendingFragment.newInstance(isButtonEnabled), R.id.mainPager, "0")
                    }
                }
                TriageStatus.TRIAGE_COMPLETED -> {
                    healthCareStatus.resultType?.let {resultType ->
                        when (resultType){
                            TriageResultType.TEXT_SCREEN -> this.replaceFragment(MyRiskAnswerCompletedText.newInstance(), R.id.mainPager, "0")
                            TriageResultType.SCORE_SCREEN -> {
                                healthCareStatus.risk?.level?.let {level ->
                                    if( level == SemaphoreTriage.RISK_HIGH.name)
                                        this.replaceFragment(MyRiskValueHighFragment.newInstance(), R.id.mainPager, "0")
                                    else
                                        this.replaceFragment(MyRiskValueFragment.newInstance(), R.id.mainPager, "0")
                                }?: run {
                                    this.replaceFragment(MyRiskWithoutTriage.newInstance(), R.id.mainPager, "0")
                                    Sentry.capture(resources.getString(R.string.errSentryMyRiskLevelNull))
                                }
                            }
                            else -> {
                                this.replaceFragment(MyRiskWithoutTriage.newInstance(), R.id.mainPager, "0")
                                Sentry.capture(String.format(resources.getString(R.string.errSentryUnknownStatusResultType), healthCareStatus.resultType))
                            }
                        }
                    }?: run {
                        this.replaceFragment(MyRiskWithoutTriage.newInstance(), R.id.mainPager, "0")
                        Sentry.capture(resources.getString(R.string.errSentryMyRiskResultTypeNull))
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

    override fun setNavigationTab() {
        if(isMyRiskTabSelected)
            bottomNavigation.menu.getItem(1).isChecked = true //My Risk
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
            mPresenter.loadFragment()
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


    private fun actionOnService(action: Actions) {
        if (getServiceState(this) == ServiceState.STOPPED && action == Actions.STOP && !isMyServiceRunning(EndlessService::class.java)) return
        Intent(this, EndlessService::class.java).also {
            it.action = action.name
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                log("Starting the service in >=26 Mode")
                startForegroundService(it)
                return
            }
            log("Starting the service in < 26 Mode")
            startService(it)
        }
    }

    fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
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
