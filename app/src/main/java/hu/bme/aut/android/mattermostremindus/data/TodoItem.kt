package hu.bme.aut.android.mattermostremindus.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

@Entity(tableName = "todoitem")
data class TodoItem(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "subject") var subject: String,
    @ColumnInfo(name = "message") var message: String,
    @ColumnInfo(name = "sendTo") var sendTo: String,
    @ColumnInfo(name = "periodInMs") var periodInMs: Long,
    @ColumnInfo(name = "nextSendInMs") var nextSendInMs: Long,
    @ColumnInfo(name = "previousSendInMs") var previousSendInMs: Long,
    @ColumnInfo(name = "intervalMultiplier") var intervalMultiplier: IntervalMultiplier,
    @ColumnInfo(name = "isOn") var isOn: Boolean,
) {
    enum class IntervalMultiplier {
        Seconds, Minutes, Hours, Days, Weeks, Years;

        companion object {
            @JvmStatic
            @TypeConverter
            fun getByOrdinal(ordinal: Long): IntervalMultiplier? {
                var ret: IntervalMultiplier? = null
                when (ordinal) {
                    1000L -> ret = Seconds
                    1000L * 60 -> ret = Minutes
                    1000L * 60 * 60 -> ret = Hours
                    1000L * 60 * 60 * 24 -> ret = Days
                    1000L * 60 * 60 * 24 * 7 -> ret = Weeks
                    1000L * 60 * 60 * 24 * 365 -> ret = Years
                }

                return ret
            }

            @JvmStatic
            @TypeConverter
            fun toLong(mul: IntervalMultiplier): Long {
                var ret: Long? = null
                ret = when (mul) {
                    Seconds -> 1000L
                    Minutes -> 1000L * 60
                    Hours -> 1000L * 60 * 60
                    Days -> 1000L * 60 * 60 * 24
                    Weeks -> 1000L * 60 * 60 * 24 * 7
                    Years -> 1000L * 60 * 60 * 24 * 365
                }

                return ret
            }
        }
    }
}
