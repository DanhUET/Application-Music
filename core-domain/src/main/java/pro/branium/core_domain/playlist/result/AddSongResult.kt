package pro.branium.core_domain.playlist.result

sealed class AddSongResult {
    object Added : AddSongResult()
    object AlreadyExists : AddSongResult()
    object Error : AddSongResult()
}