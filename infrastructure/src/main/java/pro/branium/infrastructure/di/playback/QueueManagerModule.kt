package pro.branium.infrastructure.di.playback

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pro.branium.core_domain.manager.QueueManager
import pro.branium.infrastructure.playback.QueueManagerImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class QueueManagerModule {
    @Binds
    abstract fun bindQueueManager(impl: QueueManagerImpl): QueueManager
}