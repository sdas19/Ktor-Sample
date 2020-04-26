package com.soumyajit

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route
import java.time.LocalDate

fun Routing.todoApi() {
    route("/api/todo") {
        get("/") {
            call.respond(todos)
        }
    }
}


val todo1 = TodoItem(
    "Add RestAPI Data Access",
    "Add database support",
    "Me",
    LocalDate.of(2018, 12, 18),
    Importance.HIGH
)

val todo2 = TodoItem(
    "Add RestAPI Service",
    "Add a service to get the data",
    "Me",
    LocalDate.of(2018, 12, 18),
    Importance.MEDIUM
)

val todos = listOf(todo1, todo2)

