package com.jumpitt.happ.ui.notifications

import androidx.fragment.app.FragmentActivity
import com.jumpitt.happ.network.request.TokenFCMRequest
import com.jumpitt.happ.network.response.NotificationHistoryResponse
import com.jumpitt.happ.realm.RegisterData
import com.jumpitt.happ.ui.profile.ProfileFragment

interface NotificationContract {

    interface View{
        fun setAdapterNotifications(responseNotificationHistory: NotificationHistoryResponse?)
        fun showInitializeView()
        fun showUnwrappingValues()
    }

    interface Presenter{
        fun initializeView()
    }

    interface Interactor{
        fun getNotificationHistory()
    }

    interface Router{

    }

    interface InteractorOutputs{
        fun getNotificationOutput(responseNotificationHistory: NotificationHistoryResponse?)
        fun getNotificationFailureError()
    }
}