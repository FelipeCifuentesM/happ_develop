package com.jumpitt.happ.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jumpitt.happ.R
import com.jumpitt.happ.ble.BleManagerImpl
import com.jumpitt.happ.ble.TcnBluetoothServiceCallbackDemo
import com.jumpitt.happ.ble.TcnGeneratorImpl
import com.jumpitt.happ.context
import com.jumpitt.happ.model.DemoTracing
import com.jumpitt.happ.ui.profile.DemoTracingAdapter
import kotlinx.android.synthetic.main.fragment_tracing_log.*
import kotlin.collections.ArrayList

class TracingLogActivity: AppCompatActivity(), TcnBluetoothServiceCallbackDemo {
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
        val bleManagerImpl = BleManagerImpl(app = context.applicationContext,tcnGenerator = tcnGenerator, delegateDemo = this)
        bleManagerImpl.startService()

    }

    override fun onTcnFound(tcn: ByteArray, myTcn: ByteArray?, estimatedDistance: Double?) {
    }

}

