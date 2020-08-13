package com.jumpitt.happ.ui.notifications

import androidx.fragment.app.Fragment
import com.jumpitt.happ.network.response.NotificationHistoryResponse

class NotificationPresenter constructor(val fragment: Fragment): NotificationContract.Presenter, NotificationContract.InteractorOutputs {
    private var mView: NotificationContract.View = fragment as NotificationContract.View
    private var mRouter: NotificationContract.Router = NotificationRouter(fragment)
    private var mInteractor: NotificationContract.Interactor = NotificationInteractor(this)

    override fun initializeView() {
        mView.showInitializeView()
        mInteractor.getNotificationHistory()
    }

    override fun getNotificationOutput(responseNotificationHistory: NotificationHistoryResponse?) {
        mView.setAdapterNotifications(responseNotificationHistory)

    }

    override fun getNotificationFailureError() {

    }

}