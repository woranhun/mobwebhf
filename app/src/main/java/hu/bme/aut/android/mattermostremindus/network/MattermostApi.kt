package hu.bme.aut.android.mattermostremindus.network

import hu.bme.aut.android.mattermostremindus.model.Channels
import hu.bme.aut.android.mattermostremindus.model.UserData
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface MattermostApi {
    @Headers("Content-Type: application/json")
    @POST("/api/v4/users/login")
    fun login(
        @Body request: RequestBody
    ): Call<UserData>?

    @Headers("Content-Type: application/json")
    @GET("/api/v4/channels")
    fun getChannels(
        @Header("Authorization") authHeader: String
    ): Call<Channels>
}