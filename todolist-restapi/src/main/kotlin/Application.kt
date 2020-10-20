package com.soumyajit

import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import com.mongodb.ServerAddress
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.Routing
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDate


val todoAppAppModule = module {
    single<TodoService> { TodoServiceImpl(get()) }
    single<TodoListRepository> { TodoListRepositorySql(get()) }
    single<MongoDatabase> { getMongoDatabaseAtlas() }
}

private fun getDataBase(): MongoDatabase {
    val client = mongoClientWithUri()
    return client.getDatabase("todos-db")
}

private fun getMongoClient() = MongoClient(
    ServerAddress(
        "127.0.0.1",
        27017
    )
)

private fun mongoClientWithUri() = MongoClient(
    MongoClientURI("mongodb://127.0.0.1:27017")
)

private fun getMongoDatabaseAtlas() =
    MongoClients.create(
        getMongoClientUrlAtlas()
    ).getDatabase("todos-db")

private fun getMongoClientUrlAtlas(): String {
    val fileName = "mongo-atlas-configuration.json"
    return "mongodb+srv://test-user-ktor:" +
            URLEncoder.encode("pahGkrpCkMi6g6Qe", StandardCharsets.UTF_8.toString()) +
            "@test-cluster.szqba.mongodb.net/todos-db?retryWrites=true&w=majority"
}

fun main(args: Array<String>) {
    startKoin { modules(todoAppAppModule) }
    io.ktor.server.cio.EngineMain.main(args)
}

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val todoService: TodoService by inject()
    moduleWithDependencies(todoService)
}

fun Application.moduleWithDependencies(todoService: TodoService) {

    /*if(!testing) {
        trace { application.log.trace(it.buildText()) }
    }*/
    install(Routing) {
        todoApi(todoService)
    }
    install(StatusPages) {
        this.exception<Throwable> { e ->
            call.respondText(e.localizedMessage, ContentType.Text.Plain)
            throw e
        }
    }
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            registerTypeAdapter(LocalDate::class.java, LocalDateSerializeAdapter())
            registerTypeAdapter(LocalDate::class.java, LocalDataDeserializeAdapter())
        }
    }
}
