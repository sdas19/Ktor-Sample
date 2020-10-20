package com.soumyajit

import com.google.gson.Gson
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.routing.*
import java.lang.Exception

fun Routing.todoApi(todoService: TodoService) {

    route("/api") {

        post("/todos") {
            val todos = call.receive<Array<TodoItem>>()
            todoService.insertTodos(todos.toList())
            call.respond(HttpStatusCode.Created, todos)
        }

        get("/todos") {
            val todos = todoService.getAll()
            call.respond(todos)
        }

        get("/todos/{assignee}") {
            val assignee = call.parameters["assignee"]
            if (assignee == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            try {
                val result = todoService.getTodoByAssignee(assignee)
                call.respond(result)
            } catch (e: Throwable) {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        put("/todos/{id}") {
            val id = call.parameters["id"]
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }
            val todo = call.receive(TodoItem::class)
            val result = todoService.update(id, todo)
            if (!result) {
                call.respond(HttpStatusCode.NotFound)
                return@put
            }
            call.respond(HttpStatusCode.OK)
        }

        delete("/todos/{id}") {
            val id = call.parameters["id"]
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            val result = todoService.delete(id)
            if (!result) {
                call.respond(HttpStatusCode.NotFound)
                return@delete
            }
            call.respond(HttpStatusCode.NoContent)
        }

        delete("/todos") {
            todoService.drop()
            call.respond(HttpStatusCode.NoContent)
        }
    }
}


