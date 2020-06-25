package com.jumpitt.happ.ui.registerStepTwo

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.jumpitt.happ.R
import com.jumpitt.happ.network.request.RegisterRequest
import com.jumpitt.happ.ui.ToolbarActivity
import com.jumpitt.happ.utils.*
import kotlinx.android.synthetic.main.register_step_two.*

class RegisterStepTwo: ToolbarActivity(), RegisterStepTwoContract.View{
    private lateinit var mPresenter: RegisterStepTwoContract.Presenter
    private var aValidateInputsLogin = booleanArrayOf(false, false, false, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_step_two)

        toolbarToLoad(toolbar as Toolbar, resources.getString(R.string.tbCreateAccount))
        enableHomeDisplay(true)

        mPresenter = RegisterStepTwoPresenter(this)
        mPresenter.initializeView()

        etNameRegister.validateFocus {
            aValidateInputsLogin[0] = !it.isEmpty()
            btnNextRegisterTwo.validateInputs(aValidateInputsLogin)
        }

        etSurnameRegister.validateFocus {
            aValidateInputsLogin[1] = !it.isEmpty()
            btnNextRegisterTwo.validateInputs(aValidateInputsLogin)
        }

        etMailRegister.validateFocusEnd {
            if(it.isMailValid()) {
                itMailRegister.isErrorEnabled = false
                aValidateInputsLogin[2] = true
            }
            else{
                itMailRegister.error = getString(R.string.itMailError)
                aValidateInputsLogin[2] = true
            }
            btnNextRegisterTwo.validateInputs(aValidateInputsLogin)
        }

        etPasswordRegister.validateFocus {
            aValidateInputsLogin[3] = !it.isEmpty()
            btnNextRegisterTwo.validateInputs(aValidateInputsLogin)
        }

        btnNextRegisterTwo.setOnClickListener {
            val name = etNameRegister.text.toString()
            val surname = etSurnameRegister.text.toString()
            val mail = etMailRegister.text.toString()
            val password = etPasswordRegister.text.toString()
            val registerDataObject = RegisterRequest(names= name, lastName = surname, email = mail, password = password)
            mPresenter.navigateRegisterLivingPlace(registerDataObject)
        }

        containerContentStepTwo.setOnClickListener {containerView ->
            containerView.hideKeyboard()
        }
    }

    override fun showInitializeView() {
        tvDataPerson.containedStyle(Labelstext.H4, ColorIdResource.BLACK, font = R.font.dmsans_medium)
        btnNextRegisterTwo.containedStyle(ColorIdResource.BLUE, ColorIdResource.WHITE)
        btnNextRegisterTwo.disabled()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.transitionActivity(Transition.RIGHT_TO_LEFT)
    }
}