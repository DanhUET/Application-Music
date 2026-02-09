package pro.branium.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pro.branium.core_playback.usecase.GetPlaybackStateUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPlaybackStateUseCase: GetPlaybackStateUseCase
) : ViewModel() {

    fun getPlaybackState() = getPlaybackStateUseCase()
}