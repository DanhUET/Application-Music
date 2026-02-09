package pro.branium.feature_user.domain.usecase

import pro.branium.feature_user.domain.repository.UserRepository
import javax.inject.Inject

class GetNumberUserInDbUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(): Int = repository.countUser()
}