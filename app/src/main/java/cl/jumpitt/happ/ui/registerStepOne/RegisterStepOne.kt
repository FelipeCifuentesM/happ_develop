package cl.jumpitt.happ.ui.registerStepOne

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import cl.jumpitt.happ.R
import cl.jumpitt.happ.network.request.ValidateDNIRequest
import cl.jumpitt.happ.ui.ToolbarActivity
import cl.jumpitt.happ.utils.*
import kotlinx.android.synthetic.main.register_step_one.*

class RegisterStepOne: ToolbarActivity(), RegisterStepOneContract.View{
    private lateinit var mPresenter: RegisterStepOneContract.Presenter
    private var aValidateInputsLogin = booleanArrayOf(false, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_step_one)

        toolbarToLoad(toolbar as Toolbar, resources.getString(R.string.tbCreateAccount))
        enableHomeDisplay(true)

        mPresenter = RegisterStepOnePresenter(this)
        mPresenter.initializeView()


        etRutRegister.validateFocusEnd {
            if(it.isCheckDigitRut()){
                itRutRegister.isErrorEnabled = false
                aValidateInputsLogin[0] = true
            }
            else {
                itRutRegister.error = getString(R.string.itRutError)
                aValidateInputsLogin[0] = false
            }
            btnNextRegisterOne.validateInputs(aValidateInputsLogin)
        }

        etDocumentNumberRegister.validateFocus {
            if(it.isEmpty()){
                aValidateInputsLogin[1] = false
                btnNextRegisterOne.validateInputs(aValidateInputsLogin)
            }else{
                aValidateInputsLogin[1] = true
                btnNextRegisterOne.validateInputs(aValidateInputsLogin)
            }

        }

        btnNextRegisterOne.setOnClickListener {
            val rut = etRutRegister.text.toString().rutFormatOnlyHyphen()
            val documentNumber = etDocumentNumberRegister.text.toString().deletePoints()
            val validateDNIRequest = ValidateDNIRequest(dni= rut, documentNumber= documentNumber)
            mPresenter.postValidateDNI(validateDNIRequest)
        }

        containerContentStepOne.setOnClickListener {containerView ->
            containerView.hideKeyboard()
        }
    }

    override fun showInitializeView() {
        tvValidateRut.containedStyle(Labelstext.H4, ColorIdResource.BLACK, font = R.font.dmsans_medium)
        btnNextRegisterOne.containedStyle(ColorIdResource.BLUE, ColorIdResource.WHITE)
        btnNextRegisterOne.disabled()

    }

    override fun showValidateDNIError(messageError: String) {
        showSnackbar(containerStepOne, messageError, ColorIdResource.BLUE, ColorIdResource.WHITE)
    }

    override fun showLoader() {
        pbRegisterStepOne.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        pbRegisterStepOne.visibility = View.GONE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.transitionActivity(Transition.RIGHT_TO_LEFT)
    }
}