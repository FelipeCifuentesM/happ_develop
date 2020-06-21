package cl.jumpitt.happ.ui.login

import android.os.Bundle
import android.view.View
import cl.jumpitt.happ.R
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import cl.jumpitt.happ.network.request.LoginAccessTokenRequest
import cl.jumpitt.happ.ui.ToolbarActivity
import cl.jumpitt.happ.utils.*
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
            val loginRequest = LoginAccessTokenRequest(
                username = etRutLogin.text.toString().rutFormatOnlyHyphen(),
                password = etPasswordLoginMail.text.toString()
            )
            mPresenter.postLoginAccessToken(loginRequest)
        }

        btnRecoverPassLogin.setOnClickListener {
            mPresenter.navigateRecoverPass()
        }

        btnCreateAccount.setOnClickListener {
            mPresenter.navigateRegisterStepOne()
        }


        etRutLogin.setOnFocusChangeListener { view, b ->
            if (view.isFocused) {
                etRutLogin.setText(etRutLogin.text.toString().removeRutFormat())
            } else {
                if (etRutLogin.text.toString().isCheckDigitRut() || etRutLogin.text.toString()
                        .isEmpty()
                ) {

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
}