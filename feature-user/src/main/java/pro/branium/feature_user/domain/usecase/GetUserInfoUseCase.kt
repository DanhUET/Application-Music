package pro.branium.feature_user.domain.usecase

import kotlinx.coroutines.flow.Flow
import pro.branium.feature_user.domain.model.UserModel
import pro.branium.feature_user.domain.repository.UserRepository
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(private val repository: UserRepository) {
    operator fun invoke(userId: Int): Flow<UserModel?> = repository.getUserInfo(userId)
}