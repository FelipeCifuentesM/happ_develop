package com.jumpitt.happ.ui.login

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import com.jumpitt.happ.R
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import com.jumpitt.happ.network.request.LoginAccessTokenRequest
import com.jumpitt.happ.ui.ToolbarActivity
import com.jumpitt.happ.utils.*
import kotlinx.android.synthetic.main.login.*

class Login: ToolbarActivity(), LoginContract.View{
    private lateinit var mPresenter: LoginContract.Presenter
    private var aValidateInputsLogin = booleanArrayOf(false, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        toolbarToLoad(toolbar as Toolbar, resources.getString(R.string.tbLogIn))
        enableHomeDisplay(true)

        mPresenter = LoginPresenter(this)
        mPresenter.initializeView()


        btnEnterLogin.setOnClickListener {
            val loginRequest = LoginAccessTokenRequest(username= etRutLogin.text.toString().rutFormatOnlyHyphen(), password = etPasswordLoginMail.text.toString())
            mPresenter.postLoginAccessToken(loginRequest, true)
        }

        btnRecoverPassLogin.setOnClickListener {
            mPresenter.navigateRecoverPass()
        }

        btnCreateAccount.setOnClickListener {
            mPresenter.navigateRegisterStepOne()
        }

        containerContentLogin.setOnClickListener {containerView ->
            containerView.hideKeyboard()
        }

        etRutLogin.validateFocusEnd {
            if(it.isCheckDigitRut()){
                itRutLogin.isErrorEnabled = false
                aValidateInputsLogin[0] = true
            }
            else {
                itRutLogin.error = getString(R.string.itRutError)
                aValidateInputsLogin[0] = false
            }
            btnEnterLogin.validateInputs(aValidateInputsLogin)
        }

        etPasswordLoginMail.validateFocus {
            aValidateInputsLogin[1] = !it.isEmpty()
            btnEnterLogin.validateInputs(aValidateInputsLogin)
        }

    }

    override fun showInitializeView() {
        cvLogin.setCardBackgroundColor(ResourcesCompat.getColor(resources, R.color.skyBlue, null))
        cvLogin.background.alpha = 80
        tvLoginTitle.containedStyle(Labelstext.H4, ColorIdResource.BLACK, font = R.font.dmsans_medium)
        tvCreateAccount.containedStyle(Labelstext.H5, ColorIdResource.BLACK, font = R.font.dmsans_bold)
        btnEnterLogin.containedStyle(ColorIdResource.BLUE, ColorIdResource.WHITE)
        btnRecoverPassLogin.containedStyle(ColorIdResource.WHITE, ColorIdResource.BLUE, font = R.font.dmsans_medium)
        btnCreateAccount.containedStyle(ColorIdResource.SKYBLUE, ColorIdResource.WHITE, font = R.font.dmsans_medium)
        btnEnterLogin.disabled()
    }

    override fun showValidateLoginError(messageError: String) {
        showSnackbar(containerLogin, messageError, ColorIdResource.BLUE, ColorIdResource.WHITE)
    }

    override fun showLoader() {
        pbLoogin.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        pbLoogin.visibility = View.GONE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.transitionActivity(Transition.RIGHT_TO_LEFT)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            RequestCode.ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.e("Borrar", "acepto permiso")
                } else {
                    Log.e("Borrar", "NO acepto")
                }
                val loginRequest = LoginAccessTokenRequest(username= etRutLogin.text.toString().rutFormatOnlyHyphen(), password = etPasswordLoginMail.text.toString())
                mPresenter.postLoginAccessToken(loginRequest, false)
                return
            }
            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                val loginRequest = LoginAccessTokenRequest(username= etRutLogin.text.toString().rutFormatOnlyHyphen(), password = etPasswordLoginMail.text.toString())
                mPresenter.postLoginAccessToken(loginRequest, false)
                Log.e("Borrar", "Ignoro todo")
                // Ignore all other requests.
            }
        }
    }
}