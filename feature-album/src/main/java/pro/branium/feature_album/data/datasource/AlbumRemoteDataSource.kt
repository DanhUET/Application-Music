package pro.branium.feature_album.data.datasource

import pro.branium.core_network.dto.album.AlbumDto
import pro.branium.core_network.dto.paging_param.PagingParamRequest
import pro.branium.core_network.dto.song.SongListDto


interface AlbumRemoteDataSource {
    suspend fun loadAlbumPaging(param: PagingParamRequest): List<AlbumDto>

    suspend fun getAlbumById(albumId: Int): AlbumDto?

    suspend fun getSongsForAlbum(albumId: Int): SongListDto
}