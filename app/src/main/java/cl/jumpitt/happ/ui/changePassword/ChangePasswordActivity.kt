package cl.jumpitt.happ.ui.changePassword

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import cl.jumpitt.happ.R
import cl.jumpitt.happ.ui.ToolbarActivity
import cl.jumpitt.happ.utils.*
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
            validateInputsChangePass()
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

            validateInputsChangePass()
        }

        etRepeatNewPassChange.validateFocus{repeatNewPass ->
            if(repeatNewPass.comparePassword(etNewPassChange.text.toString()) && repeatNewPass.isNotBlank()){
                aValidateInputPassword[2] = true
                aValidateInputPassword[1] = true
            }else{
                aValidateInputPassword[2] = false
            }
            validateInputsChangePass()
        }

    }

    override fun showInitializeView() {
        tvChangePasswordTitle.containedStyle(Labelstext.H4, ColorIdResource.BLACK, font = R.font.dmsans_medium)
        btnChangePassword.containedStyle(ColorIdResource.BLUE, ColorIdResource.WHITE)
        btnChangePassword.disabled()
    }

    private fun validateInputsChangePass() {
        btnChangePassword.isEnabled = !aValidateInputPassword.contains(false)
        if(btnChangePassword.isEnabled)
            btnChangePassword.alpha = 1F
        else
            btnChangePassword.alpha = 0.5F
    }
}