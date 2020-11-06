package com.jumpitt.happ.ui.triage.result

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jumpitt.happ.R
import com.jumpitt.happ.utils.*
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_triage_result_default.*

class ResultFragmentDefault : Fragment() {

    companion object {
        fun newInstance(value: Int): ResultFragmentDefault {
            val fragment = ResultFragmentDefault()
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
        return inflater.inflate(R.layout.fragment_triage_result_default, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTriageResultDefaultTtitle.containedStyle(Labelstext.H4, ColorIdResource.BLACK, .87F, R.font.dmsans_medium)
        tvTriageResultDefaultDescription.containedStyle(Labelstext.BODY1, ColorIdResource.BLACK, font = R.font.dmsans_medium)
        btnFinishTriageResultDefault.containedStyle(ColorIdResource.PRIMARY, ColorIdResource.WHITE)

        mNextButton = view.findViewById(R.id.btnFinishTriageResultDefault)
        mNextButton.setSafeOnClickListener {
            mDelegate?.onResultButtonPressed()
        }
    }

}