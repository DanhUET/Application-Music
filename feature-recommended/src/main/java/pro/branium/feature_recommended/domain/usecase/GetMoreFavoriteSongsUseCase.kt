package pro.branium.feature_recommended.domain.usecase

import kotlinx.coroutines.flow.Flow
import pro.branium.core_domain.repository.FavoriteRepository
import pro.branium.core_model.SongModel
import javax.inject.Inject

class GetMoreFavoriteSongsUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    operator fun invoke(): Flow<List<SongModel>> {
        return repository.getFavorites()
    }
}