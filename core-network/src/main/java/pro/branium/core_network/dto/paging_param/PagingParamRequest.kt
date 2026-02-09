package pro.branium.core_network.dto.paging_param

import com.google.gson.annotations.SerializedName

data class PagingParamRequest(
    @SerializedName("offset") val offset: Int,
    @SerializedName("limit") val limit: Int
)