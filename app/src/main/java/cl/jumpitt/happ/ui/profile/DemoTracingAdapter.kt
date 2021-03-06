package cl.jumpitt.happ.ui.profile


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cl.jumpitt.happ.R
import cl.jumpitt.happ.model.DemoTracing
import cl.jumpitt.happ.ui.triage.question.OptionHolder
import cl.jumpitt.happ.utils.inflate
import kotlinx.android.synthetic.main.item_demo_tracing.view.*

class DemoTracingAdapter : RecyclerView.Adapter<OptionHolder>() {
    var data = arrayListOf<DemoTracing>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionHolder {
        return OptionHolder(parent.inflate(R.layout.item_demo_tracing))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: OptionHolder, position: Int) {
        val root = holder.itemView
        val demoTracing = data[position]

        root.tcnFound.text = demoTracing.tcn
        root.distance.text = demoTracing.distance
        root.time.text = demoTracing.date
    }

}