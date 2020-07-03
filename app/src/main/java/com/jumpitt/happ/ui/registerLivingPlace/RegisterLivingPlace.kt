package com.jumpitt.happ.ui.registerLivingPlace

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
import kotlinx.android.synthetic.main.register_living_place.*


class RegisterLivingPlace: ToolbarActivity(), RegisterLivingPlaceContract.View{
    private lateinit var mPresenter: RegisterLivingPlaceContract.Presenter
    private var regionListCopy: List<RegionsResponse.DataRegions> = emptyList()
    private var communeId: String? = null
    private var regionSelectedData: RegionsResponse.DataRegions? = null
    private var aValidateInputsLogin = booleanArrayOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_living_place)

        toolbarToLoad(toolbar as Toolbar, resources.getString(R.string.tbCreateAccount))
        enableHomeDisplay(true)

        mPresenter = RegisterLivingPlacePresenter(this)
        mPresenter.initializeView()

        btnNextLivingPlace.setSafeOnClickListener {
            val registerDataObject = RegisterRequest(homeCommuneId = communeId)
            mPresenter.navigateRegisterWorkplace(registerDataObject)
        }

        //Dropdown region
        filledLivingRegionDropdown.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            aValidateInputsLogin[0] = false
            btnNextLivingPlace.validateInputs(aValidateInputsLogin)
            regionSelectedData = regionListCopy[position]
            communeId = null
            val communesList = regionListCopy[position].communes
            mPresenter.loadCommunes(communesList)
        }

        //Dropdown commune
        filledLivingCommuneDropdown.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            aValidateInputsLogin[0] = true
            btnNextLivingPlace.validateInputs(aValidateInputsLogin)
            regionSelectedData?.let { communeId = it.communes[position].id }
        }
    }


    override fun showInitializeView() {

        tiLivingRegion.typeface = ResourcesCompat.getFont(this, R.font.dmsans_regular)
        tiLivingCommune.typeface = ResourcesCompat.getFont(this, R.font.dmsans_regular)
        tvDataLive.containedStyle(Labelstext.H4, ColorIdResource.BLACK, font = R.font.dmsans_medium)
        btnNextLivingPlace.containedStyle(ColorIdResource.BLUE, ColorIdResource.WHITE)
        btnNextLivingPlace.disabled()

        mPresenter.getRegions()
    }

    override fun loadRegions(regionsNameList: ArrayList<String>, regionsList: List<RegionsResponse.DataRegions>) {
        regionListCopy = regionsList

        val adapter = ArrayAdapter(this, R.layout.item_dropdown_popup, regionsNameList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        filledLivingRegionDropdown.typeface = ResourcesCompat.getFont(this, R.font.dmsans_regular)
        filledLivingRegionDropdown.setTextColor(ResourcesCompat.getColor(resources, R.color.black, null))
        filledLivingRegionDropdown.dropDownHeight = 650
        filledLivingRegionDropdown.setAdapter(adapter)
    }

    override fun loadCommunes(communesNameList: ArrayList<String>) {
        filledLivingCommuneDropdown.setText("")
        val adapter = ArrayAdapter(this, R.layout.item_dropdown_popup, communesNameList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        filledLivingCommuneDropdown.typeface = ResourcesCompat.getFont(this, R.font.dmsans_regular)
        filledLivingCommuneDropdown.setTextColor(ResourcesCompat.getColor(resources, R.color.black, null))
        filledLivingCommuneDropdown.dropDownHeight = 650
        filledLivingCommuneDropdown.setAdapter(adapter)
    }

    override fun showLivingPlaceError(messageError: String) {
        showSnackbar(containerLivingPlace, messageError, ColorIdResource.BLUE, ColorIdResource.WHITE)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.transitionActivity(Transition.RIGHT_TO_LEFT)
    }
}
