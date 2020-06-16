package cl.jumpitt.happ.ui.login

import android.os.Bundle
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
            var loginRequest = LoginAccessTokenRequest(username= etRutLogin.text.toString().rutFormatOnlyHyphen(), password = etPasswordLoginMail.text.toString())
            mPresenter.postLoginAccessToken(loginRequest)
        }

        btnCreateAccount.setOnClickListener {
            mPresenter.navigateRegisterStepOne()
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
        btnCreateAccount.containedStyle(ColorIdResource.SKYBLUE, ColorIdResource.WHITE, font = R.font.dmsans_medium)
        btnEnterLogin.disabled()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.transitionActivity(Transition.RIGHT_TO_LEFT)
    }
}