package com.jumpitt.happ.ui


import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.jumpitt.happ.R
import com.jumpitt.happ.network.response.TriageAnswerResponse
import com.jumpitt.happ.utils.*
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.fragment_item_myrisk_high.*
import kotlinx.android.synthetic.main.fragment_item_myrisk_value.*

class MyRiskValueHighFragment : Fragment() {
    companion object {
        fun newInstance(): MyRiskValueHighFragment = MyRiskValueHighFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_myrisk_high, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val healthCareStatusLocal = Hawk.get<TriageAnswerResponse>("triageReturnValue")
        var colorID:ColorIdResource = ColorIdResource.BLACK
        var iconID = 0

        //toolbar
        healthCareStatusLocal.risk?.title?.let { title ->
            val htmlText = HtmlCompat.fromHtml(title.asteriskBold(), HtmlCompat.FROM_HTML_MODE_LEGACY)
            tvMyRiskValueDescription.text = htmlText}
        healthCareStatusLocal.score?.let { score ->  tvMyRiskValue.text = score.toString()}
        healthCareStatusLocal.risk?.level?.let { level ->
            when(level){
                SemaphoreTriage.RISK_LOW.level -> {
                    colorID =  SemaphoreTriage.RISK_LOW.colorResource
                    iconID = R.drawable.ic_shield_check_duotone
                }
                SemaphoreTriage.RISK_MEDIUM.level -> {
                    colorID =  SemaphoreTriage.RISK_MEDIUM.colorResource
                    iconID = R.drawable.ic_exclamation_circle_duotone
                }
                SemaphoreTriage.RISK_HIGH.level -> {
                    colorID =  SemaphoreTriage.RISK_HIGH.colorResource
                    iconID = R.drawable.ic_shield_virus_duotone
                }
            }
        }
        tvMyRiskValueTitle.containedStyle(Labelstext.H6, ColorIdResource.BLACK, font = R.font.dmsans_medium)
        tvMyRiskValueDescription.containedStyle(Labelstext.SUBTITLE1, ColorIdResource.BLACK)
        tvMyRiskValue.containedStyle(Labelstext.H2, colorID, font = R.font.dmsans_bold)
        ivMyRiskValue.setImageResource(iconID)
        ivMyRiskValue.setColorFilter(ContextCompat.getColor(ivMyRiskValue.context, colorID.color), PorterDuff.Mode.SRC_IN)
        healthCareStatusLocal.score?.let { score ->  pbMyRiskValue.progress = score}
        pbMyRiskValue.progressTintList = context?.resources?.getColorStateList(colorID.color, null)


        //card high value
        tvMyRiskHighTitle.containedStyle(Labelstext.H4, ColorIdResource.BLACK, font = R.font.dmsans_medium)
        tvMyRiskHighDescription.containedStyle(Labelstext.SUBTITLE1, ColorIdResource.BLACK, font = R.font.dmsans_medium)
        healthCareStatusLocal.risk?.description?.let { description ->  tvMyRiskHighTitle.text = description}
        healthCareStatusLocal.risk?.message?.let { message ->  tvMyRiskHighDescription.text = message}
    }


}