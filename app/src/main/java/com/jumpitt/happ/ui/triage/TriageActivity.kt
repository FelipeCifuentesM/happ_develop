package com.jumpitt.happ.ui.triage

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.jumpitt.happ.R
import com.jumpitt.happ.ui.ToolbarActivity
import com.jumpitt.happ.ui.main.MainActivity
import com.jumpitt.happ.ui.triage.question.QuestionFragment
import com.jumpitt.happ.ui.triage.result.ResultFragmentDefault
import com.jumpitt.happ.ui.triage.result.ResultFragmentScore
import com.jumpitt.happ.utils.*
import kotlinx.android.synthetic.main.activity_triage.*
import kotlinx.android.synthetic.main.activity_triage.toolbar
import kotlinx.android.synthetic.main.fragment_triage_question.*

class TriageActivity : ToolbarActivity(),
    TriageActivityContract.View, QuestionFragment.Delegate, ResultFragmentScore.Delegate, ResultFragmentDefault.Delegate{
    companion object {
        const val TRIAGE_TYPE_TRACING = "TRIAGE_TYPE_TRACING"
    }
    private lateinit var mPresenter: TriageActivityContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_triage)

        val tool = toolbar as Toolbar
        toolbarToLoadFragment(tool, resources.getString(R.string.tbTriage))
        enableHomeDisplay(true)

        mPresenter = TriageActivityPresenter(this)
        mPresenter.getToken()

        tool.setNavigationOnClickListener {
            finish()
        }

    }

    override fun onNextPressed(responses: List<String>) {
        mPresenter.onNextQuestionPressed(responses, intent.hasExtra(TRIAGE_TYPE_TRACING))
    }

    override fun onResultButtonPressed() {
//        if (intent.hasExtra(TRIAGE_TYPE_TRACING)) onBackPressed()
//        else mPresenter.onResultButtonPressed()
        mPresenter.onResultButtonPressed()

    }

    override fun showTriageAnswerError(triageAnswerError: String) {
        showSnackbar(containerTriageActivity, triageAnswerError, ColorIdResource.PRIMARY, ColorIdResource.WHITE)
    }

    override fun showSkeleton() {
        shimmerTriageQuestion.startShimmer()
        shimmerTriageQuestion.visibility = View.VISIBLE
    }

    override fun hideSkeleton() {
        shimmerTriageQuestion.stopShimmer()
        shimmerTriageQuestion.visibility = View.GONE
    }

    override fun showLoader() {
        next_button.disabled()
        pbTriageResult.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        next_button.enabled()
        pbTriageResult.visibility = View.GONE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.transitionActivity(Transition.RIGHT_TO_LEFT)
    }

}