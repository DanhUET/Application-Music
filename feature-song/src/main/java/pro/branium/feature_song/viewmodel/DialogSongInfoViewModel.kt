package pro.branium.feature_song.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pro.branium.core_model.SongModel
import pro.branium.feature_song.domain.usecase.GetSongByIdUseCase
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DialogSongInfoViewModel @Inject constructor(
    private val getSongByIdUseCase: GetSongByIdUseCase
) : ViewModel() {
    private val _songModel = MutableLiveData<SongModel?>()

    val songModel: MutableLiveData<SongModel?>
        get() = _songModel

    fun getSongInfo(songId: String) {
        viewModelScope.launch {
            val song = getSongByIdUseCase(songId)
            _songModel.postValue(song)
        }
    }

    fun toTimeLabel(duration: Int): String {
        val min = duration / 60
        val sec = duration % 60
        return String.format(Locale.getDefault(), "%02d:%02d", min, sec)
    }
}