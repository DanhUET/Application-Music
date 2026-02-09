package pro.branium.core_domain.usecase

import pro.branium.core_domain.repository.PermissionRepository
import javax.inject.Inject

class RevokeNotificationTriggerUseCase @Inject constructor(
    private val repository: PermissionRepository
) {
    operator fun invoke() {
        repository.revokeTrigger()
    }
}