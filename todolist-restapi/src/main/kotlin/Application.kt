package com.soumyajit

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.Routing
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import java.time.LocalDate


val todoAppAppModule = module {
    single<TodoService> { TodoServiceImpl(get()) }
    single<TodoListRepository> { TodoListRepositorySql() }
}

fun main(args: Array<String>) {
    startKoin { modules(todoAppAppModule) }
    io.ktor.server.cio.EngineMain.main(args)
}

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val todoService : TodoService by inject()
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
