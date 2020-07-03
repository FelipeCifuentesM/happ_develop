package com.jumpitt.happ.ui.registerSuccess

import android.content.Intent
import android.os.Bundle
import com.jumpitt.happ.R
import com.jumpitt.happ.ui.main.MainActivity
import com.jumpitt.happ.ui.ToolbarActivity
import com.jumpitt.happ.utils.*
import kotlinx.android.synthetic.main.register_success.*

class RegisterSuccess: ToolbarActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_success)

        tvApprovedRegister.containedStyle(Labelstext.H4, ColorIdResource.BLACK, font = R.font.dmsans_medium)
        btnRegisterSuccess.containedStyle(ColorIdResource.BLUE, ColorIdResource.WHITE)


        btnRegisterSuccess.setSafeOnClickListener {
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