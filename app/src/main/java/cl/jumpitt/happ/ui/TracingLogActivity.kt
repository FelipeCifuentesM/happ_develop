package cl.jumpitt.happ.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cl.jumpitt.happ.R
import cl.jumpitt.happ.ble.BleManagerImpl
import cl.jumpitt.happ.ble.TcnGeneratorImpl
import cl.jumpitt.happ.ble.toHex
import cl.jumpitt.happ.context
import cl.jumpitt.happ.model.DemoTracing
import cl.jumpitt.happ.ui.profile.DemoTracingAdapter
import kotlinx.android.synthetic.main.fragment_tracing_log.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TracingLogActivity: AppCompatActivity() {
    private lateinit var mAdapter: DemoTracingAdapter
    private lateinit var demoTracingRecyclerView: RecyclerView
    private var mOptions: ArrayList<DemoTracing> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_tracing_log)

        demoTracingRecyclerView = rv_demo
        mAdapter = DemoTracingAdapter()

        mAdapter.data = mOptions
        demoTracingRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        demoTracingRecyclerView.adapter = mAdapter
        //Run tcn ble
        val tcnGenerator = TcnGeneratorImpl(context =  context.applicationContext)
        val bleManagerImpl = BleManagerImpl(app = context.applicationContext,tcnGenerator = tcnGenerator)
        bleManagerImpl.startService()

    }

}
