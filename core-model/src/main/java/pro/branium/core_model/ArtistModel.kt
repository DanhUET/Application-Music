package pro.branium.core_model

data class ArtistModel(
    val id: Int,
    val name: String,
    val avatar: String?,
    val interested: Int = 0
)