package pro.branium.feature_user.domain.usecase

import pro.branium.feature_user.domain.model.UserModel
import pro.branium.feature_user.domain.repository.UserRepository
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(userModel: UserModel): Boolean {
        return repository.deleteUser(userModel)
    }
}