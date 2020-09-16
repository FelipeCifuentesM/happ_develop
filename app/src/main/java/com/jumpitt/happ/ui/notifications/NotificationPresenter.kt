package com.jumpitt.happ.ui.notifications

import androidx.fragment.app.Fragment
import com.jumpitt.happ.network.response.Notification
import com.jumpitt.happ.network.response.NotificationHistoryResponse
import com.jumpitt.happ.utils.addPaginationListValidatingLastDay
import io.sentry.Sentry
import retrofit2.Response

class NotificationPresenter constructor(val fragment: Fragment): NotificationContract.Presenter, NotificationContract.InteractorOutputs {
    private var mView: NotificationContract.View = fragment as NotificationContract.View
    private var mRouter: NotificationContract.Router = NotificationRouter(fragment)
    private var mInteractor: NotificationContract.Interactor = NotificationInteractor(this)

    override fun initializeView(listFull: List<Notification>) {
        mView.showInitializeView()
        mView.showSkeleton()
        mInteractor.getAccessToken(listFull = listFull)
    }

    override fun loadNextPage(isLoaderSkeleton: Boolean, currentPage: Int, listFull: List<Notification>) {
        mInteractor.getAccessToken(isLoaderSkeleton, currentPage, listFull)
    }

    override fun getAccesTokenOutput(isLoaderSkeleton: Boolean, accessToken: String, currentPage: Int, listFull: List<Notification>) {
        mInteractor.getNotificationHistory(isLoaderSkeleton, accessToken, currentPage, listFull)
    }

    override fun getNotificationOutput(responseNotificationHistory: NotificationHistoryResponse?, isLoaderSkeleton: Boolean, listFull: List<Notification>) {
        var listFullValidation = listFull
        responseNotificationHistory?.notifications?.let { notificationList ->
            listFullValidation = listFull.addPaginationListValidatingLastDay(notificationList)
        }

        mView.setAdapterNotifications(responseNotificationHistory, listFullValidation)
        mView.hideSkeleton()
        if(listFullValidation.isNullOrEmpty())
            mView.hideLoaderBottom()
    }

    override fun getNotificationFailureError(listFull: List<Notification>) {
        val notificationHistoryEmpty = NotificationHistoryResponse(emptyList())
        Sentry.capture("Error al cargar API de notificaciones (notifications), error: Failure")
        mView.setAdapterNotifications(notificationHistoryEmpty, listFull)
        mView.hideSkeleton()
        mView.hideLoaderBottom()
        if(listFull.isNullOrEmpty())
            mView.hideLoaderBottom()
    }

    override fun getNotificationOutputError(errorCode: Int, response: Response<NotificationHistoryResponse>, listFull: List<Notification>) {
        val notificationHistoryEmpty = NotificationHistoryResponse(emptyList())
        Sentry.capture("Error al cargar API de notificaciones (notifications), error: $errorCode")
        mView.setAdapterNotifications(notificationHistoryEmpty, listFull)
        mView.hideSkeleton()
        mView.hideLoaderBottom()
        if(listFull.isNullOrEmpty())
            mView.hideLoaderBottom()
    }

}