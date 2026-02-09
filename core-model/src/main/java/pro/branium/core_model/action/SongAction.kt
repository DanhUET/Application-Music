package pro.branium.core_model.action

sealed class SongAction {
    data class Download(val songId: String): SongAction()
    data class ViewArtist(val artistId: Int): SongAction()
    data class ViewAlbum(val albumId: Int): SongAction()
    data class ViewSong(val songId: String): SongAction()
    data class AddToFavorite(val songId: String): SongAction()
    data class AddToQueue(val songId: String): SongAction()
    data class AddToPlaylist(val songId: String): SongAction()
    data class Block(val songId: String): SongAction()
    data class ReportError(val songId: String): SongAction()
}