package pro.branium.core_domain.manager

interface QueueManager {
    fun addToQueue(songId: String)
}