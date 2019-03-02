package net.rsandor.plaything.user

import com.google.gson.FieldNamingPolicy
import com.google.inject.Guice
import com.google.inject.Injector
import io.ktor.application.*
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.AttributeKey
import net.rsandor.plaything.user.guice.CallModule
import net.rsandor.plaything.user.guice.MainModule
import net.rsandor.plaything.user.utils.UUIDs
import org.slf4j.event.Level
import java.util.*

//open class SimpleJWT(secret: String) {
//    private val algorithm = Algorithm.HMAC512(secret)
//    val verifier = JWT.require(algorithm).build()
//    fun sign(name:String): String = JWT.create().withClaim("name", name).sign(algorithm)
//}

val InjectorKey = AttributeKey<Injector>("injector")

val ApplicationCall.injector: Injector get() = attributes[InjectorKey]

fun Application.userModule() {
    val injector = Guice.createInjector(MainModule(this))

    intercept(ApplicationCallPipeline.Features) {
        call.attributes.put(InjectorKey, injector.createChildInjector(CallModule(call)))
    }

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            registerTypeAdapter(UUID::class.java, UUIDs.UuidTypeAdapter())
        }
    }

    install(CallLogging) {
        level = Level.INFO
    }
}

fun main() {
    embeddedServer(Netty,
        watchPaths = listOf("user-management"),
        port = 8080,
        module = Application::userModule).apply {
        start(true)
    }
}