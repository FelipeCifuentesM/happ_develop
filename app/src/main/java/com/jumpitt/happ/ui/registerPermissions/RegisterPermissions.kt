package com.jumpitt.happ.ui.registerPermissions

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.jumpitt.happ.R
import com.jumpitt.happ.ui.ToolbarActivity
import com.jumpitt.happ.utils.*
import kotlinx.android.synthetic.main.register_permissions.*


class RegisterPermissions: ToolbarActivity(), RegisterPermissionsContract.View{
    private lateinit var mPresenter: RegisterPermissionsContract.Presenter
    private var bAdapter: BluetoothAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_permissions)

        toolbarToLoad(toolbar as Toolbar, resources.getString(R.string.tbPermissions))
        enableHomeDisplay(true)

        mPresenter = RegisterPermissionsPresenter(this)
        mPresenter.initializeView()

        bAdapter = BluetoothAdapter.getDefaultAdapter()

        btnNextRegistePermission.setSafeOnClickListener {
            onBluetooth()
        }
    }

    override fun showInitializeView() {
        tvPermissionTitle.containedStyle(Labelstext.H4, ColorIdResource.BLACK, font = R.font.dmsans_medium)
        tvPermissionDescription.containedStyle(Labelstext.SUBTITLE1, ColorIdResource.BLACK)
        btnNextRegistePermission.containedStyle(ColorIdResource.BLUE, ColorIdResource.WHITE)
    }

    override fun showRegisterError(messageError: String) {
        showSnackbar(containerRegisterPermission, messageError, ColorIdResource.BLUE, ColorIdResource.WHITE)
    }


    private fun onBluetooth(){
        bAdapter?.let { _bAdapter ->
            if(_bAdapter.isEnabled){
                //bluetooth is already on
                mPresenter.getRegisterData(true)
            }else{
                var intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent, RequestCode.REQUEST_CODE_ENABLE_BT)
            }
        }?: run {
            showSnackbar(containerRegisterPermission, resources.getString(R.string.snkBluetoothNotAvailable), ColorIdResource.BLUE, ColorIdResource.WHITE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            RequestCode.REQUEST_CODE_ENABLE_BT ->
                if(resultCode  == Activity.RESULT_OK){
                    //Accept permission
                    mPresenter.getRegisterData(true)
                }else{
                    //Not accept permission
                    showSnackbar(containerRegisterPermission, resources.getString(R.string.snkBluetoothPermissionDenied), ColorIdResource.BLUE, ColorIdResource.WHITE)
                }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            RequestCode.LOCATION_BACKGROUND -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.e("Borrar", "acepto permiso")
                } else {
                    Log.e("Borrar", "NO acepto")
                }
                mPresenter.getRegisterData(false)
                return
            }
            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                Log.e("Borrar", "Ignoro todo")
                mPresenter.getRegisterData(false)
                // Ignore all other requests.
            }
        }
    }

    override fun showLoader() {
        pbPermissions.visibility = View.VISIBLE
    }

    override fun hideLoader() {
        pbPermissions.visibility = View.GONE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.transitionActivity(Transition.RIGHT_TO_LEFT)
    }
}