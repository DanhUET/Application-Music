package pro.branium.feature_favorite.ui.menu

import pro.branium.core_resources.R
import pro.branium.core_domain.repository.FavoriteRepository
import pro.branium.core_model.SongModel
import pro.branium.core_model.action.SongAction
import pro.branium.core_ui.menu.SongMenuItem
import pro.branium.core_ui.menu.SongMenuProvider
import javax.inject.Inject

class FavoriteMenuProvider @Inject constructor(
    private val repo: FavoriteRepository
) : SongMenuProvider {
    override fun provide(song: SongModel): SongMenuItem {
        val isFavorite = false // todo repo.isFavorite(song.id).first()
        return SongMenuItem(
            id = "favorite",
            icon = R.drawable.ic_favorite,
            textRes = R.string.item_favorited,
            stateIcon = if (isFavorite) R.drawable.ic_check else null,
            action = SongAction.AddToFavorite(song.id)
        )
    }
}