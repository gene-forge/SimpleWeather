package ru.gene.sw.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.gene.sw.Config
import ru.gene.sw.data.api.IpApiService
import ru.gene.sw.data.api.WeatherApiService
import ru.gene.sw.data.db.WeatherDatabase
import ru.gene.sw.data.db.dao.WeatherDao
import ru.gene.sw.repository.WeatherRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): WeatherDatabase =
        Room.databaseBuilder(appContext, WeatherDatabase::class.java, Config.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideWeatherDao(db: WeatherDatabase): WeatherDao = db.weatherDao()

    @Provides
    @Singleton
    fun provideWeatherRepository(
        api: WeatherApiService,
        dao: WeatherDao,
        ipApi: IpApiService
    ): WeatherRepository = WeatherRepository(api, dao, ipApi)

    @Provides
    @Singleton
    fun provideWeatherApiService(client: OkHttpClient): WeatherApiService =
        Retrofit.Builder()
            .baseUrl(Config.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    @Provides
    @Singleton
    fun provideIpApiService(client: OkHttpClient): IpApiService =
        Retrofit.Builder()
            .baseUrl(Config.IP_LOCATION_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IpApiService::class.java)

}