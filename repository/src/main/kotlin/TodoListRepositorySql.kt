package com.soumyajit

import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import org.bson.BsonDocument
import org.bson.BsonObjectId
import org.bson.types.ObjectId

class TodoListRepositorySql(val database: MongoDatabase) : TodoListRepository {

    private val todoCollection by lazy {
        database.getCollection("todo-collection", BsonDocument::class.java)
    }

    override fun insertTodos(data: List<TodoItem>) {
        todoCollection.insertMany(data.toBson())
    }

    override fun getAllTodos(): List<TodoItem> {
        val documentList = todoCollection.find()
        return mutableListOf<TodoItem>().apply {
            documentList.forEach { add(it.mapToTodo()) }
        }
    }

    override fun getTodoByAssignee(assignee: String): List<TodoItem> {
        val documentList =
            todoCollection.find(Filters.eq("assignedTo", assignee))
        return mutableListOf<TodoItem>().apply {
            documentList.forEach { add(it.mapToTodo()) }
        }
    }

    override fun update(id: String, item: TodoItem): Boolean {
        return todoCollection.replaceOne(Filters.eq("_id", BsonObjectId(ObjectId(id))), item.toBson()).wasAcknowledged()
    }

    override fun delete(id: String) =
        todoCollection.deleteOne(Filters.eq("_id", BsonObjectId(ObjectId(id)))).wasAcknowledged()

    override fun drop() = todoCollection.drop()

}
