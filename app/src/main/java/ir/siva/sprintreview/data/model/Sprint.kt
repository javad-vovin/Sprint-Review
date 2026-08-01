package ir.siva.sprintreview.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sprints")
data class Sprint(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val sprintNumber: Int,
    val startDate: Long,
    val endDate: Long,
    val isCurrent: Boolean = false
)
