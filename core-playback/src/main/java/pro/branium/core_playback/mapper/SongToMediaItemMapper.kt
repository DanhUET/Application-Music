package pro.branium.core_playback.mapper

import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import pro.branium.core_model.SongModel

fun SongModel.toMediaItem(): MediaItem {
    val metadata = MediaMetadata.Builder()
        .setTitle(this.title)
        .setArtist(this.artist)
        .setArtworkUri(this.artworkUrl?.toUri())
        .build()

    return MediaItem.Builder()
        .setMediaId(this.id)
        .setUri(this.source)
        .setMediaMetadata(metadata)
        .build()
}

fun List<SongModel>.toMediaItems(): List<MediaItem> {
    return this.map { it.toMediaItem() }
}