package com.dongyang.android.youdongknowme.ui.view.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dongyang.android.youdongknowme.data.remote.entity.Schedule
import com.dongyang.android.youdongknowme.data.remote.entity.ScheduleEntry
import com.dongyang.android.youdongknowme.data.repository.ScheduleRepository
import com.dongyang.android.youdongknowme.standard.base.BaseViewModel
import com.dongyang.android.youdongknowme.standard.network.NetworkResult
import com.dongyang.android.youdongknowme.ui.view.util.Event
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import java.util.Date

/* 학사 일정 뷰모델 */
class ScheduleViewModel(private val scheduleRepository: ScheduleRepository) : BaseViewModel() {

    private val _errorState: MutableLiveData<Event<Int>> = MutableLiveData()
    val errorState: LiveData<Event<Int>> = _errorState

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError: MutableLiveData<Boolean> = MutableLiveData()
    val isError: LiveData<Boolean> = _isError

    private val _scheduleList = MutableLiveData<List<ScheduleEntry>>()
    val scheduleList: LiveData<List<ScheduleEntry>> = _scheduleList

    private val _pickYear = MutableLiveData<Int>()
    val pickYear: LiveData<Int> = _pickYear

    private val _pickMonth = MutableLiveData<Int>()
    val pickMonth: LiveData<Int> = _pickMonth

    private var _currentYear = MutableLiveData<Int>()
    val currentYear: LiveData<Int> = _currentYear

    fun setPickedDate(date: CalendarDay) {
        _pickYear.value = date.year
        _pickMonth.value = date.month
    }

    fun setCurrentYear(date: LocalDate){
        _currentYear.value = date.year
    }

    fun getSchedules() {
        _isLoading.postValue(true)
        viewModelScope.launch {
            when (val result = scheduleRepository.fetchSchedules()) {
                is NetworkResult.Success -> {
                    val scheduleList = result.data

                    // 선택한 연월 조건에 따라 리스트 출력
                    _scheduleList.postValue(getSchedulesForPickDate(scheduleList))

                    _isError.postValue(false)
                    _isLoading.postValue(false)
                }

                is NetworkResult.Error -> {
                    handleError(result, _errorState)
                    _isError.postValue(true)
                    _isLoading.postValue(false)
                }
            }
        }
    }

    private fun getSchedulesForPickDate(list: List<Schedule>): List<ScheduleEntry> {
        return list
            .filter { schedule ->
                schedule.year == pickYear.value &&
                        schedule.yearSchedules.any { it.month == pickMonth.value }
            }.flatMap { schedule ->
                schedule.yearSchedules
                    .find { it.month == pickMonth.value }
                    ?.scheduleEntries.orEmpty()
            }
    }
}