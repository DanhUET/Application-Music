package pro.branium.core_domain.repository

import kotlinx.coroutines.flow.StateFlow
import pro.branium.core_model.PermissionState

interface PermissionRepository {
    val state: StateFlow<PermissionState>

    fun askPermission()
    fun grantPermission()
    fun revokeTrigger()
}