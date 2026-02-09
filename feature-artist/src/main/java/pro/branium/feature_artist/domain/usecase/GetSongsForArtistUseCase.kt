package pro.branium.feature_artist.domain.usecase

import pro.branium.core_domain.repository.SongRepository
import javax.inject.Inject

class GetSongsForArtistUseCase @Inject constructor(private val repository: SongRepository) {
    suspend operator fun invoke(artistId: Int) = repository.getSongsByArtist(artistId)
}