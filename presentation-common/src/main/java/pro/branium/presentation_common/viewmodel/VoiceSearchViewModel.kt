package pro.branium.presentation_common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VoiceSearchViewModel @Inject constructor() : ViewModel() {
    private val _voiceSearchQuery = MutableLiveData<String>()
    private val _voiceSearchTrigger = MutableLiveData<Boolean>()
    private val _isRecordAudioPermissionGranted = MutableLiveData<Boolean>()
    private val _isUserTriggered = MutableLiveData<Boolean>()

    val voiceSearchQuery: LiveData<String> = _voiceSearchQuery
    val voiceSearchTrigger: LiveData<Boolean> = _voiceSearchTrigger
    val isRecordAudioPermissionGranted: LiveData<Boolean> = _isRecordAudioPermissionGranted
    val isUserTriggered: LiveData<Boolean> = _isUserTriggered

    fun triggerVoiceSearch(state: Boolean) {
        _isUserTriggered.value = state
        _voiceSearchTrigger.value = state
    }

    fun setVoiceSearchQuery(query: String) {
        _voiceSearchQuery.value = query
    }

    fun setRecordAudioPermissionGranted(state: Boolean) {
        _isRecordAudioPermissionGranted.value = state
    }

    fun disableUserTriggered() {
        _isUserTriggered.value = false
    }
}