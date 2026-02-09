package pro.branium.core_model

data class PermissionState(
    val asked: Boolean = false,
    val granted: Boolean = false,
    val userTriggered: Boolean = false
)