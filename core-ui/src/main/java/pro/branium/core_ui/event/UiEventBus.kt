package pro.branium.core_ui.event

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import pro.branium.core_model.event.UiEvent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UiEventBus @Inject constructor() {
    private val _events = MutableSharedFlow<UiEvent>()
    val events: SharedFlow<UiEvent> = _events

    suspend fun emit(event: UiEvent) {
        _events.emit(event)
    }
}