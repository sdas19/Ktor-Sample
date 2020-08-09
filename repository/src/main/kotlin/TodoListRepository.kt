package com.soumyajit

interface TodoListRepository {
    fun insertTodos(data: List<TodoItem>)
    fun getAllTodos(): List<TodoItem>
    fun getTodoByAssignee(assignee: String): List<TodoItem>
    fun update(id: String, item: TodoItem): Boolean
    fun delete(id: String): Boolean
    fun drop()
}

