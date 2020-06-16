package cl.jumpitt.happ.ui.registerSuccess

import android.content.Intent
import android.os.Bundle
import cl.jumpitt.happ.R
import cl.jumpitt.happ.ui.main.MainActivity
import cl.jumpitt.happ.ui.ToolbarActivity
import cl.jumpitt.happ.utils.ColorIdResource
import cl.jumpitt.happ.utils.Labelstext
import cl.jumpitt.happ.utils.containedStyle
import cl.jumpitt.happ.utils.goToActivity
import kotlinx.android.synthetic.main.register_success.*

class RegisterSuccess: ToolbarActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_success)

        tvApprovedRegister.containedStyle(Labelstext.H4, ColorIdResource.BLACK, font = R.font.dmsans_medium)
        btnRegisterSuccess.containedStyle(ColorIdResource.BLUE, ColorIdResource.WHITE)


        btnRegisterSuccess.setOnClickListener {
            this.goToActivity<MainActivity>{
                this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.goToActivity<MainActivity>("")
    }
}