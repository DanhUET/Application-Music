package pro.branium.infrastructure.repository.permission

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import pro.branium.core_domain.repository.PermissionRepository
import pro.branium.core_model.PermissionState
import javax.inject.Inject

class PermissionRepositoryImpl @Inject constructor() : PermissionRepository {
    private val _state = MutableStateFlow(PermissionState())
    override val state: StateFlow<PermissionState> = _state

    override fun askPermission() {
        _state.update { it.copy(asked = true, userTriggered = true) }
    }

    override fun grantPermission() {
        _state.update { it.copy(granted = true) }
    }

    override fun revokeTrigger() {
        _state.update { it.copy(userTriggered = false) }
    }
}
