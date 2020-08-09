package com.soumyajit

class TodoServiceImpl(val todoListRepository: TodoListRepository) : TodoService {

    override fun insertTodos(data: List<TodoItem>) = todoListRepository.insertTodos(data)

    override fun getAll(): List<TodoItem> = todoListRepository.getAllTodos()

    override fun getTodoByAssignee(assignee: String) = todoListRepository.getTodoByAssignee(assignee)

    override fun update(id: String, todo: TodoItem) = todoListRepository.update(id, todo)

    override fun delete(id: String) = todoListRepository.delete(id)

    override fun drop() = todoListRepository.drop()

}