package ir.siva.sprintreview.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ir.siva.sprintreview.data.dao.ReviewRecordDao
import ir.siva.sprintreview.data.dao.SprintDao
import ir.siva.sprintreview.data.model.ReviewRecord
import ir.siva.sprintreview.data.model.Sprint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Sprint::class, ReviewRecord::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sprintDao(): SprintDao
    abstract fun reviewRecordDao(): ReviewRecordDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sprint_review_database"
                )
                    .addCallback(AppDatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback(
        private val context: Context
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateInitialData(database.sprintDao(), database.reviewRecordDao())
                }
            }
        }

        private suspend fun populateInitialData(sprintDao: SprintDao, reviewRecordDao: ReviewRecordDao) {
            val now = System.currentTimeMillis()
            val twoWeeks = 14 * 24 * 60 * 60 * 1000L

            val sprint1 = Sprint(
                name = "Sprint 1",
                sprintNumber = 1,
                startDate = now,
                endDate = now + twoWeeks,
                isCurrent = true
            )
            val sprintId = sprintDao.insertSprint(sprint1)

            val initialMembers = listOf(
                ReviewRecord(sprintId = sprintId, memberName = "Ali Ahmadi", avatarColorHex = "#38BDF8", r1Count = 5, r2Count = 3, totalReviews = 8),
                ReviewRecord(sprintId = sprintId, memberName = "Sara Mohammadi", avatarColorHex = "#F43F5E", r1Count = 8, r2Count = 6, totalReviews = 14),
                ReviewRecord(sprintId = sprintId, memberName = "Reza Hosseini", avatarColorHex = "#10B981", r1Count = 3, r2Count = 4, totalReviews = 7),
                ReviewRecord(sprintId = sprintId, memberName = "Maryam Karimi", avatarColorHex = "#F59E0B", r1Count = 6, r2Count = 2, totalReviews = 8),
                ReviewRecord(sprintId = sprintId, memberName = "Mehdi Nouri", avatarColorHex = "#8B5CF6", r1Count = 4, r2Count = 5, totalReviews = 9)
            )
            reviewRecordDao.insertRecords(initialMembers)
        }
    }
}
