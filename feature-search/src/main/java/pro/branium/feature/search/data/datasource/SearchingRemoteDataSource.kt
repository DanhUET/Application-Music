package pro.branium.feature.search.data.datasource

import pro.branium.core_network.dto.song.SongDto

interface SearchingRemoteDataSource {
    suspend fun search(query: String): List<SongDto>
}