package com.fawry.movieapptask.di

import android.content.Context
import com.fawry.movieapptask.BuildConfig
import com.fawry.movieapptask.business.network.*
import com.fawry.movieapptask.business.network.Constants
import com.fawry.movieapptask.business.network.interceptors.CurlLoggingInterceptor
import com.fawry.movieapptask.business.network.interceptors.ResponseLoggingInterceptor
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [ApplicationModule::class])
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideInternetConnectionManager(context: Context): InternetConnectionManagerInterface {
        return InternetConnectionManager(context)
    }

    @Provides
    @Singleton
    fun provideMoviesRepo(retrofit: Retrofit): MoviesRepoInterface {
        return MoviesRepo(retrofit)
    }

    @Provides
    @Singleton
    fun provideApiRequestManager(): ApiRequestManagerInterface {
        return ApiRequestManager()
    }

    @Provides
    @Singleton
    @Named("baseURL")
    fun provideBaseURL(): String {
        return Constants.baseURL
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(90, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .addInterceptor { chain: Interceptor.Chain ->

                val request = chain.request().newBuilder()

                chain.proceed(request.build())
            }

        if (BuildConfig.DEBUG) {
            okHttpClientBuilder
                .addInterceptor(CurlLoggingInterceptor())
                .addInterceptor(ResponseLoggingInterceptor())
        }

        return okHttpClientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
    }

    @Provides
    @Singleton
    fun provideRetrofitClient(
        @Named("baseURL") baseUrl: String,
        gson: Gson,
        httpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(httpClient)
            .build()
    }


}
