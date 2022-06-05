package com.example.onlineshop.di



import com.example.onlineshop.network.ApiService
import com.example.onlineshop.network.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton



@InstallIn(SingletonComponent::class)
@Module
class Module {

//    @Singleton
//    @Provides
//    fun provideDataBase(@ApplicationContext context: Context):AppDatabase{
//        return Room.databaseBuilder(
//            context.applicationContext,
//            AppDatabase::class.java, DATABASE_NAME
//        )
//            //.allowMainThreadQueries()
//            .fallbackToDestructiveMigration()
//            .build()
//    }

    @Singleton
    @Provides
    fun provideLogger():HttpLoggingInterceptor{
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC }
    }


    @Singleton
    @Provides
    fun provideClient(logger :HttpLoggingInterceptor):OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()
    }


    @Singleton
    @Provides
    fun provideMoshi():Moshi{
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    }

    @Singleton
    @Provides
    fun provideRetrofit(moshi : Moshi,client: OkHttpClient):Retrofit{
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .client(client)
            .build()

    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

}