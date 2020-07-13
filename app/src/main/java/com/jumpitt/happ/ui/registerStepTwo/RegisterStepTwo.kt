package com.jumpitt.happ.ui.registerStepTwo

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.jumpitt.happ.R
import com.jumpitt.happ.network.request.RegisterRequest
import com.jumpitt.happ.ui.ToolbarActivity
import com.jumpitt.happ.utils.*
import kotlinx.android.synthetic.main.register_permissions.*
import kotlinx.android.synthetic.main.register_step_two.*
import kotlinx.android.synthetic.main.register_step_two.toolbar

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
            if(it.length>=3 || it.isEmpty()){
                itNameRegister.isErrorEnabled = false
            }
            aValidateInputsLogin[0] = it.length>=3
            btnNextRegisterTwo.validateInputs(aValidateInputsLogin)
        }

        etNameRegister.validateFocusEnd{
            if(it.length >= 3 || it.isEmpty()){
                itNameRegister.isErrorEnabled = false
            }else{
                itNameRegister.error = getString(R.string.itNameError)
            }
        }

        etSurnameRegister.validateFocus {
            if(it.length>=3 || it.isEmpty()){
                itSurnameRegister.isErrorEnabled = false
            }
            aValidateInputsLogin[1] = it.length>=3
            btnNextRegisterTwo.validateInputs(aValidateInputsLogin)
        }

        etSurnameRegister.validateFocusEnd{
            if(it.length >= 3 || it.isEmpty()){
                itSurnameRegister.isErrorEnabled = false
            }else{
                itSurnameRegister.error = getString(R.string.itSurNameError)
            }
        }

        etMailRegister.validateFocusEnd {
            if(it.isMailValid()) {
                itMailRegister.isErrorEnabled = false
                aValidateInputsLogin[2] = true
            }
            else{
                itMailRegister.error = getString(R.string.itMailError)
                aValidateInputsLogin[2] = false
            }
            btnNextRegisterTwo.validateInputs(aValidateInputsLogin)
        }

        etMailRegister.validateFocus {
            if(it.isMailValid()) {
                itMailRegister.isErrorEnabled = false
                aValidateInputsLogin[2] = true
            }
            else{
                aValidateInputsLogin[2] = false
            }
            btnNextRegisterTwo.validateInputs(aValidateInputsLogin)
        }

        etPasswordRegister.validateFocus {
            if(it.length > 5) {
                itPasswordRegister.isErrorEnabled = false
            }
            else{
                itPasswordRegister.error = getString(R.string.itPasswordError)
            }

            aValidateInputsLogin[3] = it.isNotEmpty() && it.length >= 6
            btnNextRegisterTwo.validateInputs(aValidateInputsLogin)
        }

        btnNextRegisterTwo.setSafeOnClickListener {
            btnNextRegisterTwo.disabled()
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

    override fun showRegisterError(messageError: String) {
        showSnackbar(containerRegisterPermission, messageError, ColorIdResource.PRIMARY, ColorIdResource.WHITE)
    }

    override fun showInitializeView() {
        tvDataPerson.containedStyle(Labelstext.H4, ColorIdResource.BLACK, font = R.font.dmsans_medium)
        btnNextRegisterTwo.containedStyle(ColorIdResource.PRIMARY, ColorIdResource.WHITE)
        btnNextRegisterTwo.disabled()
    }

    override fun enabledButton() {
        btnNextRegisterTwo.enabled()
    }

    override fun onResume() {
        super.onResume()
        btnNextRegisterTwo.validateInputs(aValidateInputsLogin)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.transitionActivity(Transition.RIGHT_TO_LEFT)
    }
}