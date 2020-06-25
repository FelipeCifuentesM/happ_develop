package com.jumpitt.happ.ui.triage.question

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.jumpitt.happ.R
import com.jumpitt.happ.model.Choice
import com.jumpitt.happ.utils.TriageQuestionType
import com.jumpitt.happ.utils.inflate
import com.jumpitt.happ.utils.setSafeOnClickListener
import kotlinx.android.synthetic.main.item_question_option.view.*

class OptionHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
class OptionsAdapter : RecyclerView.Adapter<OptionHolder>() {
    var delegate: Delegate? = null
    var data = arrayListOf<Choice>()
    var selected = arrayListOf<Choice>()
    lateinit var type: TriageQuestionType
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionHolder {
        return OptionHolder(parent.inflate(R.layout.item_question_option))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: OptionHolder, position: Int) {
        val context = holder.itemView.context
        val root = holder.itemView
        val choice = data[position]

        root.optionValue.text = choice.value
        delegate?.enableNextButton(selected.size >= 1)
        if (selected.contains(choice)) {
            root.check.isInvisible = false
            root.option_card.strokeWidth = 0
            root.optionValue.setTextColor(ContextCompat.getColor(context, R.color.blue))
            root.option_card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.backgroundQuestionSelected))
        } else {
            root.option_card.strokeWidth = 0
            root.optionValue.setTextColor(ContextCompat.getColor(context, R.color.black))
            root.option_card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.backgroundQuestionNotSelected))
            root.check.isInvisible = true
        }

        root.container.setSafeOnClickListener {
            when (type) {
                TriageQuestionType.SIMPLE_SELECTION -> {
                    if (selected.contains(choice)) {
                        selected.remove(choice)
                    } else {
                        selected.clear()
                        selected.add(data[position])
                    }
                }
                TriageQuestionType.MULTIPLE_SELECTION -> {
                    if (selected.contains(choice)) {
                        selected.remove(choice)
                    } else {
                        if (position == itemCount - 1) {
                            selected.clear()
                        } else if (selected.contains(data[itemCount - 1])) {
                            selected.remove(data[itemCount - 1])
                        }
                        selected.add(data[position])
                    }
                }
            }
            delegate?.enableNextButton(selected.size >= 1)
            notifyDataSetChanged()
        }
    }

    interface Delegate {
        fun enableNextButton(isEnable: Boolean)
    }
}