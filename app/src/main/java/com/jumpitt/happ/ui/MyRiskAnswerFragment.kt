package com.jumpitt.happ.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jumpitt.happ.R
import com.jumpitt.happ.ui.triage.TriageActivity
import com.jumpitt.happ.utils.*
import kotlinx.android.synthetic.main.fragment_item_answer_triage.*
import kotlinx.android.synthetic.main.fragment_item_myrisk_value.*

class MyRiskAnswerFragment : Fragment() {
    private var buttonEnabled: Boolean = false
    companion object {
        fun newInstance(isButtonEnabled: Boolean): MyRiskAnswerFragment = MyRiskAnswerFragment().apply {
            buttonEnabled = isButtonEnabled
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_myrisk_answer, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //toolbar
        containerMyRiskValueIcon.visibility = View.GONE
        tvMyRiskValueDescription.visibility = View.GONE
        pbMyRiskValue.visibility = View.GONE
        tvMyRiskValueTitle.containedStyle(Labelstext.H6, ColorIdResource.BLACK, font = R.font.dmsans_medium)


        //Card
        tvTitleTriageQuestion.containedStyle(Labelstext.H3, ColorIdResource.PRIMARY, font = R.font.dmsans_medium)
        tvDescriptionTriageQuestion.containedStyle(Labelstext.BODY1, ColorIdResource.BLACK)
        btnTriageQuestions.containedStyle(ColorIdResource.PRIMARY, ColorIdResource.WHITE)

        if(!buttonEnabled){
            btnTriageQuestions.disabled()
        }

        btnTriageQuestions.setSafeOnClickListener {
            activity?.goToActivity<TriageActivity>()
        }

    }

}