package pro.branium.feature.player.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.media3.common.C
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NowPlayingViewModel @Inject constructor() : ViewModel() {
    private val _isPlaying = MutableLiveData<Boolean>()

    val isPlaying: LiveData<Boolean> get() = _isPlaying
    var currentUserId: Int = 0

    fun setIsPlaying(isPlaying: Boolean) {
        _isPlaying.value = isPlaying
    }

    fun getDuration(duration: Long): Int {
        if (duration == C.TIME_UNSET) {
            return 300 * 1000
        }
        return duration.toInt()
    }
}