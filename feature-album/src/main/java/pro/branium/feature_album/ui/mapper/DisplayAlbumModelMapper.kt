package pro.branium.feature_album.ui.mapper

import pro.branium.core_model.AlbumModel
import pro.branium.core_ui.model.DisplayAlbumModel

fun AlbumModel.toDisplayAlbumModel(isCareAbout: Boolean = false): DisplayAlbumModel {
    return DisplayAlbumModel(this, isCareAbout)
}

fun DisplayAlbumModel.toModel(): AlbumModel {
    return this.album
}

fun List<AlbumModel>.toDisplayModels(): List<DisplayAlbumModel> {
    return this.map { it.toDisplayAlbumModel() }
}

fun List<DisplayAlbumModel>.toModels(): List<AlbumModel> {
    return this.map { it.toModel() }
}