package com.jumpitt.happ.ui.triage.result

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.jumpitt.happ.R
import com.jumpitt.happ.network.response.TriageAnswerResponse
import com.jumpitt.happ.utils.*
import com.google.android.material.button.MaterialButton
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.fragment_triage_result.*

class ResultFragment : Fragment() {

    companion object {
        fun newInstance(value: Int): ResultFragment {
            val fragment = ResultFragment()
            val args = Bundle()
            args.putInt(LEVEL_ARG, value)
            fragment.arguments = args
            return fragment
        }
        const val LEVEL_ARG = "LEVEL_ARG"
        const val NO_RISK = 1
        const val LOW_RISK = 2
        const val MEDIUM_RISK = 3
        const val HIGH_RISK = 4
        const val CONFIRMED = 5
    }

    interface Delegate {
        fun onResultButtonPressed()
    }

    /**
     *------------ BEGIN ------------
     */
    private lateinit var mNextButton: MaterialButton
    private var mDelegate: Delegate? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mDelegate = activity as Delegate
        } catch (e: Exception) {
            Log.e("QuestionFragment", "Cannot attach delegate", e)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_triage_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var colorID = R.color.iconDisabled
        val triageReturnValue = Hawk.get<TriageAnswerResponse>("triageReturnValue")

        tvTriageResultTitle.containedStyle(Labelstext.H6, ColorIdResource.BLACK, font = R.font.dmsans_medium)
        tvTriageResultValue.containedStyle(Labelstext.H1, ColorIdResource.BLACK)
        tvTriageResultSubtitle.containedStyle(Labelstext.H4, ColorIdResource.BLACK)
        tvTriageResultDescription.containedStyle(Labelstext.BODY1, ColorIdResource.BLACK)
        btnFinishTriageResult.containedStyle(ColorIdResource.BLUE, ColorIdResource.WHITE)

        triageReturnValue.risk?.level?.let { level ->
            when(level){
                SemaphoreTriage.RISK_LOW.level -> {colorID =  SemaphoreTriage.RISK_LOW.colorID}
                SemaphoreTriage.RISK_MEDIUM.level -> {colorID =  SemaphoreTriage.RISK_MEDIUM.colorID}
                SemaphoreTriage.RISK_HIGH.level -> {colorID =  SemaphoreTriage.RISK_HIGH.colorID}
            }
        }

        tvTriageResultValue.setTextColor(ResourcesCompat.getColor(resources, colorID, null))
        cvLevelTriageResult.setCardBackgroundColor(ResourcesCompat.getColor(resources, colorID, null))
        cvLevelTriageResult.background.alpha = 20
        triageReturnValue.score?.let { score ->  tvTriageResultValue.text = score.toString() }
        triageReturnValue.risk?.description?.let { description -> tvTriageResultSubtitle.text = description}
        triageReturnValue.risk?.title?.let {title ->
            val htmlText = HtmlCompat.fromHtml(title.asteriskBold(), HtmlCompat.FROM_HTML_MODE_LEGACY)
            tvTriageResultDescription.text = htmlText }


        mNextButton = view.findViewById(R.id.btnFinishTriageResult)
        mNextButton.setSafeOnClickListener {
            mDelegate?.onResultButtonPressed()
        }
    }




}