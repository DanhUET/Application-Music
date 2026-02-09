package pro.branium.feature.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pro.branium.core_model.PlaylistModel
import pro.branium.core_model.SongModel
import pro.branium.core_ui.mapper.toDisplayModels
import pro.branium.core_ui.model.DisplaySongModel
import pro.branium.core_ui.model.SearchUiState
import pro.branium.core_utils.MusicAppUtils.defaultPlaylists
import pro.branium.feature.search.domain.model.HistorySearchedKeyModel
import pro.branium.feature.search.domain.usecase.ClearHistorySearchedKeysUseCase
import pro.branium.feature.search.domain.usecase.ClearHistorySearchedSongsUseCase
import pro.branium.feature.search.domain.usecase.DeleteHistorySearchedSongUseCase
import pro.branium.feature.search.domain.usecase.GetHistorySearchedKeysUseCase
import pro.branium.feature.search.domain.usecase.GetHistorySearchedSongsUseCase
import pro.branium.feature.search.domain.usecase.InsertHistorySearchedKeyUseCase
import pro.branium.feature.search.domain.usecase.InsertHistorySearchedSongUseCase
import pro.branium.feature.search.domain.usecase.InsertUserHistorySearchedKeyUseCase
import pro.branium.feature.search.domain.usecase.RemoveHistorySearchedKeyUseCase
import pro.branium.feature.search.domain.usecase.SearchSongUseCase
import javax.inject.Inject

@HiltViewModel
class SearchingViewModel @Inject constructor(
    private val getHistorySearchedKeysUseCase: GetHistorySearchedKeysUseCase,
    private val getHistorySearchedSongsUseCase: GetHistorySearchedSongsUseCase,
    private val clearHistorySearchedKeysUseCase: ClearHistorySearchedKeysUseCase,
    private val clearHistorySearchedSongsUseCase: ClearHistorySearchedSongsUseCase,
    private val deleteHistorySearchedSongUseCase: DeleteHistorySearchedSongUseCase,
    private val insertHistorySearchedSongUseCase: InsertHistorySearchedSongUseCase,
    private val insertHistorySearchedKeysUseCase: InsertHistorySearchedKeyUseCase,
    private val insertUserHistorySearchedKeyUseCase: InsertUserHistorySearchedKeyUseCase,
    private val searchSongUseCase: SearchSongUseCase,
    private val removeHistorySearchedKeyUseCase: RemoveHistorySearchedKeyUseCase
) : ViewModel() {
    private val _songs = MutableLiveData<List<DisplaySongModel>>()
    private val _searchedKey = MutableLiveData<String>()
    private val _historySearchedKeys = MutableLiveData<List<HistorySearchedKeyModel>>()
    private val _historySearchedSongs = MutableLiveData<List<DisplaySongModel>>()
    private val _searchUiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    private val _shouldSaveSearchedKey = MutableStateFlow(false)

    val searchUiState: StateFlow<SearchUiState> = _searchUiState
    val shouldSaveSearchedKey: StateFlow<Boolean> = _shouldSaveSearchedKey

    val songs: LiveData<List<DisplaySongModel>>
        get() = _songs
    val historySearchedKeysEntity: LiveData<List<HistorySearchedKeyModel>> =
        _historySearchedKeys
    val searchedKey: LiveData<String>
        get() = _searchedKey
    val historySearchedSongs: LiveData<List<DisplaySongModel>> = _historySearchedSongs
    var searchedPlaylist = defaultPlaylists[6]!!
    var historySearchedPlaylist = defaultPlaylists[7]!!

    init {
        loadHistorySearchedKeys()
        loadHistorySearchedSongs()
    }

    fun shouldSaveSearchedKey(state: Boolean = false) {
        _shouldSaveSearchedKey.value = state
    }

    fun onSearchQueryChanged(query: String) {
        if (query.isEmpty()) {
            _searchUiState.value = SearchUiState.Idle
        } else {
            _searchUiState.value = SearchUiState.Typing(query)
        }
    }

    fun onSearchConfirmed(query: String) {
        _searchUiState.value = SearchUiState.Results(query)
    }

    private fun loadHistorySearchedSongs() {
        viewModelScope.launch(Dispatchers.IO) {
            getHistorySearchedSongsUseCase().collect { songs ->
                val historySongs = songs.toDisplayModels()
                _historySearchedSongs.postValue(historySongs)
                historySearchedPlaylist = historySearchedPlaylist.copy(songs = songs)
            }
        }
    }

    private fun loadHistorySearchedKeys() {
        viewModelScope.launch(Dispatchers.IO) {
            getHistorySearchedKeysUseCase().collect { historyKeys ->
                _historySearchedKeys.postValue(historyKeys)
            }
        }
    }

    fun search(key: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val keyToSearch = "%${key.trim()}%"
            val result = searchSongUseCase(keyToSearch)
            val displaySongs = result.toDisplayModels()
            _songs.postValue(displaySongs)
            searchedPlaylist = PlaylistModel(songs = result)
        }
    }

    fun insertSearchedKey(key: String) {
        if (key.isEmpty()) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val keyId = insertHistorySearchedKeysUseCase(key)
            insertUserHistorySearchedKeyUseCase(keyId)
        }
    }

    fun insertSearchedSongs(songModel: SongModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val songId = songModel.id
            insertHistorySearchedSongUseCase(songId)
        }
    }

    fun setSelectedKey(key: String) {
        _searchedKey.value = key
    }

    fun deleteSearchedSong(songModel: SongModel) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteHistorySearchedSongUseCase(songId = songModel.id)
        }
    }

    fun clearAllKeys() {
        viewModelScope.launch(Dispatchers.IO) {
            clearHistorySearchedKeysUseCase()
        }
    }

    fun removeSearchKey(key: String) {
        viewModelScope.launch {
            removeHistorySearchedKeyUseCase(key)
        }
    }

    fun clearAllSongs() {
        viewModelScope.launch(Dispatchers.IO) {
            clearHistorySearchedSongsUseCase()
        }
    }
}