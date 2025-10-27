package com.blurr.voice.data

data class TaskHistoryItem(
    val task: String,
    val status: String,
    val startedAt: Long?, // Using Long timestamp instead of cloud service timestamp
    val completedAt: Long?, // Using Long timestamp instead of cloud service timestamp
    val success: Boolean?,
    val errorMessage: String?
) {
    fun getStatusEmoji(): String {
        return when (status.lowercase()) {
            "started" -> "🔄"
            "completed" -> if (success == true) "✅" else "❌"
            "failed" -> "❌"
            else -> "⏳"
        }
    }
    
    fun getFormattedStartTime(): String {
        return startedAt?.let { timestamp ->
            val date = java.util.Date(timestamp)
            val formatter = java.text.SimpleDateFormat("MMM dd, yyyy 'at' h:mm a", java.util.Locale.getDefault())
            formatter.format(date)
        } ?: "Unknown"
    }
    
    fun getFormattedCompletionTime(): String {
        return completedAt?.let { timestamp ->
            val date = java.util.Date(timestamp)
            val formatter = java.text.SimpleDateFormat("MMM dd, yyyy 'at' h:mm a", java.util.Locale.getDefault())
            formatter.format(date)
        } ?: "Not completed"
    }
}
