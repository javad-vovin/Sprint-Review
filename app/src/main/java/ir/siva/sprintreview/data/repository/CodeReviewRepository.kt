package ir.siva.sprintreview.data.repository

import ir.siva.sprintreview.data.dao.ReviewRecordDao
import ir.siva.sprintreview.data.dao.SprintDao
import ir.siva.sprintreview.data.model.ReviewRecord
import ir.siva.sprintreview.data.model.Sprint
import kotlinx.coroutines.flow.Flow

class CodeReviewRepository(
    private val sprintDao: SprintDao,
    private val reviewRecordDao: ReviewRecordDao
) {
    val allSprints: Flow<List<Sprint>> = sprintDao.getAllSprints()
    val currentSprint: Flow<Sprint?> = sprintDao.getCurrentSprint()

    fun getRecordsForSprint(sprintId: Long): Flow<List<ReviewRecord>> {
        return reviewRecordDao.getRecordsForSprint(sprintId)
    }

    suspend fun createNewSprint(name: String, startDate: Long, endDate: Long, sprintNumber: Int): Long {
        sprintDao.clearCurrentSprint()
        val sprint = Sprint(
            name = name,
            sprintNumber = sprintNumber,
            startDate = startDate,
            endDate = endDate,
            isCurrent = true
        )
        return sprintDao.insertSprint(sprint)
    }

    suspend fun selectSprint(sprintId: Long) {
        sprintDao.clearCurrentSprint()
        sprintDao.setCurrentSprint(sprintId)
    }

    suspend fun incrementR1(recordId: Long) {
        reviewRecordDao.incrementR1(recordId)
    }

    suspend fun decrementR1(recordId: Long) {
        reviewRecordDao.decrementR1(recordId)
    }

    suspend fun incrementR2(recordId: Long) {
        reviewRecordDao.incrementR2(recordId)
    }

    suspend fun decrementR2(recordId: Long) {
        reviewRecordDao.decrementR2(recordId)
    }

    suspend fun addMemberToSprint(sprintId: Long, memberName: String, avatarColorHex: String): Long {
        val record = ReviewRecord(
            sprintId = sprintId,
            memberName = memberName,
            avatarColorHex = avatarColorHex,
            r1Count = 0,
            r2Count = 0,
            totalReviews = 0
        )
        return reviewRecordDao.insertRecord(record)
    }

    suspend fun updateMemberRecord(record: ReviewRecord) {
        reviewRecordDao.updateRecord(record)
    }

    suspend fun deleteMemberRecord(recordId: Long) {
        reviewRecordDao.deleteRecordById(recordId)
    }

    suspend fun updateSprintName(sprintId: Long, newName: String) {
        sprintDao.updateSprintName(sprintId, newName)
    }
}
