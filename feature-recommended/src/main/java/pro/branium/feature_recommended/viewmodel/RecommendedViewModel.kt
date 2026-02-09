package pro.branium.feature_recommended.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pro.branium.core_playback.usecase.GetCurrentPlaylistUseCase
import pro.branium.core_playback.usecase.GetPlayingSongUseCase
import pro.branium.core_ui.mapper.toDisplayModels
import pro.branium.core_ui.model.DisplaySongModel
import pro.branium.core_utils.MusicAppUtils
import pro.branium.feature_recommended.domain.usecase.GetMoreFavoriteSongsUseCase
import pro.branium.feature_recommended.domain.usecase.GetPaginatedSongsUseCase
import pro.branium.feature_recommended.domain.usecase.GetSongsInPlaylistUseCase
import javax.inject.Inject

@HiltViewModel
class RecommendedViewModel @Inject constructor(
    getPaginatedSongsUseCase: GetPaginatedSongsUseCase,
    private val getSongsInPlaylistUseCase: GetSongsInPlaylistUseCase,
    getMoreFavoriteSongUseCase: GetMoreFavoriteSongsUseCase,
    getPlayingSongUseCase: GetPlayingSongUseCase,
    getCurrentPlaylistUseCase: GetCurrentPlaylistUseCase,
) : ViewModel() {
    var recommendedPlaylist = MusicAppUtils.defaultPlaylists[9]!!
    var topRecommendedPlaylist = MusicAppUtils.defaultPlaylists[1]!!

    private val _displayLimitedSongs = MutableStateFlow<List<DisplaySongModel>>(emptyList())
    val moreSongModelFlow = getPaginatedSongsUseCase().cachedIn(viewModelScope)
    val moreFavoriteSongModelFlow = getMoreFavoriteSongUseCase()

    private val baseDisplayPaging: Flow<PagingData<DisplaySongModel>> =
        combine(moreSongModelFlow, moreFavoriteSongModelFlow) { pagingData, songs ->
            pagingData.map { song ->
                DisplaySongModel(song, isFavoriteByUser = songs.contains(song))
            }
        }
    val displayLimitedSongs: StateFlow<List<DisplaySongModel>> =
        _displayLimitedSongs.combine(getPlayingSongUseCase()) { base, now ->
            val currentId = now?.id
            val playing = now?.isPlaying ?: return@combine emptyList()
            val currentPlaylist = getCurrentPlaylistUseCase().value
            base.map { m ->
                if (
                    currentPlaylist != null &&
                    (currentPlaylist.playlistId == topRecommendedPlaylist.playlistId ||
                            currentPlaylist.playlistId == recommendedPlaylist.playlistId)
                ) {
                    val copyObj = m.copy(
                        isNowPlaying = (m.song.id == currentId),
                        isPlaying = playing && (m.song.id == currentId)
                    )
                    copyObj
                } else {
                    m
                }
            }
        }
            .distinctUntilChanged()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Companion.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    init {
        loadLimitedRecommendedSongs()
        loadAllRecommendedSongs()
    }

    fun loadLimitedRecommendedSongs() {
        viewModelScope.launch(Dispatchers.IO) {
            val songs = getSongsInPlaylistUseCase(1)
            _displayLimitedSongs.value = songs.toDisplayModels()
            topRecommendedPlaylist = topRecommendedPlaylist.copy(songs = songs)
        }
    }

    fun loadAllRecommendedSongs() {
        viewModelScope.launch(Dispatchers.IO) {
            val songList = getSongsInPlaylistUseCase(recommendedPlaylist.playlistId)
            if (songList.isNotEmpty()) {
                recommendedPlaylist = recommendedPlaylist.copy(songs = songList)
            }
        }
    }

    val moreDisplayLimitedSongs: Flow<PagingData<DisplaySongModel>> =
        baseDisplayPaging.combine(getPlayingSongUseCase()) { pagingData, now ->
            val currentPlaylist = getCurrentPlaylistUseCase().value
            pagingData.map { item ->
                if (
                    currentPlaylist != null
                    && (currentPlaylist.playlistId == recommendedPlaylist.playlistId ||
                            currentPlaylist.playlistId == topRecommendedPlaylist.playlistId
                            )
                ) {
                    item.copy(
                        isNowPlaying = item.song.id == now?.id,
                        isPlaying = item.song.id == now?.id && now.isPlaying
                    )
                } else {
                    item
                }
            }
        }.cachedIn(viewModelScope)
}