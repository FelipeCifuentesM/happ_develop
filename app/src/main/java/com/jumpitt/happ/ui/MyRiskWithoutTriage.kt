package com.jumpitt.happ.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jumpitt.happ.R
import com.jumpitt.happ.utils.*
import kotlinx.android.synthetic.main.fragment_item_myrisk_value.*
import kotlinx.android.synthetic.main.fragment_item_without_triage.*

class MyRiskWithoutTriage : Fragment() {
    companion object {
        fun newInstance(): MyRiskWithoutTriage = MyRiskWithoutTriage()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_myrisk_without_triage, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //toolbar
        containerMyRiskValueIcon.visibility = View.GONE
        tvMyRiskValueDescription.visibility = View.GONE
        pbMyRiskValue.visibility = View.GONE
        tvMyRiskValueTitle.containedStyle(Labelstext.H6, ColorIdResource.BLACK, font = R.font.dmsans_medium)

        //Card
        tvWithoutTriageTitle.containedStyle(Labelstext.H3, ColorIdResource.PRIMARY, font = R.font.dmsans_medium)
        tvWithoutTriageDescription.containedStyle(Labelstext.BODY1, ColorIdResource.BLACK)
    }


}