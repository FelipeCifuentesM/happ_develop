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
    }

    interface Presenter{
        fun initializeView()
    }

    interface Interactor{
        fun getAccessToken()
        fun getNotificationHistory(accessToken: String)
    }

    interface Router{

    }

    interface InteractorOutputs{
        fun getAccesTokenOutput(accessToken: String)
        fun getNotificationOutput(responseNotificationHistory: NotificationHistoryResponse?)
        fun getNotificationFailureError()
        fun getNotificationOutputError(errorCode: Int, response: Response<NotificationHistoryResponse>)
    }
}