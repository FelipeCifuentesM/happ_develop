package com.jumpitt.happ.ui.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jumpitt.happ.R
import com.jumpitt.happ.network.response.Notification
import com.jumpitt.happ.network.response.NotificationHistoryResponse
import com.jumpitt.happ.utils.ColorIdResource
import com.jumpitt.happ.utils.Labelstext
import com.jumpitt.happ.utils.containedStyle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.item_rounded_toolbar.*
import java.util.stream.Stream


class NotificationsFragment : Fragment(), NotificationContract.View {
    private lateinit var mPresenter: NotificationContract.Presenter
    private lateinit var historyNotificationAdapter: AdapterNotifications
    private var layoutManager = LinearLayoutManager(activity)
    var isLoading = false
    private var listFull: List<Notification> = emptyList()

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
            listFull = emptyList()
            mPresenter.initializeView()
        }
    }

    override fun setAdapterNotifications(responseNotificationHistory: NotificationHistoryResponse?) {
        responseNotificationHistory?.notifications?.let { notificationList ->
            rvNotificationsHistory?.let {_rvNotificationsHistory ->
                listFull = listFull + notificationList
                srNotiHistory.isRefreshing = false
                layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL ,false)
                _rvNotificationsHistory.layoutManager = layoutManager
                historyNotificationAdapter = AdapterNotifications(requireActivity(), listFull, shimmerNotificationHistory)
                _rvNotificationsHistory.adapter = historyNotificationAdapter

                responseNotificationHistory.currentPage.let { currentPage ->
                    if(currentPage == responseNotificationHistory.lastPage){
                        pbPaginationNotiHistory.visibility = View.GONE
                    }
                }


                nsNotificationHistory.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
                    if(scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight && responseNotificationHistory.currentPage < responseNotificationHistory.lastPage){
                        Log.e("Borrar", "cargar mas datos")
                        responseNotificationHistory.currentPage.let { currentPage ->
                            val currentPageNext = currentPage+1
                            mPresenter.loadNextPage(false, currentPageNext)
                        }
                    }
                }

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
            srNotiHistory.isEnabled = false
            pbPaginationNotiHistory.visibility = View.VISIBLE
            llContainerRecycler?.let { it.visibility = View.GONE }
        }
    }

    override fun hideSkeleton() {
        shimmerNotificationHistory?.let {
            shimmerNotificationHistory.stopShimmer()
            shimmerNotificationHistory.visibility = View.GONE
            srNotiHistory?.let { it.isEnabled = true }
            llContainerRecycler?.let { it.visibility = View.VISIBLE }
        }
    }

    override fun showLoaderBottom() {
        pbPaginationNotiHistory?.let {
            pbPaginationNotiHistory.visibility = View.VISIBLE
        }

    }

    override fun hideLoaderBottom() {
        pbPaginationNotiHistory?.let {
            pbPaginationNotiHistory.visibility = View.GONE
        }
    }

}