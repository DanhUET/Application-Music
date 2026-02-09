package pro.branium.infrastructure.playback

import pro.branium.core_domain.manager.QueueManager
import javax.inject.Inject

class QueueManagerImpl @Inject constructor() : QueueManager {
    override fun addToQueue(songId: String) {
        // todo
    }
}