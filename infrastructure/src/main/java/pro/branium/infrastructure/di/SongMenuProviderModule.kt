package pro.branium.infrastructure.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import pro.branium.core_ui.menu.SongMenuProvider
import pro.branium.feature_favorite.ui.menu.FavoriteMenuProvider
import pro.branium.feature_song.ui.menu.provider.BlockMenuProvider
import pro.branium.feature_song.ui.menu.provider.DownloadMenuProvider
import pro.branium.feature_song.ui.menu.provider.PlaylistMenuProvider
import pro.branium.feature_song.ui.menu.provider.ReportMenuProvider
import pro.branium.feature_song.ui.menu.provider.ViewAlbumMenuProvider
import pro.branium.feature_song.ui.menu.provider.ViewArtistMenuProvider
import pro.branium.feature_song.ui.menu.provider.ViewSongMenuProvider

@Module
@InstallIn(SingletonComponent::class)
abstract class SongMenuProviderModule {
    @Binds
    @IntoSet
    abstract fun bindBlockMenuProvider(provider: BlockMenuProvider): SongMenuProvider

    @Binds
    @IntoSet
    abstract fun bindDownloadMenuProvider(provider: DownloadMenuProvider): SongMenuProvider

    @Binds
    @IntoSet
    abstract fun bindFavoriteMenuProvider(provider: FavoriteMenuProvider): SongMenuProvider

    @Binds
    @IntoSet
    abstract fun bindPlaylistMenuProvider(provider: PlaylistMenuProvider): SongMenuProvider

    @Binds
    @IntoSet
    abstract fun bindReportMenuProvider(provider: ReportMenuProvider): SongMenuProvider

    @Binds
    @IntoSet
    abstract fun bindViewAlbumMenuProvider(provider: ViewAlbumMenuProvider): SongMenuProvider

    @Binds
    @IntoSet
    abstract fun bindViewArtistMenuProvider(provider: ViewArtistMenuProvider): SongMenuProvider

    @Binds
    @IntoSet
    abstract fun bindSongMenuProvider(provider: ViewSongMenuProvider): SongMenuProvider
}