package com.jumpitt.happ.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jumpitt.happ.R
import com.jumpitt.happ.network.response.NotificationHistoryResponse
import com.jumpitt.happ.utils.ColorIdResource
import com.jumpitt.happ.utils.Labelstext
import com.jumpitt.happ.utils.containedStyle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.item_rounded_toolbar.*


class NotificationsFragment : Fragment(), NotificationContract.View {
    private lateinit var mPresenter: NotificationContract.Presenter

    companion object {
        fun newInstance(): NotificationsFragment = NotificationsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_notifications, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mPresenter = NotificationPresenter(this)
        mPresenter.initializeView()

        srNotiHistory.setOnRefreshListener {
            mPresenter.initializeView()
        }
    }

    override fun setAdapterNotifications(responseNotificationHistory: NotificationHistoryResponse?) {
        responseNotificationHistory?.notifications?.let { notificationList ->
            rvNotificationsHistory?.let {_rvNotificationsHistory ->
                srNotiHistory.isRefreshing = false
                _rvNotificationsHistory.layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL ,false)
                _rvNotificationsHistory.adapter = AdapterNotifications(requireActivity(), notificationList, shimmerNotificationHistory)
            }
        }

    }

    override fun showInitializeView() {
        tvRoundedToolbar.containedStyle(Labelstext.H6, ColorIdResource.BLACK, font = R.font.dmsans_medium)
        showUnwrappingValues()
    }

    override fun showUnwrappingValues() {
        tvRoundedToolbar.text = resources.getString(R.string.tbNotifications)
    }

    override fun showSkeleton() {
        shimmerNotificationHistory?.let {
            shimmerNotificationHistory.startShimmer()
            shimmerNotificationHistory.visibility = View.VISIBLE
        }
    }

    override fun hideSkeleton() {
        shimmerNotificationHistory?.let {
            shimmerNotificationHistory.stopShimmer()
            shimmerNotificationHistory.visibility = View.GONE
        }
    }

}