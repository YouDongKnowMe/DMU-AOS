package com.dongyang.android.youdongknowme.standard.util

import java.time.DayOfWeek

enum class Weekdays {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY;

    companion object {

        fun from(dayOfWeek: DayOfWeek): Weekdays {
            return when (dayOfWeek) {
                DayOfWeek.MONDAY -> MONDAY
                DayOfWeek.TUESDAY -> TUESDAY
                DayOfWeek.WEDNESDAY -> WEDNESDAY
                DayOfWeek.THURSDAY -> THURSDAY
                DayOfWeek.FRIDAY -> FRIDAY
                DayOfWeek.SATURDAY -> SATURDAY
                DayOfWeek.SUNDAY -> SUNDAY
                else -> throw IllegalArgumentException("일주일 범위에 해당되지 않습니다.")
            }
        }
    }
}