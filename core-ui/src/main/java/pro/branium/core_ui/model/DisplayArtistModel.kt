package pro.branium.core_ui.model

import pro.branium.core_model.ArtistModel


data class DisplayArtistModel(
    val artist: ArtistModel,
    var isCareAbout: Boolean = false
)
