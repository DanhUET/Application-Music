package pro.branium.feature_song.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pro.branium.core_ui.mapper.toSongModels
import pro.branium.core_ui.model.DisplaySongModel
import pro.branium.feature_song.domain.usecase.InsertNewSongUseCase
import javax.inject.Inject

class SongViewModel @Inject constructor(
    private val insertNewSongUseCase: InsertNewSongUseCase,
) : ViewModel() {
    fun insertNewSong(songModels: List<DisplaySongModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            insertNewSongUseCase(*songModels.toSongModels().toTypedArray())
        }
    }
}