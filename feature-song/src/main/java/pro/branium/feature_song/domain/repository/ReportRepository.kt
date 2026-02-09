package pro.branium.feature_song.domain.repository

interface ReportRepository {
    fun report(songId: String)
}