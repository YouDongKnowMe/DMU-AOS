package com.dongyang.android.youdongknowme.ui.view.schedule

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dongyang.android.youdongknowme.R
import com.dongyang.android.youdongknowme.databinding.FragmentScheduleBinding
import com.dongyang.android.youdongknowme.standard.base.BaseFragment
import com.dongyang.android.youdongknowme.ui.adapter.ScheduleAdapter
import com.dongyang.android.youdongknowme.ui.view.util.EventObserver
import com.prolificinteractive.materialcalendarview.CalendarDay
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDate

/* 학사 일정 화면 */
class ScheduleFragment : BaseFragment<FragmentScheduleBinding, ScheduleViewModel>() {

    override val layoutResourceId: Int = R.layout.fragment_schedule
    override val viewModel: ScheduleViewModel by viewModel()

    private lateinit var adapter: ScheduleAdapter

    override fun initStartView() {
        binding.vm = viewModel

        viewModel.setPickedDate(binding.mvScheduleCalendar.currentDate)

        binding.mvScheduleCalendar.leftArrow.setTintList(ColorStateList.valueOf(ContextCompat.getColor(this.requireContext(), R.color.blue300)))
        binding.mvScheduleCalendar.rightArrow.setTintList(ColorStateList.valueOf(ContextCompat.getColor(this.requireContext(), R.color.blue300)))

        adapter = ScheduleAdapter()
        binding.rvScheduleList.apply {
            this.adapter = this@ScheduleFragment.adapter
            this.layoutManager = LinearLayoutManager(requireActivity())
            this.setHasFixedSize(true)
            this.addItemDecoration(DividerItemDecoration(requireActivity(), 1))
        }
    }

    override fun initDataBinding() {
        viewModel.errorState.observe(viewLifecycleOwner, EventObserver { resId ->
            showToast(getString(resId))
        })

        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) showLoading()
            else dismissLoading()
        }

        viewModel.pickMonth.observe(viewLifecycleOwner) {
            viewModel.getSchedules()
        }

        viewModel.scheduleList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.scheduleErrorContainer.refresh.setOnClickListener {
            viewModel.getSchedules()
        }
    }

    override fun initAfterBinding() {

        binding.mvScheduleCalendar.setOnMonthChangedListener { _, date ->
            viewModel.setPickedDate(date)
        }

        // 최소 날짜, 최대 날짜 지정
        if(viewModel.currentYear.value == null || viewModel.currentYear.value != LocalDate.now().year){
            viewModel.setCurrentYear(LocalDate.now())
        }

        binding.mvScheduleCalendar.apply {
            viewModel.currentYear.value?.let{
                this.state().edit().setMinimumDate(CalendarDay.from(it-1, 1, 1))
                    .setMaximumDate(CalendarDay.from(it+1, 2, 28))
                    .commit()
            }
        }

        // 연/월 방식으로 타이틀 처리
        binding.mvScheduleCalendar.setTitleFormatter { day ->
            val inputText: LocalDate = day.date
            val calendarHeaderElements = inputText.toString().split("-").toTypedArray()

            val year = calendarHeaderElements[0]
            val month = calendarHeaderElements[1]

            val calendarHeaderBuilder = StringBuilder()
            calendarHeaderBuilder.append(year)
                .append(getString(R.string.calendar_year))
                .append(" ")
                .append(month)
                .append(getString(R.string.calendar_month))
            calendarHeaderBuilder.toString()
        }
    }
}
