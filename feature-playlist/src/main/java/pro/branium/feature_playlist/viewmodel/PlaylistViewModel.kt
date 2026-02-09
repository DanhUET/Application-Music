package pro.branium.feature_playlist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pro.branium.core_domain.playlist.result.AddSongResult
import pro.branium.core_domain.playlist.usecase.AddSongToPlaylistUseCase
import pro.branium.core_model.PlaylistModel
import pro.branium.core_ui.model.DisplayPlaylist
import pro.branium.feature_playlist.domain.usecase.CreatePlaylistUseCase
import pro.branium.feature_playlist.domain.usecase.DeletePlaylistUseCase
import pro.branium.feature_playlist.domain.usecase.FindPlaylistByNameUseCase
import pro.branium.feature_playlist.domain.usecase.GetAllPlaylistsUseCase
import pro.branium.feature_playlist.domain.usecase.GetLimitedPlaylistsUseCase
import pro.branium.core_domain.usecase.GetPlaylistSummaryModelsUseCase
import pro.branium.feature_playlist.domain.usecase.InitPlaylistIdGeneratorUseCase
import pro.branium.feature_playlist.domain.usecase.RenamePlaylistUseCase
import pro.branium.feature_playlist.mapper.toUiModels
import pro.branium.feature_playlist.ui.model.PlaylistItemModel
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    getLimitedPlaylistsUseCase: GetLimitedPlaylistsUseCase,
    getAllPlaylistsUseCase: GetAllPlaylistsUseCase,
    private val createPlaylistUseCase: CreatePlaylistUseCase,
    private val addSongToPlaylistUseCase: AddSongToPlaylistUseCase,
    private val renamePlaylistUseCase: RenamePlaylistUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase,
    private val findPlaylistByNameUseCase: FindPlaylistByNameUseCase,
    private val initPlaylistIdGeneratorUseCase: InitPlaylistIdGeneratorUseCase,
    private val getPlaylistSummaryModelsUseCase: GetPlaylistSummaryModelsUseCase
) : ViewModel() {
    private val _addResult = MutableLiveData<AddSongResult>()
    private val _playlistWithSongs = MutableLiveData<PlaylistModel>()
    private val _searchedPlaylistModel = MutableLiveData<PlaylistModel?>()

    lateinit var selectedPlaylistModel: DisplayPlaylist
    val searchedPlaylistModel: LiveData<PlaylistModel?> = _searchedPlaylistModel
    val limitedPlaylists: StateFlow<List<PlaylistItemModel>> =
        getPlaylistSummaryModelsUseCase(LIMIT)
            .map { it.toUiModels() }
            .stateIn(
                viewModelScope,
                SharingStarted.Companion.WhileSubscribed(5000),
                emptyList()
            )
    val allPlaylist = getAllPlaylistsUseCase()
        .map { it.toUiModels() }
        .stateIn(
            viewModelScope,
            SharingStarted.Companion.WhileSubscribed(5000),
            emptyList()
        )

    val addResult: LiveData<AddSongResult>
        get() = _addResult

    val playlistWithSongs: LiveData<PlaylistModel>
        get() = _playlistWithSongs

    init {
        viewModelScope.launch(Dispatchers.IO) {
            initPlaylistIdGeneratorUseCase()
        }
    }

    fun createNewPlaylist(playlistName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            createPlaylistUseCase(playlistName)
        }
    }

    fun findPlaylistByName(playlistName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = findPlaylistByNameUseCase(playlistName.trim())
            _searchedPlaylistModel.postValue(result)
        }
    }

    fun updatePlaylist(playlistModel: PlaylistModel) {
        viewModelScope.launch {
            renamePlaylistUseCase(playlistModel)
        }
    }

    fun deletePlaylist(playlistModel: PlaylistModel) {
        viewModelScope.launch {
            deletePlaylistUseCase(playlistModel)
        }
    }

    fun createPlaylistSongCrossRef(playlistId: Long, songId: String?) {
        if (songId != null) {
            viewModelScope.launch(Dispatchers.IO) {
                val result = addSongToPlaylistUseCase(playlistId, songId)
                _addResult.postValue(result)
            }
        } else {
            _addResult.postValue(AddSongResult.Error)
        }
    }

    companion object {
        private const val LIMIT = 10
    }
}