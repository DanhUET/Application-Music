package pro.branium.feature_album.domain.usecase

import pro.branium.core_model.SongModel
import pro.branium.feature_album.domain.repository.AlbumRepository
import javax.inject.Inject

class GetSongsForAlbumUseCase @Inject constructor(
    private val repository: AlbumRepository
) {
    suspend operator fun invoke(albumId: Int): Result<List<SongModel>> {
        return repository.getSongsForAlbum(albumId)
    }
}