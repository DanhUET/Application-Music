package pro.branium.presentation_common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import pro.branium.core_network.NetworkMonitor
import javax.inject.Inject

@HiltViewModel
class NetworkStateViewModel @Inject constructor(
    networkMonitor: NetworkMonitor
) : ViewModel() {
    val isNetworkAvailable: LiveData<Boolean> = networkMonitor.isConnected.asLiveData()
}