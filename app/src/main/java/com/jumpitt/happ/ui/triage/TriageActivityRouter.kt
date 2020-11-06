package com.jumpitt.happ.ui.triage

import androidx.appcompat.app.AppCompatActivity
import com.jumpitt.happ.R
import com.jumpitt.happ.model.Question
import com.jumpitt.happ.ui.main.MainActivity
import com.jumpitt.happ.ui.triage.question.QuestionFragment
import com.jumpitt.happ.ui.triage.result.ResultFragmentDefault
import com.jumpitt.happ.ui.triage.result.ResultFragmentScore
import com.jumpitt.happ.utils.enterFromRight
import com.jumpitt.happ.utils.goToActivity
import com.jumpitt.happ.utils.replaceFragmentQuestions

class TriageActivityRouter constructor(private val activity: AppCompatActivity):
    TriageActivityContract.Router {
    private val fragmentContainer = R.id.fragment_container

    override fun displayQuestion(question: Question) {
        activity.replaceFragmentQuestions(QuestionFragment.newInstance(question), fragmentContainer) {
            enterFromRight()
        }
    }

    override fun displayResultScore(tracing: Boolean) {
        activity.replaceFragmentQuestions(ResultFragmentScore.newInstance(4), fragmentContainer) {
            enterFromRight()
        }
    }

    override fun displayResultDefault(tracing: Boolean) {
        activity.replaceFragmentQuestions(ResultFragmentDefault.newInstance(5), fragmentContainer) {
            enterFromRight()
        }
    }

    override fun showMain() {
        activity.goToActivity<MainActivity>("", finish = true)
    }

}