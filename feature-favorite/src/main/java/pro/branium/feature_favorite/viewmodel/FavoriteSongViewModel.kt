package pro.branium.feature_favorite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pro.branium.feature_favorite.domain.usecase.AddFavoriteStatusUseCase
import pro.branium.feature_favorite.domain.usecase.DeleteFavoriteStatusUseCase
import pro.branium.feature_favorite.domain.usecase.IsFavoriteSongUseCase
import javax.inject.Inject

@HiltViewModel
class FavoriteSongViewModel @Inject constructor(
    private val addFavoriteStatusUseCase: AddFavoriteStatusUseCase,
    private val deleteFavoriteStatusUseCase: DeleteFavoriteStatusUseCase,
    private val isFavoriteSongUseCase: IsFavoriteSongUseCase
) : ViewModel() {
    private val _songId = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val isFavorite: StateFlow<Boolean> = _songId
        .filterNotNull()
        .flatMapLatest { songId ->
            isFavoriteSongUseCase(songId)
        }.stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), false)


    fun toggleFavorite() {
        val status = isFavorite.value
        _songId.value?.let { songId ->
            viewModelScope.launch(Dispatchers.IO) {
                if (!status) {
                    addFavoriteStatusUseCase(songId)
                } else {
                    deleteFavoriteStatusUseCase(songId)
                }
            }
        }
    }

    fun setFavoriteSongId(newSongId: String?) {
        _songId.value = newSongId
    }
}