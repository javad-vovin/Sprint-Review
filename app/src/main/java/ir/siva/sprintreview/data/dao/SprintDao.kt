package ir.siva.sprintreview.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ir.siva.sprintreview.data.model.Sprint
import kotlinx.coroutines.flow.Flow

@Dao
interface SprintDao {
    @Query("SELECT * FROM sprints ORDER BY sprintNumber DESC")
    fun getAllSprints(): Flow<List<Sprint>>

    @Query("SELECT * FROM sprints WHERE id = :sprintId")
    fun getSprintById(sprintId: Long): Flow<Sprint?>

    @Query("SELECT * FROM sprints WHERE isCurrent = 1 LIMIT 1")
    fun getCurrentSprint(): Flow<Sprint?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSprint(sprint: Sprint): Long

    @Update
    suspend fun updateSprint(sprint: Sprint)

    @Delete
    suspend fun deleteSprint(sprint: Sprint)

    @Query("UPDATE sprints SET isCurrent = 0")
    suspend fun clearCurrentSprint()

    @Query("UPDATE sprints SET isCurrent = 1 WHERE id = :sprintId")
    suspend fun setCurrentSprint(sprintId: Long)

    @Query("UPDATE sprints SET name = :newName WHERE id = :sprintId")
    suspend fun updateSprintName(sprintId: Long, newName: String)
}
