package ir.siva.sprintreview.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ir.siva.sprintreview.data.model.ReviewRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewRecordDao {
    @Query("SELECT * FROM review_records WHERE sprintId = :sprintId ORDER BY memberName ASC")
    fun getRecordsForSprint(sprintId: Long): Flow<List<ReviewRecord>>

    @Query("SELECT * FROM review_records WHERE id = :recordId")
    suspend fun getRecordById(recordId: Long): ReviewRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: ReviewRecord): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecords(records: List<ReviewRecord>)

    @Update
    suspend fun updateRecord(record: ReviewRecord)

    @Delete
    suspend fun deleteRecord(record: ReviewRecord)

    @Query("DELETE FROM review_records WHERE id = :recordId")
    suspend fun deleteRecordById(recordId: Long)

    @Query("UPDATE review_records SET r1Count = r1Count + 1, totalReviews = totalReviews + 1 WHERE id = :recordId")
    suspend fun incrementR1(recordId: Long)

    @Query("UPDATE review_records SET r1Count = CASE WHEN r1Count > 0 THEN r1Count - 1 ELSE 0 END, totalReviews = CASE WHEN r1Count > 0 AND totalReviews > 0 THEN totalReviews - 1 ELSE totalReviews END WHERE id = :recordId")
    suspend fun decrementR1(recordId: Long)

    @Query("UPDATE review_records SET r2Count = r2Count + 1, totalReviews = totalReviews + 1 WHERE id = :recordId")
    suspend fun incrementR2(recordId: Long)

    @Query("UPDATE review_records SET r2Count = CASE WHEN r2Count > 0 THEN r2Count - 1 ELSE 0 END, totalReviews = CASE WHEN r2Count > 0 AND totalReviews > 0 THEN totalReviews - 1 ELSE totalReviews END WHERE id = :recordId")
    suspend fun decrementR2(recordId: Long)
}
