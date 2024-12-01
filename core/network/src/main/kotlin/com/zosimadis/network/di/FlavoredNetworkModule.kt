package com.zosimadis.network.di

import com.zosimadis.network.PixaBayDataSource
import com.zosimadis.network.retrofit.RetrofitNetwork
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface FlavoredNetworkModule {

    @Binds
    fun bindNetwork(network: RetrofitNetwork): PixaBayDataSource
}
