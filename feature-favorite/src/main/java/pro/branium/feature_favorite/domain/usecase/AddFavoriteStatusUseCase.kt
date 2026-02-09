package pro.branium.feature_favorite.domain.usecase

import pro.branium.core_domain.repository.FavoriteRepository
import javax.inject.Inject

class AddFavoriteStatusUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    suspend operator fun invoke(songId: String) {
        repository.addFavorite(songId)
    }
}