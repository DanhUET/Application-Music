package pro.branium.feature.search.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "history_searched_keys",
    indices = [Index(value = ["key"], unique = true)]
)
data class HistorySearchedKeyEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0,

    @ColumnInfo(name = "key")
    var key: String = "",
)