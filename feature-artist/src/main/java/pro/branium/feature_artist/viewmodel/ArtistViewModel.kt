package pro.branium.feature_artist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import pro.branium.core_model.ArtistModel
import pro.branium.core_ui.model.DisplayArtistModel
import pro.branium.feature_artist.domain.usecase.GetLimitedArtistsUseCase
import pro.branium.feature_artist.domain.usecase.GetPaginatedArtistUseCase
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    getLimitedArtistsUseCase: GetLimitedArtistsUseCase,
    getPaginatedArtistUseCase: GetPaginatedArtistUseCase
) : ViewModel() {
    private val limitedArtistModelFlow =
        getLimitedArtistsUseCase(PAGE_SIZE).cachedIn(viewModelScope)
    private val limitedFavoriteArtistFlow = MutableStateFlow<List<ArtistModel>>(emptyList())
    private val _artistFlow = getPaginatedArtistUseCase().cachedIn(viewModelScope)

    val limitedArtistFlow =
        combine(limitedArtistModelFlow, limitedFavoriteArtistFlow) { pagingData, artists ->
            pagingData.map { artist ->
                DisplayArtistModel(artist, false)
            }
        }.cachedIn(viewModelScope)
    val artistFlow = combine(_artistFlow, limitedFavoriteArtistFlow) { pagingData, artists ->
        pagingData.map { artist ->
            DisplayArtistModel(artist, false)
        }
    }.cachedIn(viewModelScope)

    companion object {
        const val PAGE_SIZE = 20
    }
}