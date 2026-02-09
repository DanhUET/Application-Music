package pro.branium.core_utils.generator

object PlaylistIdGenerator {
    // ID cho playlist người dùng
    private var nextUserPlaylistId: Long = 10000

    fun init(startId: Long) {
        nextUserPlaylistId = startId
    }

    fun newUserPlaylistId(): Long = ++nextUserPlaylistId
}