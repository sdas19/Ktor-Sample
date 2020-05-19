package com.soumyajit

interface TodoService {
    fun getAll(): List<TodoItem>
    fun getTodo(id: Int): TodoItem
    fun delete(id: Int): Boolean
    fun create(todo: TodoItem): Boolean
    fun update(id: Int, todo: TodoItem): Boolean
}