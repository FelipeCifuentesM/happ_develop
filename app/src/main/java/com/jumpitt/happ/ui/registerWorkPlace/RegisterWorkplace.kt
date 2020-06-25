package com.jumpitt.happ.ui.registerWorkPlace

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import com.jumpitt.happ.R
import com.jumpitt.happ.network.request.RegisterRequest
import com.jumpitt.happ.network.response.RegionsResponse
import com.jumpitt.happ.ui.ToolbarActivity
import com.jumpitt.happ.utils.*
import kotlinx.android.synthetic.main.register_work_place.*

class RegisterWorkplace: ToolbarActivity(), RegisterWorkPlaceContract.View{
    private lateinit var mPresenter: RegisterWorkPlaceContract.Presenter
    private var regionListCopy: List<RegionsResponse.DataRegions> = emptyList()
    private var regionSelectedData: RegionsResponse.DataRegions? = null
    private var communeId: String? = null
    private var aValidateInputsLogin = booleanArrayOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_work_place)

        toolbarToLoad(toolbar as Toolbar, resources.getString(R.string.tbCreateAccount))
        enableHomeDisplay(true)

        mPresenter = RegisterWorkPlacePresenter(this)
        mPresenter.initializeView()

        btnNextWorkPlace.setOnClickListener {
            val registerDataObject = RegisterRequest(workCommuneId = communeId)
            mPresenter.navigateRegisterWorkplace(registerDataObject)
        }

        filledWorkRegionDropdown.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            aValidateInputsLogin[0] = false
            btnNextWorkPlace.validateInputs(aValidateInputsLogin)
            regionSelectedData = regionListCopy[position]
            communeId = null
            val communesList = regionListCopy[position].communes
            mPresenter.loadCommunes(communesList)
        }

        //Dropdown commune
        filledWorkCommuneDropdown.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            aValidateInputsLogin[0] = true
            btnNextWorkPlace.validateInputs(aValidateInputsLogin)
            regionSelectedData?.let { communeId = it.communes[position].id }
        }
    }

    override fun showInitializeView() {
        tiWorkRegion.typeface = ResourcesCompat.getFont(this, R.font.dmsans_regular)
        tiWorkCommune.typeface = ResourcesCompat.getFont(this, R.font.dmsans_regular)
        tvDataWork.containedStyle(Labelstext.H4, ColorIdResource.BLACK, font = R.font.dmsans_medium)
        btnNextWorkPlace.containedStyle(ColorIdResource.BLUE, ColorIdResource.WHITE)
        btnNextWorkPlace.disabled()
        mPresenter.getRegions()
    }

    override fun loadRegions(regionsNameList: ArrayList<String>, regionsList: List<RegionsResponse.DataRegions>) {
        regionListCopy = regionsList

        val adapter = ArrayAdapter(this, R.layout.item_dropdown_popup, regionsNameList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        filledWorkRegionDropdown.typeface = ResourcesCompat.getFont(this, R.font.dmsans_regular)
        filledWorkRegionDropdown.setTextColor(ResourcesCompat.getColor(resources, R.color.black, null))
        filledWorkRegionDropdown.dropDownHeight = 650
        filledWorkRegionDropdown.setAdapter(adapter)
    }

    override fun loadCommunes(communesNameList: ArrayList<String>) {
        filledWorkCommuneDropdown.setText("")
        val adapter = ArrayAdapter(this, R.layout.item_dropdown_popup, communesNameList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        filledWorkCommuneDropdown.typeface = ResourcesCompat.getFont(this, R.font.dmsans_regular)
        filledWorkCommuneDropdown.setTextColor(ResourcesCompat.getColor(resources, R.color.black, null))
        filledWorkCommuneDropdown.dropDownHeight = 650
        filledWorkCommuneDropdown.setAdapter(adapter)
    }

    override fun showWorkPlaceError(messageError: String) {
        showSnackbar(containerWorkPlace, messageError, ColorIdResource.BLUE, ColorIdResource.WHITE)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.transitionActivity(Transition.RIGHT_TO_LEFT)
    }
}