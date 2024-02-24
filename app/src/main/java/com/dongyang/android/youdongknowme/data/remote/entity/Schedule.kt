package com.dongyang.android.youdongknowme.data.remote.entity

import com.google.gson.annotations.SerializedName

data class Schedule(
    @SerializedName("year")
    val year: Int,
    @SerializedName("yearSchedule")
    val yearSchedule: List<YearSchedule>,
)

data class YearSchedule(
    @SerializedName("month")
    val month: Int,
    @SerializedName("scheduleEntries")
    val scheduleEntries: List<ScheduleEntry>
)

data class ScheduleEntry(
    @SerializedName("content")
    val content: String,
    @SerializedName("date")
    val date: List<String>
)