package cl.jumpitt.happ.ui.triage

import androidx.appcompat.app.AppCompatActivity
import cl.jumpitt.happ.R
import cl.jumpitt.happ.model.Question
import cl.jumpitt.happ.ui.main.MainActivity
import cl.jumpitt.happ.ui.triage.question.QuestionFragment
import cl.jumpitt.happ.ui.triage.result.ResultFragment
import cl.jumpitt.happ.utils.enterFromRight
import cl.jumpitt.happ.utils.goToActivity
import cl.jumpitt.happ.utils.replaceFragmentQuestions

class TriageActivityRouter constructor(private val activity: AppCompatActivity):
    TriageActivityContract.Router {
    private val fragmentContainer = R.id.fragment_container

    override fun displayQuestion(question: Question) {
        activity.replaceFragmentQuestions(QuestionFragment.newInstance(question), fragmentContainer) {
            enterFromRight()
        }
    }

    override fun displayResult(tracing: Boolean) {
        activity.replaceFragmentQuestions(ResultFragment.newInstance(4), fragmentContainer) {
            enterFromRight()
        }
    }

    override fun showMain() {
        activity.goToActivity<MainActivity>("", finish = true)
    }

}