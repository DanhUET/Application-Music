package pro.branium.feature_favorite.domain.usecase

import kotlinx.coroutines.flow.Flow
import pro.branium.core_domain.repository.FavoriteRepository
import javax.inject.Inject

class IsFavoriteSongUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    operator fun invoke(songId: String): Flow<Boolean> {
        return repository.isFavorite(songId)
    }
}