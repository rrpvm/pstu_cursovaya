package com.rrpvm.pstu_curs_rrpvm.presentation

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.rrpvm.domain.repository.ClientRepository
import com.rrpvm.domain.repository.KinoRepository
import com.rrpvm.domain.repository.TicketsRepository
import com.rrpvm.domain.service.IAuthenticationService
import com.rrpvm.pstu_curs_rrpvm.presentation.notification.TicketNotificationBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlin.time.toJavaDuration

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val authenticationService: IAuthenticationService,
    private val clientRepository: ClientRepository,
    private val ticketsRepository: TicketsRepository,
    private val kinoRepository: KinoRepository
) : AndroidViewModel(application = application) {
    val isAuth = authenticationService.isAuthenticated
    val isShowActivityProgress = authenticationService.isAuthJobFirst.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        true
    )

    class NotificationWorker(context: Context, workerParameters: WorkerParameters) :
        Worker(context, workerParameters) {
        override fun doWork(): Result {
            TicketNotificationBuilder(applicationContext).buildAlarmNotification()
            return Result.success()
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            ticketsRepository.observeNewTickets()
                .collect { ticket ->
                    kinoRepository.getSessionById(ticket.sessionId)
                        .onSuccess { sessionInfo ->
                            val now = Calendar.getInstance()
                            val difference = sessionInfo.sessionDate.time - now.timeInMillis
                            val delay = if (difference < 3_600_000) {
                                1_000//1 секунда
                            } else {
                                difference - 3_600_000 // как раз разница во времени - 1 час
                            }
                            val notificationWorkRequest =
                                OneTimeWorkRequestBuilder<NotificationWorker>()
                                    .setInitialDelay(
                                        delay.toDuration(DurationUnit.MILLISECONDS).toJavaDuration()
                                    )
                                    .build()

                            withContext(Dispatchers.Main) {
                                WorkManager.getInstance(application)
                                    .enqueue(notificationWorkRequest)
                                TicketNotificationBuilder(
                                    this@MainViewModel.getApplication()
                                ).buildNotification(
                                    sessionInfo
                                )
                            }

                        }.onFailure {
                            it.printStackTrace()
                        }
                }
        }
    }
}