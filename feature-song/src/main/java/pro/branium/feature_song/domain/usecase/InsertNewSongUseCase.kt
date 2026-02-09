package pro.branium.feature_song.domain.usecase

import pro.branium.core_domain.repository.SongRepository
import pro.branium.core_model.SongModel
import javax.inject.Inject

class InsertNewSongUseCase @Inject constructor(private val repository: SongRepository) {
    suspend operator fun invoke(vararg songModels: SongModel) {
        repository.insert(*songModels)
    }
}