package pro.branium.infrastructure.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import pro.branium.feature_song.action.SongActionHandler
import pro.branium.feature_song.action.handler.AddToFavoriteActionHandler
import pro.branium.feature_song.action.handler.AddToPlaylistActionHandler
import pro.branium.feature_song.action.handler.AddToQueueActionHandler
import pro.branium.feature_song.action.handler.BlockSongActionHandler
import pro.branium.feature_song.action.handler.DownloadActionHandler
import pro.branium.feature_song.action.handler.ReportErrorActionHandler
import pro.branium.feature_song.action.handler.ViewAlbumActionHandler
import pro.branium.feature_song.action.handler.ViewArtistActionHandler
import pro.branium.feature_song.action.handler.ViewSongActionHandler
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SongActionHandlerModule {
    @Binds
    @IntoSet
    abstract fun bindViewSongActionHandler(handler: ViewSongActionHandler): SongActionHandler

    @Binds
    @IntoSet
    abstract fun bindViewArtistActionHandler(handler: ViewArtistActionHandler): SongActionHandler

    @Binds
    @IntoSet
    abstract fun bindViewAlbumActionHandler(handler: ViewAlbumActionHandler): SongActionHandler

    @Binds
    @IntoSet
    abstract fun bindAddToFavoriteActionHandler(handler: AddToFavoriteActionHandler): SongActionHandler

    @Binds
    @IntoSet
    @Singleton
    abstract fun bindAddToPlaylistActionHandler(handler: AddToPlaylistActionHandler): SongActionHandler

    @Binds
    @IntoSet
    abstract fun bindAddToQueueActionHandler(handler: AddToQueueActionHandler): SongActionHandler

    @Binds
    @IntoSet
    abstract fun bindBlockSongActionHandler(handler: BlockSongActionHandler): SongActionHandler

    @Binds
    @IntoSet
    abstract fun bindDownloadActionHandler(handler: DownloadActionHandler): SongActionHandler

    @Binds
    @IntoSet
    abstract fun bindReportActionHandler(handler: ReportErrorActionHandler): SongActionHandler
}