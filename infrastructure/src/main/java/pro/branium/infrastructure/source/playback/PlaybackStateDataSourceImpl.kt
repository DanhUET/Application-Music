package pro.branium.infrastructure.source.playback

import android.content.SharedPreferences
import androidx.core.content.edit
import pro.branium.core_model.PlaybackStateData
import pro.branium.core_playback.PlaybackStateDataSource
import pro.branium.core_utils.MusicAppUtils.PREF_CURRENT_POSITION
import pro.branium.core_utils.MusicAppUtils.PREF_PLAYLIST_ID
import pro.branium.core_utils.MusicAppUtils.PREF_SONG_ID
import javax.inject.Inject

class PlaybackStateDataSourceImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : PlaybackStateDataSource {
    override fun loadLastPlaybackState(): PlaybackStateData {
        val songId = sharedPreferences.getString(PREF_SONG_ID, null)
        val position = sharedPreferences.getLong(PREF_CURRENT_POSITION, 0L)
        val playlistId = sharedPreferences.getLong(PREF_PLAYLIST_ID, -1)
        return PlaybackStateData(songId, playlistId, position)
    }

    override fun saveLastPlaybackState(playbackStateData: PlaybackStateData) {
        sharedPreferences.edit {
            putString(PREF_SONG_ID, playbackStateData.songId)
                .putLong(PREF_CURRENT_POSITION, playbackStateData.position)
                .putLong(PREF_PLAYLIST_ID, playbackStateData.playlistId ?: 0)
        }
    }
}