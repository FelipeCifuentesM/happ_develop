package com.jumpitt.happ.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jumpitt.happ.R
import com.jumpitt.happ.ble.BleManagerImpl
import com.jumpitt.happ.ble.TcnGeneratorImpl
import com.jumpitt.happ.ble.toHex
import com.jumpitt.happ.model.DemoTracing
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TracingLogFragment : Fragment(), TcnBluetoothServiceCallbackDemo {
    private lateinit var mAdapter: DemoTracingAdapter
    private lateinit var demoTracingRecyclerView: RecyclerView
    private var mOptions: ArrayList<DemoTracing> = ArrayList()


    companion object {
        fun newInstance(): TracingLogFragment = TracingLogFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_tracing_log, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        demoTracingRecyclerView = view.findViewById(R.id.rv_demo)
        mAdapter = DemoTracingAdapter()

        mAdapter.data = mOptions
        demoTracingRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        demoTracingRecyclerView.adapter = mAdapter
        //Run tcn ble
        val tcnGenerator = TcnGeneratorImpl(context =  requireActivity().applicationContext)
        val bleManagerImpl = BleManagerImpl(app = requireActivity().applicationContext,tcnGenerator = tcnGenerator)
        bleManagerImpl.startService()


    }

    override fun onTcnFound(tcn: ByteArray, myTcn: ByteArray?, estimatedDistance: Double?) {

        var distance = "Indeterminada"
        if(estimatedDistance != null){
            if (estimatedDistance > 1) {
                distance = "%.2f".format(estimatedDistance) + " metros"
            }else{
                val centimeters = (estimatedDistance * 100).toInt()
                distance = "$centimeters centimetros"
            }
        }



        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        mOptions.add(DemoTracing(tcn = tcn.toHex(), date = currentDate,distance = distance))
        mOptions.sortByDescending{it.date}

        activity?.runOnUiThread {
            Log.e("sep","unopod "+mOptions.size)
            mAdapter.notifyDataSetChanged()
        }


    }
}

interface TcnBluetoothServiceCallbackDemo{
    fun onTcnFound(tcn: ByteArray, myTcn: ByteArray?, estimatedDistance: Double?)
}