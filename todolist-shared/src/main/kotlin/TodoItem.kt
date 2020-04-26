package com.soumyajit

import java.time.LocalDate

data class TodoItem(
    val title: String,
    val details: String,
    val assignedTo: String,
    val dueDate: LocalDate,
    val importance: Importance
)

enum class Importance {
    LOW, MEDIUM, HIGH
}