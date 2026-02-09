package pro.branium.feature_song.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pro.branium.core_model.SongModel
import pro.branium.feature_song.domain.usecase.GetSongByIdUseCase
import javax.inject.Inject

@HiltViewModel
class SongMenuViewModel @Inject constructor(
    private val getSongByIdUseCase: GetSongByIdUseCase
) : ViewModel() {
    private val _songModel = MutableLiveData<SongModel>()
    private val _playlistName = MutableLiveData<String>()

    val songModel: MutableLiveData<SongModel> = _songModel
    val playlistName: MutableLiveData<String> = _playlistName

    fun setSelectedSong(song: SongModel) {
        _songModel.value = song
    }

    fun getSong(songId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val song = getSongByIdUseCase(songId = songId)
            song?.let {
                _songModel.postValue(song)
            }
        }
    }
}