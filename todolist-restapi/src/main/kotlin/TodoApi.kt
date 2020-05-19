package com.soumyajit

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import java.time.LocalDate

fun Routing.todoApi(todoService: TodoService) {
    route("/api") {
        get("/todos") {
            val todos = todoService.getAll()
            call.respond(todos)
        }
        get("/todos/{id}") {
            val id = call.parameters["id"]
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            try {
                val todo = todoService.getTodo(id.toInt())
                call.respond(todo)
            } catch (e: Throwable) {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        post("/todos") {
            val todo = call.receive<TodoItem>()
            todoService.create(todo)
            /*val newTodo = TodoItem(
                todos.size + 1,
                todo.title,
                todo.details,
                todo.assignedTo,
                todo.dueDate,
                todo.importance
            )
            todos = todos + newTodo*/
            call.respond(HttpStatusCode.Created, todos)
        }
        put("/todos/{id}") {
            val id = call.parameters["id"] ?: throw IllegalArgumentException("Missing Id")
            /*if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }
            val foundItem = todos.getOrNull(id.toInt())
            if (foundItem == null) {
                call.respond(HttpStatusCode.NotFound)
                return@put
            }*/
            val todo = call.receive<TodoItem>()
            todoService.update(id.toInt(), todo)
            /*todos = todos.filter { it.id != todo.id }
            todos = todos + todo*/
            call.respond(HttpStatusCode.NoContent)
        }

        delete("/todos/{id}") {
            val id = call.parameters["id"] ?: throw IllegalArgumentException("Missing Id")
            /*if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            val foundItem = todos.getOrNull(id.toInt())
            if (foundItem == null) {
                call.respond(HttpStatusCode.NotFound)
                return@delete
            }
            todos = todos.filter { it.id != id.toInt() }*/
            todoService.delete(id.toInt())
            call.respond(HttpStatusCode.NoContent)
        }
    }
}


