package com.jumpitt.happ.ui.recoverPassword

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.jumpitt.happ.R
import com.jumpitt.happ.ui.ToolbarActivity
import com.jumpitt.happ.utils.*
import kotlinx.android.synthetic.main.activity_recover_password.*
import kotlinx.android.synthetic.main.activity_recover_password.toolbar

class RecoverPasswordActivity: ToolbarActivity(), RecoverPasswordContract.View {
    private lateinit var mPresenter: RecoverPasswordContract.Presenter
    private var aValidateInputRecoverPass = booleanArrayOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover_password)

        toolbarToLoad(toolbar as Toolbar, resources.getString(R.string.tbChangePass))
        enableHomeDisplay(true)

        mPresenter = RecoverPasswordPresenter(this)
        mPresenter.initializeView()

        etMailRecoverPass.validateFocus {
            aValidateInputRecoverPass[0] = it.isMailValid()
            btnRecoverPassword.validateInputs(aValidateInputRecoverPass)
        }

        btnRecoverPassword.setOnClickListener {
            mPresenter.postForgotPassword(etMailRecoverPass.text.toString())
        }

        containerContentRecoverPass.setOnClickListener {containerView ->
            containerView.hideKeyboard()
        }

    }

    override fun showInitializeView() {
        tvRecoverPasswordTitle.containedStyle(Labelstext.H4, ColorIdResource.BLACK, font = R.font.dmsans_medium)
        btnRecoverPassword.containedStyle(ColorIdResource.BLUE, ColorIdResource.WHITE)
        btnRecoverPassword.disabled()
    }

    override fun showRecoverPasswordResponse(message: String?) {
        message?.let { showSnackbar(containerRecoverPassword, message, ColorIdResource.BLUE, ColorIdResource.WHITE) }
    }

    override fun showLoader() {
        pbRecoverPassword.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        pbRecoverPassword.visibility = View.GONE
    }

}