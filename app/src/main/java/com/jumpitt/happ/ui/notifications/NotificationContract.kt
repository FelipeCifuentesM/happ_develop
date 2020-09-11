package com.jumpitt.happ.ui.notifications

import androidx.fragment.app.FragmentActivity
import com.jumpitt.happ.network.request.TokenFCMRequest
import com.jumpitt.happ.network.response.NotificationHistoryResponse
import com.jumpitt.happ.network.response.ProfileResponse
import com.jumpitt.happ.realm.RegisterData
import com.jumpitt.happ.ui.profile.ProfileFragment
import retrofit2.Response

interface NotificationContract {

    interface View{
        fun setAdapterNotifications(responseNotificationHistory: NotificationHistoryResponse?)
        fun showInitializeView()
        fun showUnwrappingValues()
        fun showSkeleton()
        fun hideSkeleton()
        fun showLoaderBottom()
        fun hideLoaderBottom()
    }

    interface Presenter{
        fun initializeView()
        fun loadNextPage(isLoaderSkeleton: Boolean, currentPage: Int)
    }

    interface Interactor{
        fun getAccessToken(isLoaderSkeleton: Boolean = true, currentPage: Int = 1)
        fun getNotificationHistory(isLoaderSkeleton: Boolean, accessToken: String, currentPage: Int)
    }

    interface Router{

    }

    interface InteractorOutputs{
        fun getAccesTokenOutput(isLoaderSkeleton: Boolean, accessToken: String, currentPage: Int)
        fun getNotificationOutput(responseNotificationHistory: NotificationHistoryResponse?, isLoaderSkeleton: Boolean)
        fun getNotificationFailureError()
        fun getNotificationOutputError(errorCode: Int, response: Response<NotificationHistoryResponse>)
    }
}