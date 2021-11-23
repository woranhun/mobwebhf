package hu.bme.aut.android.mattermostremindus.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todoitem")
data class TodoItem(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "subject") var subject: String,
    @ColumnInfo(name = "message") var message: String,
    @ColumnInfo(name = "sendTo") var sendTo: String,
    @ColumnInfo(name = "periodInMs") var periodInMs: Long,
    @ColumnInfo(name = "nextSendInMs") var nextSendInMs: Long,
    @ColumnInfo(name = "previousSendInMs") var previousSendInMs: Long,
    @ColumnInfo(name = "isOn") var isOn: Boolean,
) {
}
