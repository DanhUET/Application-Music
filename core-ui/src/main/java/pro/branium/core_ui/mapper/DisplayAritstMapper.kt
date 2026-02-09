package pro.branium.core_ui.mapper

import pro.branium.core_ui.model.DisplayArtistModel
import pro.branium.core_model.ArtistModel


fun DisplayArtistModel.toModel(): ArtistModel {
    return this.artist
}

fun ArtistModel.toDisplayModel(isFavoriteByUser: Boolean = false): DisplayArtistModel {
    return DisplayArtistModel(this, isFavoriteByUser)
}

fun List<DisplayArtistModel>.toSongModels(): List<ArtistModel> {
    return this.map { it.artist }
}

fun List<ArtistModel>.toDisplayModels(): List<DisplayArtistModel> {
    return this.map { it.toDisplayModel(false) }
}