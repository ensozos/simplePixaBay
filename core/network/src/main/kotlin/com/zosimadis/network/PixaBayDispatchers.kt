package com.zosimadis.network

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val pixaBayDispatchers: PixaBayDispatchers)

enum class PixaBayDispatchers {
    IO,
    Default,
}
