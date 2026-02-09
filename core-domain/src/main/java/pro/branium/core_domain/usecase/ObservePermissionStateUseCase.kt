package pro.branium.core_domain.usecase

import kotlinx.coroutines.flow.StateFlow
import pro.branium.core_domain.repository.PermissionRepository
import pro.branium.core_model.PermissionState
import javax.inject.Inject

class ObservePermissionStateUseCase @Inject constructor(
    private val repository: PermissionRepository
) {
    operator fun invoke(): StateFlow<PermissionState> = repository.state
}