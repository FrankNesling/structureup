package com.webie.structureup.utils

// Helpers
import java.util.UUID

// OS
import java.util.Calendar


fun generateUniqueId(): String {
    return UUID.randomUUID().toString()
}

fun getStartOfDayInMilliSec(day: Calendar): Long {
    val dayAtStart = day.clone() as Calendar
    dayAtStart.apply {
        set(Calendar.HOUR_OF_DAY, 0)    // Set hours to 0
        set(Calendar.MINUTE, 0)         // Set minutes to 0
        set(Calendar.SECOND, 0)         // Set seconds to 0
        set(Calendar.MILLISECOND, 0)    // Set milliseconds to 0
    }
    return dayAtStart.timeInMillis  // Return the start of the day in milliseconds
}

fun getWeekDays(startDate: Calendar): List<Calendar> {
    // Generate a list of 7 Calendar objects representing the days of the week, starting from startDate
    return List(7) { offset ->
        (startDate.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, offset) }
    }
}