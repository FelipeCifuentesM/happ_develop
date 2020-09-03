package com.jumpitt.happ.ui.notifications

import androidx.fragment.app.Fragment
import com.jumpitt.happ.R
import com.jumpitt.happ.network.response.NotificationHistoryResponse
import io.sentry.Sentry
import retrofit2.Response

class NotificationPresenter constructor(val fragment: Fragment): NotificationContract.Presenter, NotificationContract.InteractorOutputs {
    private var mView: NotificationContract.View = fragment as NotificationContract.View
    private var mRouter: NotificationContract.Router = NotificationRouter(fragment)
    private var mInteractor: NotificationContract.Interactor = NotificationInteractor(this)

    override fun initializeView() {
        mView.showInitializeView()
        mView.showSkeleton()
        mInteractor.getAccessToken()
    }

    override fun loadNextPage(isLoaderSkeleton: Boolean) {
        mInteractor.getAccessToken(isLoaderSkeleton)
    }

    override fun getAccesTokenOutput(isLoaderSkeleton: Boolean, accessToken: String) {
        mInteractor.getNotificationHistory(isLoaderSkeleton, accessToken)
    }

    override fun getNotificationOutput(responseNotificationHistory: NotificationHistoryResponse?, isLoaderSkeleton: Boolean) {
        mView.setAdapterNotifications(responseNotificationHistory)
        mView.hideSkeleton()
    }

    override fun getNotificationFailureError() {
        val notificationHistoryEmpty = NotificationHistoryResponse(emptyList())
        Sentry.capture("Error al cargar API de notificaciones (notifications), error: Failure")
        mView.setAdapterNotifications(notificationHistoryEmpty)
        mView.hideSkeleton()
    }

    override fun getNotificationOutputError(errorCode: Int, response: Response<NotificationHistoryResponse>) {
        val notificationHistoryEmpty = NotificationHistoryResponse(emptyList())
        Sentry.capture("Error al cargar API de notificaciones (notifications), error: $errorCode")
        mView.setAdapterNotifications(notificationHistoryEmpty)
        mView.hideSkeleton()
    }

}