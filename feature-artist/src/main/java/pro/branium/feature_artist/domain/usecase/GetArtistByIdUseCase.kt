package pro.branium.feature_artist.domain.usecase

import pro.branium.feature_artist.domain.repository.ArtistRepository
import javax.inject.Inject

class GetArtistByIdUseCase @Inject constructor(private val repository: ArtistRepository) {
    suspend operator fun invoke(artistId: Int) = repository.getArtistById(artistId)
}