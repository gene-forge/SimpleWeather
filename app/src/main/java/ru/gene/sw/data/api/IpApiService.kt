package ru.gene.sw.data.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.gene.sw.data.db.entity.IpLocation

interface IpApiService {
    @GET("json/")
    suspend fun getLocation(
        @Query("lang") lang:String
    ): IpLocation
}