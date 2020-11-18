package com.jumpitt.happ.ui

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jumpitt.happ.R
import com.jumpitt.happ.realm.TriageReturnValue
import com.jumpitt.happ.utils.*
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_myrisk_completed_text.*

class MyRiskAnswerCompletedText: Fragment() {
    companion object {
        fun newInstance(): MyRiskAnswerCompletedText = MyRiskAnswerCompletedText()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_myrisk_completed_text, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val realm = Realm.getDefaultInstance()
        val healthCareStatusLocal = realm.where(TriageReturnValue::class.java).findFirst()

        //toolbar
        tvRoundedToolbar.containedStyle(Labelstext.H6, ColorIdResource.BLACK, font = R.font.dmsans_medium)

        //Information
        tvMyRiskCompletedTextTitle.containedStyle(Labelstext.H5, ColorIdResource.PRIMARY, font = R.font.dmsans_medium)
        tvMyRiskCompletedTextDescription.containedStyle(Labelstext.BODY1, ColorIdResource.BLACK)

        healthCareStatusLocal?.resultTypeTextTitle?.let { tvMyRiskCompletedTextTitle.text = it.formatHtml() }
        healthCareStatusLocal?.resultTypeTextDescription?.let { tvMyRiskCompletedTextDescription.text = it.formatHtml() }

    }


}