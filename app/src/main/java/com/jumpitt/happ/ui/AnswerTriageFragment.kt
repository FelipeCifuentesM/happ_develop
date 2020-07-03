package com.jumpitt.happ.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jumpitt.happ.R
import com.jumpitt.happ.ui.triage.TriageActivity
import com.jumpitt.happ.utils.ColorIdResource
import com.jumpitt.happ.utils.Labelstext
import com.jumpitt.happ.utils.containedStyle
import com.jumpitt.happ.utils.goToActivity
import kotlinx.android.synthetic.main.fragment_answer_triage.*

class AnswerTriageFragment : Fragment() {
    companion object {
        fun newInstance(): AnswerTriageFragment = AnswerTriageFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_answer_triage, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tvTitleTriageQuestion.containedStyle(Labelstext.H4, ColorIdResource.WHITE, font = R.font.dmsans_medium)
        tvDescriptionTriageQuestion.containedStyle(Labelstext.BODY1, ColorIdResource.BLACK)
        btnTriageQuestions.containedStyle(ColorIdResource.BLUE, ColorIdResource.WHITE)

        btnTriageQuestions.setOnClickListener {
            activity?.goToActivity<TriageActivity>()
        }
    }


}