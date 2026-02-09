package pro.branium.core_ui.model

sealed class SearchUiState {
    object Idle : SearchUiState()
    data class Typing(val keyword: String) : SearchUiState()
    data class Results(val keyword: String) : SearchUiState()
}