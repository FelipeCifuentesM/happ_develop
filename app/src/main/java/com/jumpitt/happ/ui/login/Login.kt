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
import kotlinx.android.synthetic.main.login.toolbar

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


        btnEnterLogin.setSafeOnClickListener {
            val loginRequest = LoginAccessTokenRequest(username= etRutLogin.text.toString().rutFormatOnlyHyphen(), password = etPasswordLoginMail.text.toString())
            mPresenter.postLoginAccessToken(loginRequest, true)
        }

        btnRecoverPassLogin.setSafeOnClickListener {
            mPresenter.navigateRecoverPass()
        }

        btnCreateAccount.setSafeOnClickListener {
            mPresenter.navigateRegisterStepOne()
        }


        etRutLogin.setOnFocusChangeListener { view, b ->
            if (view.isFocused) {
                etRutLogin.setText(etRutLogin.text.toString().removeRutFormat())
            } else {
                if (etRutLogin.text.toString().isCheckDigitRut() || etRutLogin.text.toString().isEmpty()) {

                    etRutLogin.setText(etRutLogin.text.toString().rutFormat())

                    itRutLogin.isErrorEnabled = false
                    aValidateInputsLogin[0] = etRutLogin.text.toString().isNotEmpty()
                } else {
                    itRutLogin.error = getString(R.string.itRutError)
                    aValidateInputsLogin[0] = false
                }

                btnEnterLogin.validateInputs(aValidateInputsLogin)

                containerContentLogin.setOnClickListener { containerView ->
                    containerView.hideKeyboard()
                }

                etPasswordLoginMail.validateFocus {
                    aValidateInputsLogin[1] = !it.isEmpty()
                    btnEnterLogin.validateInputs(aValidateInputsLogin)
                }

            }
        }

        etRutLogin.validateFocus {
            aValidateInputsLogin[0] = it.isCheckDigitRut()
            btnEnterLogin.validateInputs(aValidateInputsLogin)
        }

        containerContentLogin.setOnClickListener {containerView ->
            containerView.hideKeyboard()
        }

    }

    override fun showInitializeView() {
        cvLogin.setCardBackgroundColor(ResourcesCompat.getColor(resources, R.color.skyBlue, null))
        cvLogin.background.alpha = 80
        tvLoginTitle.containedStyle(Labelstext.H4, ColorIdResource.BLACK, font = R.font.dmsans_medium)
        tvCreateAccount.containedStyle(Labelstext.H5, ColorIdResource.BLACK, font = R.font.dmsans_bold)
        btnEnterLogin.containedStyle(ColorIdResource.PRIMARY, ColorIdResource.WHITE)
        btnRecoverPassLogin.containedStyle(ColorIdResource.WHITE, ColorIdResource.PRIMARY, font = R.font.dmsans_medium)
        btnCreateAccount.containedStyle(ColorIdResource.SKYBLUE, ColorIdResource.WHITE, font = R.font.dmsans_medium)
        btnEnterLogin.disabled()
    }

    override fun showValidateLoginError(messageError: String) {
        showSnackbar(containerLogin, messageError, ColorIdResource.PRIMARY, ColorIdResource.WHITE)
    }

    override fun showLoader() {
        btnEnterLogin.disabled()
        pbLoogin.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        btnEnterLogin.enabled()
        pbLoogin.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        btnEnterLogin.validateInputs(aValidateInputsLogin)
    }

    override fun onRestart() {
        mPresenter.deleteProfileData()
        super.onRestart()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.transitionActivity(Transition.RIGHT_TO_LEFT)
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
                val loginRequest = LoginAccessTokenRequest(username= etRutLogin.text.toString().rutFormatOnlyHyphen(), password = etPasswordLoginMail.text.toString())
                mPresenter.postLoginAccessToken(loginRequest, false)
                return
            }
            RequestCode.BATTERY_PERMISSION -> {
                Log.e("Borrar", "Bateria: $grantResults")
            }
            else -> {
                val loginRequest = LoginAccessTokenRequest(username= etRutLogin.text.toString().rutFormatOnlyHyphen(), password = etPasswordLoginMail.text.toString())
                mPresenter.postLoginAccessToken(loginRequest, false)
                Log.e("Borrar", "Ignoro todo")
                // Ignore all other requests.
            }
        }
    }
}