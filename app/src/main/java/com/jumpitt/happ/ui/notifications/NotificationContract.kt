package com.jumpitt.happ.ui.notifications

import com.jumpitt.happ.network.response.Notification
import com.jumpitt.happ.network.response.NotificationHistoryResponse
import retrofit2.Response

interface NotificationContract {

    interface View{
        fun setAdapterNotifications(responseNotificationHistory: NotificationHistoryResponse?, listFull: List<Notification>)
        fun showInitializeView()
        fun showUnwrappingValues()
        fun showSkeleton()
        fun hideSkeleton()
        fun showLoaderBottom()
        fun hideLoaderBottom()
    }

    interface Presenter{
        fun initializeView(listFull: List<Notification>)
        fun loadNextPage(isLoaderSkeleton: Boolean, currentPage: Int, listFull: List<Notification>)
    }

    interface Interactor{
        fun getAccessToken(isLoaderSkeleton: Boolean = true, currentPage: Int = 1, listFull: List<Notification>)
        fun getNotificationHistory(isLoaderSkeleton: Boolean, accessToken: String, currentPage: Int, listFull: List<Notification>)
    }

    interface Router{

    }

    interface InteractorOutputs{
        fun getAccesTokenOutput(isLoaderSkeleton: Boolean, accessToken: String, currentPage: Int, listFull: List<Notification>)
        fun getNotificationOutput(responseNotificationHistory: NotificationHistoryResponse?, isLoaderSkeleton: Boolean, listFull: List<Notification>)
        fun getNotificationFailureError(listFull: List<Notification>)
        fun getNotificationOutputError(errorCode: Int, response: Response<NotificationHistoryResponse>, listFull: List<Notification>)
    }
}