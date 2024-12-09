package com.zosimadis.data.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Provides
    fun providesContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    fun providesSharedPref(context: Context): SharedPreferences {
        return context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
    }



}