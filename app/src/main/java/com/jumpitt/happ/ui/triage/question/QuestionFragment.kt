package com.jumpitt.happ.ui.triage.question

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jumpitt.happ.R
import com.jumpitt.happ.model.Choice
import com.jumpitt.happ.model.Question
import com.jumpitt.happ.utils.*

import com.google.android.material.button.MaterialButton

//TODO: last answer of multi_choice should deactivate iquique others when selected
class QuestionFragment : Fragment(), OptionsAdapter.Delegate {
    companion object {
        const val ARG_ID = "ID"
        const val ARG_ORDER = "ORDER"
        const val ARG_TYPE = "TYPE"
        const val ARG_TITLE = "TITLE"
        const val ARG_DESC = "DESC"
        const val ARG_CHOICES = "CHOICES"

        fun newInstance(question: Question): QuestionFragment {
            val fragment = QuestionFragment()
            val args = Bundle()
            args.putString(ARG_ID, question.id)
            args.putInt(ARG_ORDER, question.order)
            args.putString(ARG_TYPE, question.type)
            args.putString(ARG_TITLE, question.title)
            args.putString(ARG_DESC, question.description)
            args.putParcelableArrayList(ARG_CHOICES, question.choices)
            fragment.arguments = args
            return fragment
        }
    }

    interface Delegate {
        fun onNextPressed(responses: List<String>)
    }

    /**
     *------------ BEGIN ------------
     */
    private lateinit var mId: String
    private var mOrder: Int = 0
    private lateinit var mType: TriageQuestionType
    private lateinit var mTitle: String
    private var mDescription: String? = null
    private var mOptions: ArrayList<Choice>? = null
    private lateinit var mAdapter: OptionsAdapter

    private lateinit var mOptionsRecyclerView: RecyclerView
    private lateinit var mLabelTitle: TextView
    private lateinit var mLabelDescription: TextView
    private lateinit var mNextButton: MaterialButton

    private var mDelegate: Delegate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            mId = args.getString(ARG_ID)!!
            mOrder = args.getInt(ARG_ORDER)
            mType = TriageQuestionType.valueOf(args.getString(ARG_TYPE)!!)
            mTitle = args.getString(ARG_TITLE)!!
            mDescription = args.getString(ARG_DESC)
            mOptions = args.getParcelableArrayList(ARG_CHOICES)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mDelegate = activity as Delegate
        } catch (e: Exception) {
            Log.e("QuestionFragment", "Cannot attach delegate", e)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_triage_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mOptionsRecyclerView = view.findViewById(R.id.options_list)
        mLabelTitle = view.findViewById(R.id.title_label)
        mLabelDescription = view.findViewById(R.id.description_label)
        mNextButton = view.findViewById(R.id.next_button)

        mLabelTitle.containedStyle(Labelstext.H6,ColorIdResource.BLACK, 0.87F, R.font.dmsans_medium)
        mNextButton.containedStyle(ColorIdResource.BLUE, ColorIdResource.WHITE)
        mNextButton.disabled()

        mTitle?.let { mLabelTitle.text = HtmlCompat.fromHtml(mTitle.asteriskBold(), HtmlCompat.FROM_HTML_MODE_LEGACY) }
        mDescription?.let {
            mLabelDescription.text = it
            mLabelDescription.isVisible = true
        }

        mNextButton.setSafeOnClickListener {
            mDelegate?.onNextPressed(mAdapter.selected.map { it.id })
        }

        mAdapter = OptionsAdapter()

        mOptions?.forEach {
            if (it.selected) mAdapter.selected.add(it)
        }
        mAdapter.delegate = this
        mAdapter.data = mOptions!!
        mAdapter.type = mType


        mOptionsRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mOptionsRecyclerView.adapter = mAdapter
    }

    override fun enableNextButton(isEnable: Boolean) {
        if(isEnable){
            mNextButton.enabled()
        }else{
            mNextButton.disabled()
        }
    }
}