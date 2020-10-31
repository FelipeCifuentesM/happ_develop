package com.jumpitt.happ.ui.happHome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jumpitt.happ.R
import com.jumpitt.happ.utils.ColorIdResource
import com.jumpitt.happ.utils.Labelstext
import com.jumpitt.happ.utils.containedStyle
import kotlinx.android.synthetic.main.fragment_happ_home.*


class HappHomeFragment : Fragment() {

    companion object {
        fun newInstance(): HappHomeFragment = HappHomeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_happ_home, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        tvRoundedToolbar.containedStyle(Labelstext.H6, ColorIdResource.BLACK, font = R.font.dmsans_medium)
        tvCompanyName.containedStyle(Labelstext.H3, ColorIdResource.PRIMARY, font = R.font.dmsans_medium)
        tvHappBackground.containedStyle(Labelstext.BODY1, ColorIdResource.BLACK, 0.74F, R.font.dmsans_medium)
    }
}