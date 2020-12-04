package com.jumpitt.happ.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.jumpitt.happ.BuildConfig.URL_PRIVACY_POLICIES_DOC
import com.jumpitt.happ.R
import com.jumpitt.happ.model.OnBoardSlide
import com.jumpitt.happ.ui.login.Login
import com.jumpitt.happ.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.on_board.*


class OnBoard: AppCompatActivity(){

    private lateinit var introOnBoardAdapter: OnBoardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.on_board)

        introOnBoardAdapter = OnBoardAdapter(
            listOf(
                OnBoardSlide(
                    resources.getString(R.string.lbOnBoardTitle1),
                    resources.getString(R.string.lbOnBoardDescription1),
                    R.drawable.ic_mother_and_son
                )
//                OnBoardSlide(
//                    resources.getString(R.string.lbOnBoardTitle1),
//                    resources.getString(R.string.lbOnBoardDescription1),
//                    R.drawable.ic_buckler_heart
//                ),
//                OnBoardSlide(
//                    resources.getString(R.string.lbOnBoardTitle1),
//                    resources.getString(R.string.lbOnBoardDescription1),
//                    R.drawable.ic_family_group
//                )
            )
        )

        tvTermsConditions.containedStyle(Labelstext.BODY2, ColorIdResource.BLACK, font = R.font.dmsans_medium)
        btnStartOB.containedStyle(ColorIdResource.PRIMARY, ColorIdResource.WHITE)


        val termsConditionLink = resources.getString(R.string.lbTermsConditions)
        val privacyLink = resources.getString(R.string.lbPrivacy)
        val phrase = String.format(resources.getString(R.string.lbPhraseTermsConditions),termsConditionLink, privacyLink)

        val firstCharacterTerms = phrase.indexOf(termsConditionLink)
        val firstCharacterPrivacy = phrase.indexOf(privacyLink)


        val ss = SpannableString(phrase)
//        val clickSpanTerms = object : ClickableSpan(){
//            override fun onClick(widget: View) {
//                Log.e("Borrar", "uno")
//            }
//
//            override fun updateDrawState(ds: TextPaint) {
//                super.updateDrawState(ds)
//                ds.color = ColorIdResource.TEXTCOLORLINK.color
//                ds.isUnderlineText = false
//            }
//        }

        val clickSpanPrivacy = object : ClickableSpan(){
            override fun onClick(widget: View) {
                goToActivity<WebViewActivity>{
                    putExtra("urlWebView", URL_PRIVACY_POLICIES_DOC)
                    putExtra("titleBar", resources.getString(R.string.tbPrivacy))
                }
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ColorIdResource.TEXTCOLORLINK.color
                ds.isUnderlineText = false
            }
        }
        tvTermsConditions.highlightColor = Color.LTGRAY
//        ss.setSpan(clickSpanTerms,firstCharacterTerms, firstCharacterTerms + termsConditionLink.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(clickSpanPrivacy,firstCharacterPrivacy, firstCharacterPrivacy + privacyLink.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvTermsConditions.text = ss
        tvTermsConditions.movementMethod = LinkMovementMethod.getInstance()


        vpIntroOnBoard.adapter = introOnBoardAdapter
//        setupIndicators()
//        setCurrentIndicator(0)
        vpIntroOnBoard.registerOnPageChangeCallback(object:
            ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position )
            }
        })

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            showSnackbar(containerOnBoard, resources.getString(R.string.snkMinimumVersion), ColorIdResource.PRIMARY, ColorIdResource.WHITE)
            btnStartOB.disabled()
        }

        btnStartOB.setSafeOnClickListener {
            this.goToActivity<Login>()
        }
    }

    private fun setupIndicators(){
        val indicators = arrayOfNulls<ImageView>(introOnBoardAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(24,0,24,0)
        for(i in indicators.indices){
            indicators[i] = ImageView(applicationContext)
            indicators[i].apply {
                this?.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.indicator_inactive))
                this?.layoutParams = layoutParams
            }
            indicatorContainer.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(index: Int){
        val childCount = indicatorContainer.childCount
        for (i in 0 until childCount){
            val imageView = indicatorContainer[i] as ImageView
            if(i == index){
                imageView.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.indicator_active))
            }else{
                imageView.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.indicator_inactive))

            }
        }
    }

}