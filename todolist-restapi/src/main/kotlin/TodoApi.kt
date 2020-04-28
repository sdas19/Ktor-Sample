package com.soumyajit

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import java.time.LocalDate

fun Routing.todoApi() {
    route("/api") {
        get("/todos") {
            call.respond(todos)
        }
        get("/todos/{id}") {
            val id = call.parameters["id"]
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            try {
                val todo = todos[id.toInt()]
                call.respond(todo)
            } catch (e: Throwable) {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        post("/todos") {
            val todo = call.receive<TodoItem>()
            val newTodo = TodoItem(
                todos.size + 1,
                todo.title,
                todo.details,
                todo.assignedTo,
                todo.dueDate,
                todo.importance
            )
            todos = todos + newTodo
            call.respond(HttpStatusCode.Created, todos)
        }
        put("/todos/{id}") {
            val id = call.parameters["id"]
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }
            val foundItem = todos.getOrNull(id.toInt())
            if (foundItem == null) {
                call.respond(HttpStatusCode.NotFound)
                return@put
            }
            val todo = call.receive<TodoItem>()
            todos = todos.filter { it.id != todo.id }
            todos = todos + todo
            call.respond(HttpStatusCode.NoContent)
        }

        delete("/todos/{id}") {
            val id = call.parameters["id"]
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            val foundItem = todos.getOrNull(id.toInt())
            if (foundItem == null) {
                call.respond(HttpStatusCode.NotFound)
                return@delete
            }
            todos = todos.filter { it.id != id.toInt() }
            call.respond(HttpStatusCode.NoContent)
        }
    }
}


val todo1 = TodoItem(
    0,
    "Add RestAPI Data Access",
    "Add database support",
    "Me",
    LocalDate.of(2018, 12, 18),
    Importance.HIGH
)

val todo2 = TodoItem(
    1,
    "Add RestAPI Service",
    "Add a service to get the data",
    "Me",
    LocalDate.of(2018, 12, 18),
    Importance.MEDIUM
)

var todos = listOf(todo1, todo2)

