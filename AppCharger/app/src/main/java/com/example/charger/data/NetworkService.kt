package com.example.charger.data

import com.example.charger.data.model.ChargingLocation
import com.example.charger.data.model.user.AuthRequest
import com.example.charger.data.model.user.AuthResponse
import com.example.charger.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

private const val OUTPUT = "json"

class NetworkService {

    private var authInterceptor: AuthInterceptor? = null

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.HEADERS
    }

    private var okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .apply {
            authInterceptor?.let { interceptor ->
                addInterceptor(interceptor)
            }
            addInterceptor(loggingInterceptor)
        }
        .build()

    private val retrofit = Retrofit.Builder()

        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val service: OcmApiService = retrofit.create(OcmApiService::class.java)

    fun setAuthInterceptor(interceptor: AuthInterceptor) {
        authInterceptor = interceptor
        okHttpClient = okHttpClient.newBuilder().addInterceptor(interceptor).build()
    }

    interface OcmApiService {
        @GET("/v3/poi/")
        suspend fun getChargingLocations(
            @Query("output") output: String = OUTPUT,
            @Query("latitude") latitude: Double,
            @Query("longitude") longitude: Double,
            @Query("distance") distance: Int,
            @Query("maxresults") maxResults: Int,
            @Query("key") apiKey: String = BuildConfig.API_KEY
        ): List<ChargingLocation>

        @POST("/v3/profile/authenticate")
        suspend fun authenticate(
            @Query("key") apiKey: String = BuildConfig.API_KEY,
            @Body request: AuthRequest
        ): AuthResponse

        @GET("/v3/poi/")
        suspend fun getChargingLocationById(
            @Query("chargepointid") chargePointId: String,
            @Query("output") output: String = OUTPUT,
            @Query("key") apiKey: String = BuildConfig.API_KEY
        ): List<ChargingLocation>
    }
}

class AuthInterceptor(private val accessToken: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val modifiedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

        return chain.proceed(modifiedRequest)
    }
}