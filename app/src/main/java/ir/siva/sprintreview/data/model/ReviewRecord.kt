package ir.siva.sprintreview.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "review_records")
data class ReviewRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sprintId: Long,
    val memberName: String,
    val avatarColorHex: String = "#38BDF8",
    val r1Count: Int = 0,
    val r2Count: Int = 0,
    val totalReviews: Int = 0
)
