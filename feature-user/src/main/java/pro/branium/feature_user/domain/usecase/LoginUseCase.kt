package pro.branium.feature_user.domain.usecase

import pro.branium.feature_user.domain.model.UserModel
import pro.branium.feature_user.domain.repository.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(userModel: UserModel): UserModel? = repository.login(userModel)
}