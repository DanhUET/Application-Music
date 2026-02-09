package pro.branium.feature_artist.domain.usecase

import pro.branium.feature_artist.domain.repository.ArtistRepository
import javax.inject.Inject

class GetArtistByNameUseCase @Inject constructor(
    private val repository: ArtistRepository
) {
    suspend operator fun invoke(artistName: String) = repository.getArtistByName(artistName)
}