package hu.bme.aut.android.mattermostremindus.network

import hu.bme.aut.android.mattermostremindus.model.channels.Channel
import hu.bme.aut.android.mattermostremindus.model.channels.Channels
import hu.bme.aut.android.mattermostremindus.model.login.User
import hu.bme.aut.android.mattermostremindus.model.post.Post
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface MattermostApi {
    @Headers("Content-Type: application/json")
    @POST("/api/v4/users/login")
    fun login(
        @Body request: RequestBody
    ): Call<User>?

    @Headers("Content-Type: application/json")
    @GET("/api/v4/channels")
    fun getChannels(
        @Header("Authorization") authHeader: String
    ): Call<Channels>

    @Headers("Content-Type: application/json")
    @GET("/api/v4/users/username/{username}")
    fun getUserIDByName(
        @Header("Authorization") authHeader: String,
        @Path("username") username: String

    ): Call<User>?

    @Headers("Content-Type: application/json")
    @POST("/api/v4/channels/direct")
    fun createDirectMessageChannel(
        @Header("Authorization") authHeader: String,
        @Body request: RequestBody
    ): Call<Channel>?

    @Headers("Content-Type: application/json")
    @POST("/api/v4/posts")
    fun sendMessage(
        @Header("Authorization") authHeader: String,
        @Body request: RequestBody
    ): Call<Post>?
}