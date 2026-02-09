package pro.branium.feature_song.ui.menu

import pro.branium.core_model.SongModel
import pro.branium.core_ui.menu.SongMenuItem
import pro.branium.core_ui.menu.SongMenuProvider
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Tập hợp các menu từ nhiều module
 */
@Singleton
class SongMenuAggregator @Inject constructor(
    private val providers: Set<@JvmSuppressWildcards SongMenuProvider>
) {
    fun getMenuFor(song: SongModel): List<SongMenuItem> {
        return providers.map { it.provide(song) }
    }
}