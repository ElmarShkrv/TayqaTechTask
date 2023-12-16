package com.example.tayqatechtask.di

import android.content.Context
import androidx.room.Room
import com.example.tayqatechtask.data.local.AppDatabase
import com.example.tayqatechtask.data.remote.TayqaTechApi
import com.example.tayqatechtask.util.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun injectRoomDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context, AppDatabase::class.java, "AppDB"
    ).build()

    @Provides
    @Singleton
    fun injectDao(database: AppDatabase) = database.personDao()

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideTayqaTechApi(retrofit: Retrofit): TayqaTechApi =
        retrofit.create(TayqaTechApi::class.java)

}