package cl.jumpitt.happ.ui.main

import android.bluetooth.BluetoothAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cl.jumpitt.happ.R
import cl.jumpitt.happ.network.response.TriageAnswerResponse
import cl.jumpitt.happ.ui.*
import cl.jumpitt.happ.ui.profile.ProfileFragment
import cl.jumpitt.happ.utils.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainActivityContract.View {
    private lateinit var mPresenter: MainActivityContract.Presenter
    private lateinit var healthCareStatusCopy: TriageAnswerResponse
    private var bAdapter: BluetoothAdapter? = null
    private val otherStrings = arrayOf("a", "b", "c")

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
                when(healthCareStatusCopy.triageStatus){
                    TriageStatus.TRIAGE_NOT_STARTED -> this.replaceFragment(MyRiskAnswerFragment.newInstance(), R.id.mainContainer)
                    TriageStatus.TRIAGE_PENDING -> this.replaceFragment(MyRiskPendingFragment.newInstance(), R.id.mainContainer)
                    TriageStatus.TRIAGE_COMPLETED -> {
                        healthCareStatusCopy.risk?.level?.let {level ->
                            if( level == SemaphoreTriage.RISK_HIGH.name)
                                this.replaceFragment(MyRiskValueHighFragment.newInstance(), R.id.mainContainer)
                            else
                                this.replaceFragment(MyRiskValueFragment.newInstance(), R.id.mainContainer)
                        }

                    }
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigationProfile -> {
                this.replaceFragment(ProfileFragment.newInstance(), R.id.mainContainer)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun loadFragmentMyRisk(healthCareStatus: TriageAnswerResponse) {
        healthCareStatusCopy = healthCareStatus
        when(healthCareStatus.triageStatus){
            TriageStatus.TRIAGE_NOT_STARTED -> this.replaceFragment(MyRiskAnswerFragment.newInstance(), R.id.mainContainer)
            TriageStatus.TRIAGE_PENDING -> this.replaceFragment(MyRiskPendingFragment.newInstance(), R.id.mainContainer)
            TriageStatus.TRIAGE_COMPLETED -> {
                healthCareStatus.risk?.level?.let {level ->
                    if( level == SemaphoreTriage.RISK_HIGH.name)
                        this.replaceFragment(MyRiskValueHighFragment.newInstance(), R.id.mainContainer)
                    else
                        this.replaceFragment(MyRiskValueFragment.newInstance(), R.id.mainContainer)
                }

            }
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1)
            supportFragmentManager.popBackStack()
        else
            finish()
    }

}
