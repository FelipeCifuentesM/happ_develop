package cl.jumpitt.happ.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.util.regex.Pattern
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes


// Input text validate
fun TextInputEditText.validateFocusEnd(validation: (String) -> Unit){
    this.setOnFocusChangeListener { v, hasFocus ->
        if(!hasFocus){
            validation((v as TextInputEditText).text.toString())
        }
    }
}

// Input text validate
fun TextInputEditText.validateFocus(validation: (String) -> Unit){
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            validation(editable.toString())
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

    })
}

// Enabled/disabled input text empty
fun MaterialButton.validateInputs(aValidate: BooleanArray){
    this.isEnabled = !aValidate.contains(false)
    if(this.isEnabled)
        this.alpha = 1F
    else
        this.alpha = 0.5F
}


//Input mail validate
fun String.isMailValid(): Boolean{
    return try {
        val REGEX_LETRAS = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$"
        val patron = Pattern.compile(REGEX_LETRAS)
        patron.matcher(this).matches()
    }catch (e: Exception){
        false
    }
}

//Input rut validate
fun String.isCheckDigitRut() : Boolean{
    try {
        var rut = this.toUpperCase()
        rut= rut.replace(".","")
        rut= rut.replace("-","")
        var rutAux = rut.substring(0, rut.length - 1).toInt()
        val dv = rut.get(rut.length-1)

        var m = 0
        var s = 1

        while(rutAux != 0){
            s = (s + rutAux % 10 * (9 - m++ % 6)) % 11
            rutAux /= 10
        }

        s = if(s != 0)  s + 47 else 75
        if(dv == s.toChar()) return true
    }catch (e : Exception){

    }
    return false
}

//Delete points between string
fun String.deletePoints(): String{
    return this.replace(".","")
}

//Rut Format 123456789-1
fun String.rutFormatOnlyHyphen(): String{
    var rut = this.toLowerCase()
    rut= rut.replace(".","")
    rut= rut.replace("-","")
    rut = rut.replace(" ","")
    var rutAux = rut.substring(0, rut.length - 1)
    val dv = rut.get(rut.length-1)

    return "$rutAux-$dv"
}

//Bold backend **
fun String.asteriskBold(): String{
    var formattedString = ""
    var position = 0
    val stringSplit = this.split("**").toTypedArray()
    if(stringSplit.isNotEmpty()){
        for(str in stringSplit){
            if ((position % 2 ) == 0)
                formattedString = "$formattedString$str"
            else
                formattedString = "$formattedString<b>${str}</b>"
            position++
        }
    }else{
        formattedString = this
    }
    return formattedString
}

//Triage Inicio
fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

class SafeClickListener(private var defaultInterval: Int = 1000, private val onSafeCLick: (View) -> Unit) : View.OnClickListener {
    private var lastTimeClicked: Long = 0
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeCLick(v)
    }
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}
//Triage Fin

