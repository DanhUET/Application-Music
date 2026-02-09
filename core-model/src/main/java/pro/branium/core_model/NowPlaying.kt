package pro.branium.core_model

data class NowPlaying(
    val id: String?,
    val isPlaying: Boolean,
    val positionMs: Long = 0,
    val durationMs: Long = 0
)