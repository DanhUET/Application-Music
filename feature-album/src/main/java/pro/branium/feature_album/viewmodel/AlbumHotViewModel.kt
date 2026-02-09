package pro.branium.feature_album.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pro.branium.feature_album.domain.model.AlbumSummaryModel
import pro.branium.feature_album.domain.usecase.GetLimitedAlbumsUseCase
import pro.branium.feature_album.domain.usecase.GetPaginatedAlbumsUseCase
import pro.branium.feature_album.domain.usecase.GetTopAlbumUseCase
import javax.inject.Inject

@HiltViewModel
class AlbumHotViewModel @Inject constructor(
    getLimitedAlbumsUseCase: GetLimitedAlbumsUseCase,
    getPaginatedAlbumsUseCase: GetPaginatedAlbumsUseCase,
    private val getTopAlbumUseCase: GetTopAlbumUseCase
) : ViewModel() {
    private val _topAlbums = MutableStateFlow<List<AlbumSummaryModel>>(emptyList())

    val topAlbums: StateFlow<List<AlbumSummaryModel>> = _topAlbums
    val limitedAlbumFlow = getLimitedAlbumsUseCase(PAGE_SIZE).cachedIn(viewModelScope)

    val moreAlbumFlow = getPaginatedAlbumsUseCase().cachedIn(viewModelScope)

    init {
        loadTopAlbums()
    }

    fun loadTopAlbums() {
        viewModelScope.launch(Dispatchers.IO) {
            val topAlbums = getTopAlbumUseCase(PAGE_SIZE)
            _topAlbums.value = topAlbums
        }
    }

    companion object {
        const val PAGE_SIZE = 10
    }
}