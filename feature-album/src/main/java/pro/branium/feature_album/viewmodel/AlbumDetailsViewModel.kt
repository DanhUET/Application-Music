package pro.branium.feature_album.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pro.branium.core_model.AlbumModel
import pro.branium.core_model.PlaylistModel
import pro.branium.core_model.SongModel
import pro.branium.core_playback.usecase.GetPlayingSongUseCase
import pro.branium.core_ui.mapper.toDisplayModels
import pro.branium.core_ui.mapper.toSongModels
import pro.branium.core_ui.model.DisplayAlbumModel
import pro.branium.core_ui.model.DisplaySongModel
import pro.branium.feature_album.domain.usecase.GetAlbumDetailsUseCase
import pro.branium.feature_album.domain.usecase.GetSongsForAlbumUseCase
import pro.branium.feature_album.ui.mapper.toDisplayAlbumModel
import javax.inject.Inject

@HiltViewModel
class AlbumDetailsViewModel @Inject constructor(
    private val getAlbumDetailsUseCase: GetAlbumDetailsUseCase,
    private val getSongsForAlbumUseCase: GetSongsForAlbumUseCase,
    getPlayingSongUseCase: GetPlayingSongUseCase
) : ViewModel() {
    private val _albumModel = MutableLiveData<DisplayAlbumModel?>()
    private val _songs = MutableStateFlow<List<DisplaySongModel>>(emptyList())

    val albumModel: LiveData<DisplayAlbumModel?>
        get() = _albumModel

    val songs: StateFlow<List<DisplaySongModel>> =
        _songs.combine(getPlayingSongUseCase()) { base, now ->
            val currentId = now?.id
            val playing = now?.isPlaying ?: return@combine emptyList()
            base.map { m ->
                val copyObj = m.copy(
                    isNowPlaying = (m.song.id == currentId),
                    isPlaying = playing && (m.song.id == currentId)
                )
                copyObj
            }
        }.distinctUntilChanged()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    var playlistModel = PlaylistModel()

    fun loadAlbumDetails(albumId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val albumResult = getAlbumDetailsUseCase(albumId)
            _albumModel.postValue(albumResult?.toDisplayAlbumModel())
            if (albumResult != null) {
                createPlaylist(albumResult)
            }
        }
    }

    private fun createPlaylist(albumModel: AlbumModel) {
        val songsList = _songs.value
        playlistModel = playlistModel.copy(
            playlistId = albumModel.id.toLong(),
            name = albumModel.name,
            songs = songsList.toSongModels()
        )
    }

    fun loadSongsForAlbum(albumId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getSongsForAlbumUseCase(albumId)
            if (result.isSuccess) {
                _songs.value = result.getOrDefault(emptyList()).toDisplayModels()
                if (result.getOrDefault(emptyList()).isNotEmpty()) {
                    updatePlaylistSongs(result.getOrDefault(emptyList()))
                }
            }
        }
    }

    private fun updatePlaylistSongs(songModels: List<SongModel>) {
        playlistModel = playlistModel.copy(songs = songModels)
    }
}