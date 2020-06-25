package com.jumpitt.happ.ui.changePassword

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.jumpitt.happ.R
import com.jumpitt.happ.ui.ToolbarActivity
import com.jumpitt.happ.utils.*
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePasswordActivity : ToolbarActivity(), ChangePasswordContract.View{
    private lateinit var mPresenter: ChangePasswordContract.Presenter
    private var aValidateInputPassword = booleanArrayOf(false, false, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        toolbarToLoad(toolbar as Toolbar, resources.getString(R.string.tbChangePass))
        enableHomeDisplay(true)

        mPresenter = ChangePasswordPresenter(this)
        mPresenter.initializeView()

        etCurrentPassChange.validateFocus {currentPass ->
            if(currentPass.isNotEmpty()){
                aValidateInputPassword[0] = true
            }else{
                aValidateInputPassword[0] = false
            }
            btnChangePassword.validateInputs(aValidateInputPassword)
        }

        etNewPassChange.validateFocus {newPass ->
            if(newPass.isNotEmpty()){
                if(etRepeatNewPassChange.text.toString().isNotEmpty()){
                    if(newPass.comparePassword(etRepeatNewPassChange.text.toString()) && etNewPassChange.text.toString().isNotEmpty()){
                        aValidateInputPassword[1] = true
                        aValidateInputPassword[2] = true
                    }else{
                        aValidateInputPassword[1] = false
                    }
                }else{
                    aValidateInputPassword[1] = true
                }
            }else{
                aValidateInputPassword[1] = false
            }
            btnChangePassword.validateInputs(aValidateInputPassword)
        }

        etRepeatNewPassChange.validateFocus{repeatNewPass ->
            if(repeatNewPass.comparePassword(etNewPassChange.text.toString()) && repeatNewPass.isNotBlank()){
                aValidateInputPassword[2] = true
                aValidateInputPassword[1] = true
            }else{
                aValidateInputPassword[2] = false
            }
            btnChangePassword.validateInputs(aValidateInputPassword)
        }

        btnChangePassword.setOnClickListener {
            mPresenter.getAccessTokenProfile(etCurrentPassChange.text.toString(), etRepeatNewPassChange.text.toString())
        }

        containerContentChangePass.setOnClickListener {containerView ->
            containerView.hideKeyboard()
        }

    }

    override fun showInitializeView() {
        tvChangePasswordTitle.containedStyle(Labelstext.H4, ColorIdResource.BLACK, font = R.font.dmsans_medium)
        btnChangePassword.containedStyle(ColorIdResource.BLUE, ColorIdResource.WHITE)
        btnChangePassword.disabled()
    }

    override fun showChangePasswordResponse(messageError: String) {
        showSnackbar(containerChangePassword, messageError, ColorIdResource.BLUE, ColorIdResource.WHITE)
    }

    override fun showLoader() {
        pbChangePassword.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        pbChangePassword.visibility = View.GONE
    }
}