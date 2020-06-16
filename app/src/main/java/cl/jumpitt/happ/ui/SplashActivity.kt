package cl.jumpitt.happ.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cl.jumpitt.happ.R
import cl.jumpitt.happ.network.response.RegisterResponse
import cl.jumpitt.happ.ui.main.MainActivity
import cl.jumpitt.happ.utils.goToActivity
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.splash_activity.*

class SplashActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        ivHappSplash.alpha = 0F
        ivHappSplash.animate().setDuration(1500).alpha(1F).withEndAction {

            val userData = Hawk.get<RegisterResponse>("userProfileData")
            if(userData!=null){
                this.goToActivity<MainActivity>("") {
                    this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            }else{
                this.goToActivity<OnBoard>("")
            }
            finish()
        }
    }
}