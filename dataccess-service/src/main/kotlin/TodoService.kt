package com.soumyajit

interface TodoService {
    fun insertTodos(data: List<TodoItem>)
    fun getAll(): List<TodoItem>
    fun getTodoByAssignee(assignee: String): List<TodoItem>
    fun update(id: String, todo: TodoItem): Boolean
    fun delete(id: String): Boolean
    fun drop()
}