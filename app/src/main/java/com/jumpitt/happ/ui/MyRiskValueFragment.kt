package com.jumpitt.happ.ui

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.jumpitt.happ.R
import com.jumpitt.happ.network.response.TriageAnswerResponse
import com.jumpitt.happ.utils.*
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.jumpitt.happ.realm.TriageReturnValue
import com.orhanobut.hawk.Hawk
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_item_myrisk_pass.*
import kotlinx.android.synthetic.main.fragment_item_myrisk_value.*
import java.util.*


class MyRiskValueFragment : Fragment() {
    companion object {
        fun newInstance(): MyRiskValueFragment = MyRiskValueFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_myrisk_pass, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        val healthCareStatusLocal = Hawk.get<TriageAnswerResponse>("triageReturnValue")
        val realm = Realm.getDefaultInstance()
        val healthCareStatusLocal = realm.where(TriageReturnValue::class.java).findFirst()


        var colorID:ColorIdResource = ColorIdResource.BLACK
        var iconID = 0

        //toolbar
        healthCareStatusLocal?.riskTitle?.let { title ->
            val htmlText = HtmlCompat.fromHtml(title.asteriskBold(), HtmlCompat.FROM_HTML_MODE_LEGACY)
            tvMyRiskValueDescription.text = htmlText}
        healthCareStatusLocal?.score?.let { score ->  tvMyRiskValue.text = score.toString()}
        healthCareStatusLocal?.riskLevel?.let { level ->
            when(level){
                SemaphoreTriage.RISK_LOW.level -> {
                    colorID =  SemaphoreTriage.RISK_LOW.colorResource
                    iconID = R.drawable.ic_shield_check_duotone
                    cvCornerBottom.foreground = ResourcesCompat.getDrawable(resources, R.drawable.cardview_border_bottom_low, null)
                }
                SemaphoreTriage.RISK_MEDIUM.level -> {
                    colorID =  SemaphoreTriage.RISK_MEDIUM.colorResource
                    iconID = R.drawable.ic_exclamation_circle_duotone
                    cvCornerBottom.foreground = ResourcesCompat.getDrawable(resources, R.drawable.cardview_border_bottom_medium, null)
                }
                SemaphoreTriage.RISK_HIGH.level -> {
                    colorID =  SemaphoreTriage.RISK_HIGH.colorResource
                    iconID = R.drawable.ic_shield_virus_duotone
                    cvCornerBottom.foreground = ResourcesCompat.getDrawable(resources, R.drawable.cardview_border_bottom_high, null)
                }
            }
        }
        tvMyRiskValueTitle.containedStyle(Labelstext.H6, ColorIdResource.BLACK, font = R.font.dmsans_medium)
        tvMyRiskValueDescription.containedStyle(Labelstext.SUBTITLE1, ColorIdResource.BLACK)
        tvMyRiskValue.containedStyle(Labelstext.H2, colorID, font = R.font.dmsans_bold)
        ivMyRiskValue.setImageResource(iconID)
        ivMyRiskValue.setColorFilter(ContextCompat.getColor(ivMyRiskValue.context, colorID.color), PorterDuff.Mode.SRC_IN)
        healthCareStatusLocal?.score?.let { score ->  pbMyRiskValue.progress = score}
        pbMyRiskValue.progressTintList = context?.resources?.getColorStateList(colorID.color, null)

        //card risk value
        tvMyRiskPass.containedStyle(Labelstext.H6, ColorIdResource.BLACK, 0.87F, font = R.font.dmsans_medium)
        tvMyRiskPassTitle.containedStyle(Labelstext.H5, ColorIdResource.BLACK, 0.87F, font = R.font.dmsans_medium)
        tvMyRiskPassDescription.containedStyle(Labelstext.SUBTITLE1, ColorIdResource.BLACK, 0.87F)
        tvMyRiskDurationTitle.containedStyle(Labelstext.BODY2, ColorIdResource.BLACK, font = R.font.dmsans_medium)
        tvMyRiskPassTime.containedStyle(Labelstext.BUTTON, ColorIdResource.BLACK, font = R.font.dmsans_medium)

        healthCareStatusLocal?.riskDescription?.let { description ->  tvMyRiskPassTitle.text = description}
        healthCareStatusLocal?.riskMessage?.let { message ->  tvMyRiskPassDescription.text = message}
        healthCareStatusLocal?.passportTimeRemainingVerbose?.let { time ->  tvMyRiskPassTime.text = time}
        cvMyRiskPassTop.setCardBackgroundColor(ResourcesCompat.getColor(resources, colorID.color, null))

        healthCareStatusLocal?.passportValidationUrl?.let { qrValidationUrl -> qrCodeWriter(qrValidationUrl) }

    }


    private fun qrCodeWriter(qrValidationUrl: String){
        val qrCodeWriter = QRCodeWriter()
        var hints: MutableMap<EncodeHintType?, Any?>? = null


            hints = EnumMap(EncodeHintType::class.java)
            hints?.let {
                hints[EncodeHintType.MARGIN] = 2 } /* default = 4 */

        val bitMatrix = qrCodeWriter.encode(qrValidationUrl, BarcodeFormat.QR_CODE,283, 283, hints) // width x height

        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        ivMyRiskCodeQR.setImageBitmap(bitmap)
    }

}