package com.zosimadis.testing.di

import com.zosimadis.network.Dispatcher
import com.zosimadis.network.PixaBayDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestDispatcher

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [TestDispatcherModule::class],
)
internal object TestDispatchersModule {

    @Provides
    @Dispatcher(PixaBayDispatchers.IO)
    fun providesIODispatcher(testDispatcher: TestDispatcher): CoroutineDispatcher = testDispatcher

    @Provides
    @Dispatcher(PixaBayDispatchers.Default)
    fun provideDefaultDispatcher(testDispatcher: TestDispatcher): CoroutineDispatcher =
        testDispatcher
}
