package pro.branium.feature_album.domain.usecase

import pro.branium.core_model.AlbumModel
import pro.branium.feature_album.domain.repository.AlbumRepository
import javax.inject.Inject

class GetAlbumDetailsUseCase @Inject constructor(
    private val repository: AlbumRepository
) {
    suspend operator fun invoke(albumId: Int): AlbumModel? {
        return repository.getAlbumById(albumId)
    }
}