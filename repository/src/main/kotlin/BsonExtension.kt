@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.soumyajit

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.application.Application
import org.bson.BSONObject
import org.bson.BsonDocument
import org.bson.BsonObjectId
import org.bson.BsonValue
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.reflect.KClass

private const val EMPTY_STRING = ""
private const val DATE_INPUT_FORMAT = "yyyy-MM-dd"
private val gson by lazy { Gson() }

fun List<Any>.toBson(): List<BsonDocument> {
    val list = mutableListOf<BsonDocument>()
    list.addAll(this.map { item ->
        BsonDocument.parse(gson.toJson(item))
    })
    return list
}

fun Any.toBson(): BsonDocument = BsonDocument.parse(gson.toJson(this))

fun BsonDocument.mapToTodo(): TodoItem {
    return TodoItem(
        id = (this["_id"] as BsonObjectId).value.toString(),
        title = this["title"].mapToString(),
        details = this["details"].mapToString(),
        assignedTo = this["assignedTo"].mapToString(),
        dueDate = (this["dueDate"] as BsonDocument)?.stringValue().mapToLocalDate()
            ?: LocalDate.now(),
        importance = this["importance"].mapToString().toImportance()
    )
}

fun BsonValue?.mapToString() = this?.asString()?.value ?: EMPTY_STRING

fun BsonDocument?.stringValue(): String {
    return this?.let {
        "${it["year"]?.asInt32()?.value}-${it["month"]?.asInt32()?.value}-${it["day"]?.asInt32()?.value}"
    } ?: EMPTY_STRING
}

fun String?.mapToLocalDate(): LocalDate? {
    return LocalDate.parse(this, DateTimeFormatter.ofPattern(DATE_INPUT_FORMAT))
}

fun String.toImportance(): Importance {
    return when (this) {
        "HIGH" -> Importance.HIGH
        "MEDIUM" -> Importance.MEDIUM
        else -> Importance.LOW
    }
}
