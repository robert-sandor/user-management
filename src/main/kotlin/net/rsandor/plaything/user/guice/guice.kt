package net.rsandor.plaything.user.guice

import com.google.inject.AbstractModule
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import net.rsandor.plaything.user.repo.UserDao
import net.rsandor.plaything.user.repo.impl.UserDaoImpl
import net.rsandor.plaything.user.services.UserService
import net.rsandor.plaything.user.services.impl.UserServiceImpl
import net.rsandor.plaything.user.web.UserRouter

class CallModule(private val call: ApplicationCall): AbstractModule() {
    override fun configure() {
        bind(ApplicationCall::class.java).toInstance(call)
    }
}

class MainModule(private val application: Application): AbstractModule() {
    override fun configure() {
        bind(UserRouter::class.java).asEagerSingleton()
        bind(Application::class.java).toInstance(application)

        bind(UserDao::class.java).to(UserDaoImpl::class.java)
        bind(UserService::class.java).to(UserServiceImpl::class.java)
    }
}