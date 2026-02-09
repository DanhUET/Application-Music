package pro.branium.core_domain.playlist.usecase

import pro.branium.core_domain.playlist.repository.PlaylistRepository
import pro.branium.core_domain.playlist.result.AddSongResult
import javax.inject.Inject

class AddSongToPlaylistUseCase @Inject constructor(
    private val repository: PlaylistRepository,
    private val isSongInPlaylistUseCase: IsSongInPlaylistUseCase
) {
    suspend operator fun invoke(playlistId: Long, songId: String): AddSongResult {
        return if (isSongInPlaylistUseCase(playlistId, songId)) {
            AddSongResult.AlreadyExists
        } else {
            val insertedRowId = repository.insertPlaylistSongCrossRef(playlistId, songId)
            if (insertedRowId != -1L) AddSongResult.Added else AddSongResult.Error
        }
    }
}