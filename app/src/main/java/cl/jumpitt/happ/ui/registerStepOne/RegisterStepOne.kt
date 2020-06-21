package cl.jumpitt.happ.ui.registerStepOne

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import cl.jumpitt.happ.R
import cl.jumpitt.happ.network.request.ValidateDNIRequest
import cl.jumpitt.happ.ui.ToolbarActivity
import cl.jumpitt.happ.utils.*
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.register_step_one.*
import kotlinx.android.synthetic.main.register_step_one.toolbar
import kotlinx.android.synthetic.main.register_step_two.*

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

        etRutRegister.setOnFocusChangeListener { view, b ->
            if (view.isFocused) {
                etRutRegister.setText(etRutRegister.text.toString().removeRutFormat())
            }else{
                if(etRutRegister.text.toString().isCheckDigitRut() || etRutRegister.text.toString().isEmpty()){

                    etRutRegister.setText(etRutRegister.text.toString().rutFormat())

                    itRutRegister.isErrorEnabled = false
                    aValidateInputsLogin[0] = etRutRegister.text.toString().isNotEmpty()
                }
                else {
                    itRutRegister.error = getString(R.string.itRutError)
                    aValidateInputsLogin[0] = false
                }

                btnNextRegisterOne.validateInputs(aValidateInputsLogin)
            }
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
    }

    override fun showInitializeView() {
        tvValidateRut.containedStyle(Labelstext.H4, ColorIdResource.BLACK, font = R.font.dmsans_medium)
        btnNextRegisterOne.containedStyle(ColorIdResource.BLUE, ColorIdResource.WHITE)
        btnNextRegisterOne.disabled()

    }

    override fun showValidateDNIError(messageError: String) {
        showSnackbar(containerStepOne, messageError, ColorIdResource.BLUE, ColorIdResource.WHITE)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.transitionActivity(Transition.RIGHT_TO_LEFT)
    }
}