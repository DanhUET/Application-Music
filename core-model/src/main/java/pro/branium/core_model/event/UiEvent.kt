package pro.branium.core_model.event

sealed class UiEvent {
    data class ShowMessage(val message: String) : UiEvent()
}