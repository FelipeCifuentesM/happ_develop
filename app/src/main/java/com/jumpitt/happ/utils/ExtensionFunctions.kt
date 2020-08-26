package com.jumpitt.happ.utils

import android.app.Activity
import android.content.Intent
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.jumpitt.happ.R
import com.jumpitt.happ.context
import com.jumpitt.happ.network.response.ErrorResponse
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import retrofit2.Response

import java.lang.Exception


//Style Labels
fun TextView.containedStyle(labelstext: Labelstext, colorText: ColorIdResource, opacityPercentage: Float = 1F, font: Int = labelstext.font){
    this.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(labelstext.sizeText))
    this.typeface = ResourcesCompat.getFont(context, font)
    this.setTextColor(context.resources.getColorStateList(colorText.color,null))
    this.alpha = opacityPercentage
}

// Style Buttons
fun MaterialButton.containedStyle(colorBackground: ColorIdResource, colorText: ColorIdResource, opacityPercentage: Int = 100, font: Int = R.font.dmsans_bold){
    this.backgroundTintList = context.resources.getColorStateList(colorBackground.color, null).withAlpha((255*opacityPercentage)/100)
//  this.setBackgroundColor(colorBackground.color)
    this.setTextColor(context.resources.getColorStateList(colorText.color,null))
    this.setRippleColorResource(colorText.color)
    this.typeface = ResourcesCompat.getFont(context, font)
    this.isAllCaps = false
    this.cornerRadius = resources.getDimension(R.dimen.btnCornerRadius).toInt()
    this.letterSpacing = 0F
    this.setTextSize(TypedValue.COMPLEX_UNIT_PX,resources.getDimension(R.dimen.btnTextSizeMedium))
//    this.height = resources.getDimensionPixelSize(R.dimen.btnHeight)
}

//Button disabled
fun MaterialButton.disabled(){
    this.isEnabled = false
    this.alpha = 0.5F
}

//Button enabled
fun MaterialButton.enabled(){
    this.isEnabled = true
    this.alpha = 1F
}

//Go to second activity
inline fun <reified T: Activity> Activity.goToActivity(transition: String = Transition.LEFT_TO_RIGHT, finish: Boolean = false, noinline init: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    intent.init()
    startActivity(intent)
    this.transitionActivity(transition)
    if (finish)
        finishAffinity()
}

inline fun <reified T: Activity> Activity.goToActivityForResult(transition: String = Transition.LEFT_TO_RIGHT, requestCode: Int, noinline init: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    startActivityForResult(intent, requestCode)
    this.transitionActivity(transition)

}

fun Activity.transitionActivity(transition: String){
    when(transition){
        Transition.LEFT_TO_RIGHT -> overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        Transition.RIGHT_TO_LEFT -> overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
    }
}

//Style Snackbar
fun Activity.showSnackbar(view: View, message: String, colorSnackbarBackground: ColorIdResource, colorSnackbarText: ColorIdResource) {
    val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
    val snackbarView = snackbar.view
    snackbarView.background = context.getDrawable(R.drawable.snackbar_container)
//    snackbarView.setBackgroundColor(ResourcesCompat.getColor(resources,colorSnackbarBackground.color,null))
    val textView = snackbarView.findViewById(R.id.snackbar_text) as TextView
    textView.setTextColor(ResourcesCompat.getColor(resources, colorSnackbarText.color, null))
    //textView.textSize = 12f
    textView.typeface = ResourcesCompat.getFont(this.applicationContext, R.font.dmsans_regular)
    textView.isAllCaps = false
    snackbar.show()
}

//
inline fun <reified T> Response<*>.parseErrJsonResponse(): ErrorResponse {
    try {
        var mJson: JsonElement? = null
        mJson = JsonParser.parseString(this.errorBody()!!.string())
        val gson = Gson()
        val errorResponse: ErrorResponse = gson.fromJson(mJson, ErrorResponse::class.java)
        return errorResponse
    } catch (e: Exception) {
        return ErrorResponse()
    }
}

fun Response<*>.qualifyResponseErrorDefault(errorCode: Int, activity: Activity): String{
    when(errorCode){
        200 -> {
            return activity.resources.getString(R.string.snkDataNullError)
        }
        422 -> {
            val errorResponse = this.parseErrJsonResponse<ErrorResponse>()
            errorResponse.errorMessage?.let {messageError ->
                return messageError
            }?: run{
                return activity.resources.getString(R.string.snkDefaultApiError)
            }

        }
        else ->
            return activity.resources.getString(R.string.snkDefaultApiError)
    }
}

//Hide keyboard when pressing out of input
fun View.hideKeyboard() {
    val inputMethodManager = context.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    inputMethodManager?.hideSoftInputFromWindow(this.windowToken, 0)
}

//Fragments
//TODO: FRAGMENT TRANSACTION
inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit = {}) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}


fun FragmentActivity.replaceFragment(
    fragment: Fragment,
    frameId: Int,
    backStackTag: String? = null
): Boolean {
    supportFragmentManager.inTransaction {
        replace(frameId, fragment)
//        addToBackStack(backStackTag)
    }
    return true
}

fun AppCompatActivity.replaceFragmentQuestions(
    fragment: Fragment,
    frameId: Int,
    backStackTag: String? = null,
    transactionOptions: FragmentTransaction.() -> Unit = {}
) {
    supportFragmentManager.inTransaction {
        transactionOptions()
        backStackTag?.let { addToBackStack(it) }
        replace(frameId, fragment)
    }
}

fun FragmentTransaction.enterFromRight() =
    setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)