package pro.branium.feature_artist.viewmodel

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
import pro.branium.core_model.PlaylistModel
import pro.branium.core_playback.usecase.GetPlayingSongUseCase
import pro.branium.core_ui.mapper.toDisplayModel
import pro.branium.core_ui.mapper.toDisplayModels
import pro.branium.core_ui.mapper.toSongModels
import pro.branium.core_ui.model.DisplayArtistModel
import pro.branium.core_ui.model.DisplaySongModel
import pro.branium.feature_artist.domain.usecase.GetArtistByIdUseCase
import pro.branium.feature_artist.domain.usecase.GetArtistByNameUseCase
import pro.branium.feature_artist.domain.usecase.GetSongsForArtistUseCase
import javax.inject.Inject

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(
    private val getArtistByIdUseCase: GetArtistByIdUseCase,
    private val getArtistByNameUseCase: GetArtistByNameUseCase,
    private val getSongsForArtistUseCase: GetSongsForArtistUseCase,
    getPlayingSongUseCase: GetPlayingSongUseCase
) : ViewModel() {
    private val _artistModel = MutableLiveData<DisplayArtistModel?>()
    private val _songs = MutableStateFlow<List<DisplaySongModel>>(emptyList())

    val artistModel: LiveData<DisplayArtistModel?> = _artistModel
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

    var playlist = PlaylistModel()

    fun getArtist(artistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getArtistByIdUseCase(artistId)
            result.collect { artist ->
                _artistModel.postValue(artist?.toDisplayModel())
            }
        }
    }

    fun getArtist(artistName: String) {
        val firstArtist = artistName.substringBefore("ft")
        viewModelScope.launch(Dispatchers.IO) {
            val nameToSearch = firstArtist.ifBlank { artistName }
            val result = getArtistByNameUseCase(nameToSearch)
            result.collect { artist ->
                _artistModel.postValue(artist?.toDisplayModel())
                artist?.let {
                    getSongsByArtist(it.id)
                }
            }
        }
    }

    fun getSongsByArtist(artistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getSongsForArtistUseCase(artistId)
            val songModels = result.toDisplayModels()
            _songs.value = songModels
            playlist = playlist.copy(songs = result)
        }
    }

    fun updatePlaylistSongs(songModels: List<DisplaySongModel>) {
        playlist = playlist.copy(songs = songModels.toSongModels())
    }
}