package com.jumpitt.happ.utils

import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern


// Input text validate
fun TextInputEditText.validateFocusEnd(validation: (String) -> Unit){
    this.setOnFocusChangeListener { v, hasFocus ->
        if(!hasFocus){
            validation((v as TextInputEditText).text.toString())
        }
    }
}

// Input text validate
fun TextInputEditText.validateFocusBegin(validation: (String) -> Unit){
    this.setOnFocusChangeListener { v, hasFocus ->
        if(hasFocus){
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

//Compare Passwords
fun String.comparePassword(passwordOrigin: String): Boolean{
    val pattern = Pattern.compile(passwordOrigin, Pattern.CASE_INSENSITIVE)
    val matcher = pattern.matcher(this)

    if (!matcher.matches()) {
        return false
    }
    return true
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
    val rutAux = rut.substring(0, rut.length - 1)
    val dv = rut.get(rut.length-1)

    return "$rutAux-$dv"
}

fun String.rutFormat():String{

    if(this.isEmpty()){
        return this
    }
    val rut = this.rutFormatOnlyHyphen()

    if(rut.length > 2){
        val hyphen = rut.takeLast(2)
        val removeHypenRut = rut.substring(0, rut.length - 2)
        val removeHypenRutNumber = removeHypenRut.toInt()
        val formatter = NumberFormat.getInstance(Locale("es", "CL"))
        val formatedRut = formatter.format(removeHypenRutNumber)
        return formatedRut+hyphen
    }else{
        return  this
    }
}

fun String.removeRutFormat():String{

    if(this.isEmpty()){
        return this
    }
    val rut = this.rutFormatOnlyHyphen()
    return if(rut.length > 2){
        val hyphen = rut.takeLast(1)
        val cleanRut = rut.substring(0, rut.length - 2).replace(".", "")
        cleanRut + hyphen
    }else{
        this
    }
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

fun dateDifferenceSeconds(initialDate: Date, finalDate: Date): Long{
    val diffInMs: Long = finalDate.time - initialDate.time
    var seconds:Long = 0
    if(diffInMs>0){
        seconds = diffInMs / 1000
        Log.e("Borrar", "REALM segundos: "+seconds)
    }
    return seconds
}

fun dateTotalTimeMinute(initialDate: Date, finalDate: Date): Long{
    val diffInMs: Long = finalDate.time - initialDate.time
    var minutes:Long = 0
    if(diffInMs>0){
        minutes = (diffInMs / 1000) / 60
        Log.e("Borrar", "REALM minutos: "+minutes)
    }
    return minutes
}

fun dateDifferenceHMS(initialDate: Date, finalDate: Date): String{
    val diffInMs: Long = finalDate.time - initialDate.time
    val hoursString:String
    val minutesString:String
    val secondsString:String
    var timeHMS = "00:00:00"
    if(diffInMs>0){
        val hours: Long = TimeUnit.MILLISECONDS.toHours(diffInMs) % 24
        val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(diffInMs) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMs) % 60

        hoursString = if (hours<10) "0$hours" else "$hours"
        minutesString = if (hours<10) "0$minutes" else "$minutes"
        secondsString = if (hours<10) "0$seconds" else "$seconds"

        timeHMS = "$hoursString:$minutesString:$secondsString"
    }
    return timeHMS
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

