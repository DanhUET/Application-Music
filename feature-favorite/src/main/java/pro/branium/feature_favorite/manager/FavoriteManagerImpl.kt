package pro.branium.feature_favorite.manager

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import pro.branium.core_domain.manager.FavoriteManager
import pro.branium.feature_favorite.domain.usecase.AddFavoriteStatusUseCase
import pro.branium.feature_favorite.domain.usecase.DeleteFavoriteStatusUseCase
import pro.branium.feature_favorite.domain.usecase.IsFavoriteSongUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteManagerImpl @Inject constructor(
    private val addFavoriteStatusUseCase: AddFavoriteStatusUseCase,
    private val deleteFavoriteStatusUseCase: DeleteFavoriteStatusUseCase,
    private val isFavoriteSongUseCase: IsFavoriteSongUseCase
) : FavoriteManager {

    private val _songId = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val isFavorite: StateFlow<Boolean> =
        _songId.filterNotNull()
            .flatMapLatest { songId -> isFavoriteSongUseCase(songId) }
            .stateIn(
                scope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
                started = SharingStarted.Companion.WhileSubscribed(5000),
                initialValue = false
            )

    override fun setFavoriteSongId(newSongId: String?) {
        _songId.value = newSongId
    }

    override suspend fun toggleFavorite() {
        val songId = _songId.value ?: return
        val current = isFavorite.value
        if (current) {
            deleteFavoriteStatusUseCase(songId)
        } else {
            addFavoriteStatusUseCase(songId)
        }
    }
}