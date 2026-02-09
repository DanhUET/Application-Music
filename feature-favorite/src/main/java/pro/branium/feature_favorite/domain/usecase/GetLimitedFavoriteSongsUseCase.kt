package pro.branium.feature_favorite.domain.usecase

import kotlinx.coroutines.flow.Flow
import pro.branium.core_model.SongModel
import pro.branium.core_domain.repository.FavoriteRepository
import javax.inject.Inject

class GetLimitedFavoriteSongsUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    operator fun invoke(limit: Int): Flow<List<SongModel>> {
        return repository.getLimitedFavoriteSong(limit)
    }
}