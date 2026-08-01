package ir.siva.sprintreview.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ir.siva.sprintreview.data.model.ReviewRecord
import ir.siva.sprintreview.data.model.Sprint
import ir.siva.sprintreview.data.repository.CodeReviewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class MemberSortFilter {
    DEFAULT,
    HIGHEST_TOTAL,
    HIGHEST_R1,
    HIGHEST_R2
}

data class SprintAnalytics(
    val totalR1: Int = 0,
    val totalR2: Int = 0,
    val totalCombined: Int = 0,
    val topReviewerName: String = "-",
    val avgReviewsPerMember: Float = 0f,
    val avgR1PerMember: Float = 0f,
    val avgR2PerMember: Float = 0f,
    val teamMemberCount: Int = 0,
    val prevTotalR1: Int = 0,
    val prevTotalR2: Int = 0,
    val prevTotalCombined: Int = 0
)

data class DashboardUiState(
    val isLoading: Boolean = false,
    val sprints: List<Sprint> = emptyList(),
    val selectedSprint: Sprint? = null,
    val previousSprint: Sprint? = null,
    val records: List<ReviewRecord> = emptyList(),
    val previousSprintRecords: List<ReviewRecord> = emptyList(),
    val filteredRecords: List<ReviewRecord> = emptyList(),
    val analytics: SprintAnalytics = SprintAnalytics(),
    val searchQuery: String = "",
    val sortFilter: MemberSortFilter = MemberSortFilter.DEFAULT,
    val sprintStartDay: String = "Saturday",
    val sprintDurationWeeks: Int = 2
)

class CodeReviewViewModel(
    private val repository: CodeReviewRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _sortFilter = MutableStateFlow(MemberSortFilter.DEFAULT)
    private val _sprintStartDay = MutableStateFlow("Saturday")
    private val _sprintDurationWeeks = MutableStateFlow(2)
    private val _selectedSprintId = MutableStateFlow<Long?>(null)

    private val _filterParams = combine(
        _searchQuery,
        _sortFilter,
        _sprintStartDay,
        _sprintDurationWeeks
    ) { query, filter, startDay, durationWeeks ->
        FilterContext(query, filter, startDay, durationWeeks)
    }

    val uiState: StateFlow<DashboardUiState> = combine(
        repository.allSprints,
        _selectedSprintId,
        _filterParams
    ) { sprints, selectedId, filterCtx ->
        val selected = if (selectedId != null) {
            sprints.find { it.id == selectedId } ?: sprints.firstOrNull()
        } else {
            sprints.find { it.isCurrent } ?: sprints.firstOrNull()
        }
        val selectedIndex = if (selected != null) sprints.indexOf(selected) else -1
        val previous = if (selectedIndex >= 0 && selectedIndex + 1 < sprints.size) {
            sprints[selectedIndex + 1]
        } else null

        SprintContext(sprints, selected, previous, filterCtx)
    }.flatMapLatest { ctx ->
        val selectedId = ctx.selectedSprint?.id ?: -1L
        val previousId = ctx.previousSprint?.id ?: -1L

        combine(
            if (selectedId != -1L) repository.getRecordsForSprint(selectedId) else flowOf(emptyList()),
            if (previousId != -1L) repository.getRecordsForSprint(previousId) else flowOf(emptyList())
        ) { records, prevRecords ->
            val filtered = records.filter {
                it.memberName.contains(ctx.filterCtx.query, ignoreCase = true)
            }.sortedBy { it.memberName.lowercase() }

            val totalR1 = records.sumOf { it.r1Count }
            val totalR2 = records.sumOf { it.r2Count }
            val totalCombined = totalR1 + totalR2
            val memberCount = records.size
            val topReviewer = records.maxByOrNull { it.totalReviews }?.memberName ?: "-"
            val avgTotal = if (memberCount > 0) totalCombined.toFloat() / memberCount else 0f
            val avgR1 = if (memberCount > 0) totalR1.toFloat() / memberCount else 0f
            val avgR2 = if (memberCount > 0) totalR2.toFloat() / memberCount else 0f

            val prevTotalR1 = prevRecords.sumOf { it.r1Count }
            val prevTotalR2 = prevRecords.sumOf { it.r2Count }
            val prevTotalCombined = prevTotalR1 + prevTotalR2

            val analytics = SprintAnalytics(
                totalR1 = totalR1,
                totalR2 = totalR2,
                totalCombined = totalCombined,
                topReviewerName = topReviewer,
                avgReviewsPerMember = avgTotal,
                avgR1PerMember = avgR1,
                avgR2PerMember = avgR2,
                teamMemberCount = memberCount,
                prevTotalR1 = prevTotalR1,
                prevTotalR2 = prevTotalR2,
                prevTotalCombined = prevTotalCombined
            )

            DashboardUiState(
                isLoading = false,
                sprints = ctx.sprints,
                selectedSprint = ctx.selectedSprint,
                previousSprint = ctx.previousSprint,
                records = records,
                previousSprintRecords = prevRecords,
                filteredRecords = filtered,
                analytics = analytics,
                searchQuery = ctx.filterCtx.query,
                sortFilter = ctx.filterCtx.sortFilter,
                sprintStartDay = ctx.filterCtx.startDay,
                sprintDurationWeeks = ctx.filterCtx.durationWeeks
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState(isLoading = true)
    )

    data class FilterContext(
        val query: String,
        val sortFilter: MemberSortFilter,
        val startDay: String,
        val durationWeeks: Int
    )

    private data class SprintContext(
        val sprints: List<Sprint>,
        val selectedSprint: Sprint?,
        val previousSprint: Sprint?,
        val filterCtx: FilterContext
    )

    fun selectSprint(sprintId: Long) {
        _selectedSprintId.value = sprintId
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSortFilter(filter: MemberSortFilter) {
        _sortFilter.value = filter
    }

    fun updateSprintSettings(startDay: String, durationWeeks: Int) {
        _sprintStartDay.value = startDay
        _sprintDurationWeeks.value = durationWeeks
    }

    fun incrementR1(recordId: Long) {
        viewModelScope.launch { repository.incrementR1(recordId) }
    }

    fun decrementR1(recordId: Long) {
        viewModelScope.launch { repository.decrementR1(recordId) }
    }

    fun incrementR2(recordId: Long) {
        viewModelScope.launch { repository.incrementR2(recordId) }
    }

    fun decrementR2(recordId: Long) {
        viewModelScope.launch { repository.decrementR2(recordId) }
    }

    fun addMemberToCurrentSprint(memberName: String, avatarColorHex: String) {
        val currentSprintId = uiState.value.selectedSprint?.id ?: return
        addMemberToSprint(currentSprintId, memberName, avatarColorHex)
    }

    fun addMemberToSprint(sprintId: Long, memberName: String, avatarColorHex: String) {
        viewModelScope.launch {
            repository.addMemberToSprint(sprintId, memberName, avatarColorHex)
        }
    }

    fun updateMemberRecord(record: ReviewRecord) {
        viewModelScope.launch {
            repository.updateMemberRecord(record)
        }
    }

    fun deleteMemberRecord(recordId: Long) {
        viewModelScope.launch {
            repository.deleteMemberRecord(recordId)
        }
    }

    fun deleteMemberRecord(record: ReviewRecord) {
        viewModelScope.launch {
            repository.deleteMemberRecord(record.id)
        }
    }

    fun updateSprintName(sprintId: Long, newName: String) {
        viewModelScope.launch {
            repository.updateSprintName(sprintId, newName)
        }
    }

    fun createNewSprint(name: String, startDate: Long, endDate: Long, number: Int) {
        viewModelScope.launch {
            val newId = repository.createNewSprint(name, startDate, endDate, number)
            _selectedSprintId.value = newId
        }
    }
}

class CodeReviewViewModelFactory(
    private val repository: CodeReviewRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CodeReviewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CodeReviewViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
