package com.soumyajit

import com.google.gson.GsonBuilder
import io.ktor.config.MapApplicationConfig
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.createTestEnvironment
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import org.amshove.kluent.`should be`
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotBeNull
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.time.LocalDate

object GetTodosSpec : Spek({

    val todo = TodoItem(
        1,
        "Add database processing",
        "Add backend support to this code",
        "Kevin",
        LocalDate.of(2018, 12, 18),
        Importance.HIGH
    )

    describe("Get the Todos"){
        val engine = TestApplicationEngine(createTestEnvironment())
        engine.start(false)
        with(engine){
            (environment.config as MapApplicationConfig).apply {
                put("ktor.environment", "test")
            }
        }
        engine.application.module(true)

        val gson =  GsonBuilder().apply {
            registerTypeAdapter(LocalDate::class.java, LocalDateSerializeAdapter())
            registerTypeAdapter(LocalDate::class.java, LocalDataDeserializeAdapter())
            setPrettyPrinting()
        }.create()

        with(engine){
            it("Should be OK to get the list of Todos"){
                handleRequest(HttpMethod.Get, "/api/todos").apply {
                    response.status().`should be`(HttpStatusCode.OK)
                }
            }

            it("should get the todos") {
                with(handleRequest(HttpMethod.Get, "/api/todos")) {
                    response.content.shouldNotBeNull().shouldContain("database")
                }
            }

            it("should create the todos") {
                with(handleRequest(HttpMethod.Post, "/api/todos"){
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(gson.toJson(todo))
                }) {
                    response.status().`should be`(HttpStatusCode.Created)
                }
            }

            it("should update the todos") {
                with(handleRequest(HttpMethod.Put, "/api/todos/1"){
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(gson.toJson(todo))
                }) {
                    response.status().`should be`(HttpStatusCode.NoContent)
                }
            }

            it("should delete the todos") {
                with(handleRequest(HttpMethod.Delete, "/api/todos/1")) {
                    response.status().`should be`(HttpStatusCode.NoContent)
                }
            }

            it("should get the todo if the id is set") {
                with(handleRequest(HttpMethod.Get, "/api/todos/1")) {
                    response.content
                        .shouldNotBeNull()
                        .shouldContain("database")
                }
            }

            it("should return an error if the id is invalid") {
                with(handleRequest(HttpMethod.Get, "/api/todos/3")) {
                    response.status().shouldEqual(HttpStatusCode.NotFound)
                }
            }
        }
    }
})