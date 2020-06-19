package cl.jumpitt.happ.ui.registerPermissions

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import cl.jumpitt.happ.R
import cl.jumpitt.happ.ui.ToolbarActivity
import cl.jumpitt.happ.utils.*
import kotlinx.android.synthetic.main.register_permissions.*


class RegisterPermissions: ToolbarActivity(), RegisterPermissionsContract.View{
    private lateinit var mPresenter: RegisterPermissionsContract.Presenter
    private var bAdapter: BluetoothAdapter? = null
    private val REQUEST_CODE_ENABLE_BT = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_permissions)

        toolbarToLoad(toolbar as Toolbar, resources.getString(R.string.tbPermissions))
        enableHomeDisplay(true)

        mPresenter = RegisterPermissionsPresenter(this)
        mPresenter.initializeView()


        bAdapter = BluetoothAdapter.getDefaultAdapter()

        btnNextRegistePermission.setOnClickListener {
//            mPresenter.navigateRegisterSuccess()
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
                mPresenter.getRegisterData()
            }else{
                var intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent, REQUEST_CODE_ENABLE_BT)
            }
        }?: run {
            showSnackbar(containerRegisterPermission, resources.getString(R.string.snkBluetoothNotAvailable), ColorIdResource.BLUE, ColorIdResource.WHITE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            REQUEST_CODE_ENABLE_BT ->
                if(resultCode  == Activity.RESULT_OK){
                    //Accept permission
                    mPresenter.getRegisterData()
                }else{
                    //Not accept permission
                    showSnackbar(containerRegisterPermission, resources.getString(R.string.snkBluetoothPermissionDenied), ColorIdResource.BLUE, ColorIdResource.WHITE)
                }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.transitionActivity(Transition.RIGHT_TO_LEFT)
    }
}