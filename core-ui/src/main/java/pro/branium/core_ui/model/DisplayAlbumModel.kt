package pro.branium.core_ui.model

import pro.branium.core_model.AlbumModel

data class DisplayAlbumModel(
    val album: AlbumModel,
    val isCareAbout: Boolean = false
)
