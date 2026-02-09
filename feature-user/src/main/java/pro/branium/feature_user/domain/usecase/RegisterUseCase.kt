package pro.branium.feature_user.domain.usecase

import pro.branium.feature_user.domain.model.UserModel
import pro.branium.feature_user.domain.repository.UserRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(userModel: UserModel): Boolean {
        val numberOfUsers = repository.countUser()
        if (numberOfUsers == 0) {
            return repository.register(userModel)
        }
        return false
    }
}