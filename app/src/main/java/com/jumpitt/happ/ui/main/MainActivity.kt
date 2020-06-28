package com.jumpitt.happ.ui.main

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.jumpitt.happ.R
import com.jumpitt.happ.network.response.TriageAnswerResponse
import com.jumpitt.happ.ui.MyRiskAnswerFragment
import com.jumpitt.happ.ui.MyRiskPendingFragment
import com.jumpitt.happ.ui.MyRiskValueFragment
import com.jumpitt.happ.ui.MyRiskValueHighFragment
import com.jumpitt.happ.ui.profile.ProfileFragment
import com.jumpitt.happ.utils.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jumpitt.happ.ui.changePassword.ChangePasswordActivity
import com.jumpitt.happ.ui.triage.question.OptionsAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*

class MainActivity : AppCompatActivity(), MainActivityContract.View {
    private lateinit var mPresenter: MainActivityContract.Presenter
    private lateinit var healthCareStatusCopy: TriageAnswerResponse
    private var bAdapter: BluetoothAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mPresenter = MainActivityPresenter(this)
        mPresenter.getAccessToken()

        bAdapter = BluetoothAdapter.getDefaultAdapter()
        bAdapter?.let { _bAdapter ->

        }?: run {
            showSnackbar(bottomNavigation, resources.getString(R.string.snkBluetoothNotAvailable), ColorIdResource.BLUE, ColorIdResource.WHITE)
        }
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.navigationRisk -> {
                mPresenter.getAccessToken()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigationProfile -> {
                this.replaceFragment(ProfileFragment.newInstance(), R.id.mainPager, "1")
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun loadFragmentMyRisk(healthCareStatus: TriageAnswerResponse) {
        healthCareStatusCopy = healthCareStatus
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
    }

    override fun showTriageAnswerError(messageError: String) {
        showSnackbar(mainPager, messageError, ColorIdResource.BLUE, ColorIdResource.WHITE)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1){
            val backEntry: FragmentManager.BackStackEntry = supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 2)
            val stackId: Int? = backEntry.name?.toInt()
            stackId?.let { bottomNavigation.menu.getItem(stackId).isChecked = true }
            supportFragmentManager.popBackStack()
        }
        else
            finish()
    }

    override fun onRestart() {
        super.onRestart()
        if(bottomNavigation.menu.findItem(R.id.navigationRisk).isChecked ){
            mPresenter.getAccessToken()
        }
    }

}
