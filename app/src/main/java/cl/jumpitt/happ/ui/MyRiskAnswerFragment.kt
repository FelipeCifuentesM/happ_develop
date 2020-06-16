package cl.jumpitt.happ.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cl.jumpitt.happ.R
import cl.jumpitt.happ.ui.triage.TriageActivity
import cl.jumpitt.happ.utils.ColorIdResource
import cl.jumpitt.happ.utils.Labelstext
import cl.jumpitt.happ.utils.containedStyle
import cl.jumpitt.happ.utils.goToActivity
import kotlinx.android.synthetic.main.fragment_answer_triage.*
import kotlinx.android.synthetic.main.fragment_item_myrisk_value.*

class MyRiskAnswerFragment : Fragment() {
    companion object {
        fun newInstance(): MyRiskAnswerFragment = MyRiskAnswerFragment()
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
        tvTitleTriageQuestion.containedStyle(Labelstext.H4, ColorIdResource.WHITE, font = R.font.dmsans_medium)
        tvDescriptionTriageQuestion.containedStyle(Labelstext.BODY1, ColorIdResource.BLACK)
        btnTriageQuestions.containedStyle(ColorIdResource.BLUE, ColorIdResource.WHITE)

        btnTriageQuestions.setOnClickListener {
            activity?.goToActivity<TriageActivity>()
        }

    }


}