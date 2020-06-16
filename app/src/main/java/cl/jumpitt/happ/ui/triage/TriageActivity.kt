package cl.jumpitt.happ.ui.triage

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import cl.jumpitt.happ.R
import cl.jumpitt.happ.ui.ToolbarActivity
import cl.jumpitt.happ.ui.triage.question.QuestionFragment
import cl.jumpitt.happ.ui.triage.result.ResultFragment
import cl.jumpitt.happ.utils.ColorIdResource
import cl.jumpitt.happ.utils.showSnackbar
import cl.jumpitt.happ.utils.transitionActivity
import kotlinx.android.synthetic.main.activity_triage.*

class TriageActivity : ToolbarActivity(),
    TriageActivityContract.View, QuestionFragment.Delegate, ResultFragment.Delegate{
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
        showSnackbar(containerTriageActivity, triageAnswerError, ColorIdResource.BLUE, ColorIdResource.WHITE)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.transitionActivity("")
    }

}