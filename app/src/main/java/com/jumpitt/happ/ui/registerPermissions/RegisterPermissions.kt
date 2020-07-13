package com.jumpitt.happ.ui.registerPermissions

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.jumpitt.happ.R
import com.jumpitt.happ.ui.ToolbarActivity
import com.jumpitt.happ.utils.*
import kotlinx.android.synthetic.main.register_permissions.*


class RegisterPermissions: ToolbarActivity(), RegisterPermissionsContract.View{
    private lateinit var mPresenter: RegisterPermissionsContract.Presenter
    private var bAdapter: BluetoothAdapter? = null
    private var isValidateReturn: Boolean = false
    private var isFromLogin: Boolean = false
    private var isFromRegister: Boolean = false
    private var isFromService: Boolean = false
    private var isFromSplash: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_permissions)

        toolbarToLoad(toolbar as Toolbar, resources.getString(R.string.tbPermissions))
        enableHomeDisplay(true)

        mPresenter = RegisterPermissionsPresenter(this)
        mPresenter.initializeView()

        bAdapter = BluetoothAdapter.getDefaultAdapter()

        btnNextRegisterPermission.setSafeOnClickListener {
            onBluetooth()
        }
    }

    override fun showInitializeView() {
        tvPermissionTitle.containedStyle(Labelstext.H4, ColorIdResource.BLACK, font = R.font.dmsans_medium)
        tvPermissionDescription.containedStyle(Labelstext.SUBTITLE1, ColorIdResource.BLACK)
        btnNextRegisterPermission.containedStyle(ColorIdResource.PRIMARY, ColorIdResource.WHITE)
    }

    override fun showRegisterError(messageError: String) {
        showSnackbar(containerRegisterPermission, messageError, ColorIdResource.PRIMARY, ColorIdResource.WHITE)
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
            showSnackbar(containerRegisterPermission, resources.getString(R.string.snkBluetoothNotAvailable), ColorIdResource.PRIMARY, ColorIdResource.WHITE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            RequestCode.REQUEST_CODE_ENABLE_BT ->
                if(resultCode  == Activity.RESULT_OK){
                    //Accept permission
                    if(isFromLogin){
                        mPresenter.validateTcn()
                        mPresenter.navigateMainActivity()
                    }else if(isFromRegister)
                        mPresenter.getRegisterData(true)
                }else{
                    //Not accept permission
                    showSnackbar(containerRegisterPermission, resources.getString(R.string.snkBluetoothPermissionDenied), ColorIdResource.PRIMARY, ColorIdResource.WHITE)
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

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if(isFromService)
                finish()
            if(isFromSplash){
//                mPresenter.validateTcn()
                mPresenter.navigateMainActivity()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter("action.finish")
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,filter)

        intent.extras?.getBoolean("validateReturnWhitOutPermission")?.let { isValidateReturn = it }?: run { isValidateReturn = false }
        intent.extras?.getBoolean("fromLogin")?.let { isFromLogin = it }?: run { isFromLogin = false }
        intent.extras?.getBoolean("fromRegister")?.let { isFromRegister = it }?: run { isFromRegister = false }
        intent.extras?.getBoolean("fromService")?.let { isFromService = it }?: run { isFromService = false }
        intent.extras?.getBoolean("fromSplash")?.let { isFromSplash = it }?: run { isFromSplash = false }
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver)
    }

    override fun onBackPressed() {
        if(isValidateReturn){
            showSnackbar(containerRegisterPermission, resources.getString(R.string.snkBluetoothPermissionDenied), ColorIdResource.PRIMARY, ColorIdResource.WHITE)
        }else{
            super.onBackPressed()
            this.transitionActivity(Transition.RIGHT_TO_LEFT)
        }


    }
}