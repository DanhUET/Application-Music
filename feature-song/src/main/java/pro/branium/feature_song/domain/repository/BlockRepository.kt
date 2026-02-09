package pro.branium.feature_song.domain.repository

interface BlockRepository {
    fun block(songId: String)
}