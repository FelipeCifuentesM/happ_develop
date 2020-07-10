package com.jumpitt.happ.ui.main

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.FragmentManager
import com.jumpitt.happ.R
import com.jumpitt.happ.network.response.TriageAnswerResponse
import com.jumpitt.happ.ui.profile.ProfileFragment
import com.jumpitt.happ.utils.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jumpitt.happ.ui.*
import com.jumpitt.happ.ui.changePassword.ChangePasswordActivity
import com.jumpitt.happ.ui.triage.question.OptionsAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*

class MainActivity : AppCompatActivity(), MainActivityContract.View {
    private lateinit var mPresenter: MainActivityContract.Presenter
    private lateinit var healthCareStatusCopy: TriageAnswerResponse
    private var isShowRiskFragment = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.e("Borrar", "MAIIIN")
        mPresenter = MainActivityPresenter(this)
        mPresenter.getAccessToken()

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
                R.id.navigationRisk -> {
                    isShowRiskFragment = true
                    mainPager.visibility = View.GONE
                    mPresenter.getAccessToken()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigationProfile -> {
                    isShowRiskFragment = false
                    this.replaceFragment(ProfileFragment.newInstance(), R.id.mainPager, "1")
                    hideSkeleton()
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

    override fun loadFragmentMyRisk(healthCareStatus: TriageAnswerResponse) {
        healthCareStatusCopy = healthCareStatus

        if(isShowRiskFragment){
            when(healthCareStatus.triageStatus){
                TriageStatus.TRIAGE_NOT_STARTED -> this.replaceFragment(MyRiskAnswerFragment.newInstance(), R.id.mainPager, "0")
                TriageStatus.TRIAGE_PENDING -> this.replaceFragment(MyRiskPendingFragment.newInstance(), R.id.mainPager, "0")
                TriageStatus.TRIAGE_COMPLETED -> {
                    healthCareStatus.risk?.level?.let {level ->
                        if( level == SemaphoreTriage.RISK_HIGH.name)
                            this.replaceFragment(MyRiskValueHighFragment.newInstance(), R.id.mainPager, "0")
                        else
                            this.replaceFragment(MyRiskValueFragment.newInstance(), R.id.mainPager, "0")
                    }

                }
            }
            hideSkeleton()
            mainPager.visibility = View.VISIBLE
        }
    }

    override fun showTriageAnswerError(messageError: String) {
        showSnackbar(mainPager, messageError, ColorIdResource.PRIMARY, ColorIdResource.WHITE)
    }

    override fun showSkeleton() {
        shimmerMyRisk.startShimmer()
        shimmerMyRisk.visibility = View.VISIBLE
    }

    override fun hideSkeleton() {
        shimmerMyRisk.stopShimmer()
        shimmerMyRisk.visibility = View.GONE
    }

    override fun onBackPressed() {
        if(bottomNavigation.menu.getItem(0).isChecked){
            finish()
        }else{
            isShowRiskFragment = true
            bottomNavigation.menu.getItem(0).isChecked = true
            mainPager.visibility = View.GONE
            mPresenter.getAccessToken()
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


    override fun onRestart() {
        super.onRestart()
        if(bottomNavigation.menu.findItem(R.id.navigationRisk).isChecked ){
            mainPager.visibility = View.GONE
            mPresenter.getAccessToken()
        }
    }

}
