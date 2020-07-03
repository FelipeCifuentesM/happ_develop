package com.jumpitt.happ.ui.splash

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.jumpitt.happ.R
import com.jumpitt.happ.utils.RequestCode
import kotlinx.android.synthetic.main.splash_activity.*

class SplashActivity: AppCompatActivity(), SplashActivityContract.View{
    private lateinit var mPresenter: SplashActivityContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        mPresenter = SplashActivityPresenter(this)

        ivHappSplash.alpha = 0F
        ivHappSplash.animate().setDuration(1500).alpha(1F).withEndAction {
            mPresenter.getUserProfileData(true)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            RequestCode.LOCATION_BACKGROUND -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.e("Borrar", "acepto permiso")
                } else {
                    Log.e("Borrar", "NO acepto")
                }
                mPresenter.getUserProfileData(false)
                return
            }
            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                mPresenter.getUserProfileData(false)
                Log.e("Borrar", "Ignoro todo")
                // Ignore all other requests.
            }
        }
    }

}