package com.cyphernerd.resume.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.cyphernerd.resume.data.network.GeminiApi
import com.cyphernerd.resume.data.network.OpenAIApi
import com.cyphernerd.resume.data.repositoryimpl.ResumeRepositoryImpl
import com.cyphernerd.resume.data.util.Constant
import com.cyphernerd.resume.domain.repository.ResumeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofit(@ApplicationContext context: Context): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.openai.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .addInterceptor(ChuckerInterceptor(context))
            .build())
        .build()

    @Provides
    @Singleton
    fun provideOpenAIApi(@ApplicationContext context: Context): OpenAIApi {
        return Retrofit.Builder()
            .baseUrl("https://api.openai.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        }
                    )
                    .addInterceptor(ChuckerInterceptor(context))
                    .build())
            .build()
            .create(OpenAIApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGeminiApi(@ApplicationContext context: Context): GeminiApi {
        val geminiClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder()
                        .addHeader("Authorization", Constant.Api_Key) // Replace this with your actual API key
                        .addHeader("Content-Type", "application/json") // Ensure correct content type
                        .build()
                )
            }
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()

        return Retrofit.Builder()
            .baseUrl("https://aiplatform.googleapis.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(geminiClient)
            .build()
            .create(GeminiApi::class.java)
    }
    @Provides
    @Singleton
    fun provideGeminiRepository(geminiApi: GeminiApi): ResumeRepository{
        return ResumeRepositoryImpl(geminiApi)
    }
}
