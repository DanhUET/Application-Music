package pro.branium.presentation_common.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import pro.branium.core_domain.usecase.AskNotificationPermissionUseCase
import pro.branium.core_domain.usecase.GrantNotificationPermissionUseCase
import pro.branium.core_domain.usecase.ObservePermissionStateUseCase
import pro.branium.core_domain.usecase.RevokeNotificationTriggerUseCase
import pro.branium.core_model.PermissionState
import javax.inject.Inject

@HiltViewModel
class PostNotificationPermissionViewModel @Inject constructor(
    private val askPermissionUseCase: AskNotificationPermissionUseCase,
    private val grantPermissionUseCase: GrantNotificationPermissionUseCase,
    private val revokeTriggerUseCase: RevokeNotificationTriggerUseCase,
    observePermissionStateUseCase: ObservePermissionStateUseCase
) : ViewModel() {
    val state: StateFlow<PermissionState> = observePermissionStateUseCase()

    fun askPermission() = askPermissionUseCase()

    fun grantPermission() = grantPermissionUseCase()

    fun revokeTrigger() = revokeTriggerUseCase()
}