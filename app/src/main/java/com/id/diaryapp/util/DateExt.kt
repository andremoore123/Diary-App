package com.id.diaryapp.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Extension function to convert Long to formatted date string
fun Long.toFormattedDate(): String {
    // Create a Date object from the Long value
    val date = Date(this)
    // Define the date format
    val dateFormat = SimpleDateFormat("d MMM", Locale.getDefault())
    // Format the date and return the result
    return dateFormat.format(date)
}