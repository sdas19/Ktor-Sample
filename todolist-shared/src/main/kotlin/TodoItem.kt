package com.soumyajit

import org.bson.codecs.pojo.annotations.BsonId
import java.time.LocalDate
import java.util.*

data class TodoItem(
    @BsonId var id: String = UUID.randomUUID().toString(),
    val title: String,
    val details: String,
    val assignedTo: String,
    val dueDate: LocalDate,
    val importance: Importance
)

enum class Importance {
    LOW, MEDIUM, HIGH
}