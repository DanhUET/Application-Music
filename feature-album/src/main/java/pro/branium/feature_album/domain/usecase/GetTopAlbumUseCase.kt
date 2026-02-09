package pro.branium.feature_album.domain.usecase

import pro.branium.feature_album.domain.repository.AlbumRepository
import javax.inject.Inject

class GetTopAlbumUseCase @Inject constructor(
    private val repository: AlbumRepository
) {
    suspend operator fun invoke(limit: Int) = repository.getTopAlbums(limit)
}