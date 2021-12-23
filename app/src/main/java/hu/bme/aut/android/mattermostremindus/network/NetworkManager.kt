package hu.bme.aut.android.mattermostremindus.network

import android.annotation.SuppressLint
import android.util.Log
import hu.bme.aut.android.mattermostremindus.model.channels.Channel
import hu.bme.aut.android.mattermostremindus.model.login.User
import hu.bme.aut.android.mattermostremindus.model.post.Post
import hu.bme.aut.android.mattermostremindus.utils.Log.Companion.logTAG
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.reflect.KFunction1

object NetworkManager {
    private var retrofit: Retrofit? = null
    private var mattermostApi: MattermostApi? = null

    //private const val SERVICE_URL = "http://172.17.0.1:8065"

    fun login(
        login_id: String,
        mmUrl: String,
        password: String,
    ): Call<User>? {
        val json = JSONObject()
        json.put("login_id", login_id)
        json.put("password", password)
        init(mmUrl)
        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("application/json"), json.toString())
        return mattermostApi!!.login(requestBody)
    }

    private fun init(url: String) {
        if (retrofit == null || retrofit!!.baseUrl().toString() != url) {
            retrofit = Retrofit.Builder()
                .baseUrl(url)
                .client(OkHttpClient.Builder().build())
                .addConverterFactory(GsonConverterFactory.create()).build()
            mattermostApi = retrofit!!.create(MattermostApi::class.java)
        }

    }

    private fun getUserByIDCaller(login_id: String, mmUrl: String, username: String): Call<User>? {
        init(mmUrl)
        return mattermostApi!!.getUserIDByName("Bearer $login_id", username)
    }

    private fun createPrivateChannelCaller(
        login_id: String,
        mmUrl: String,
        user_id1: String,
        user_id2: String,
    ): Call<Channel>? {
        init(mmUrl)
        val json = "[\"$user_id1\",\"$user_id2\"]"
        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("application/json"), json)
        return mattermostApi!!.createDirectMessageChannel("Bearer $login_id", requestBody)
    }

    private fun sendMessageCaller(
        login_id: String,
        mmUrl: String,
        createdchannelid: String,
        message: String
    ): Call<Post>? {
        init(mmUrl)
        val json = JSONObject()
        json.put("channel_id", createdchannelid)
        json.put("message", message)
        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("application/json"), json.toString())
        return mattermostApi!!.sendMessage("Bearer $login_id", requestBody)
    }


    private fun getUsernameById(
        token: String,
        mmUrl: String,
        username: String,
        callback: (String) -> Unit
    ) {
        if (token.isNotEmpty()) {
            getUserByIDCaller(token, mmUrl, username)?.enqueue(
                object : Callback<User> {
                    @SuppressLint("CommitPrefEdits")
                    override fun onResponse(
                        call: Call<User>?,
                        response: Response<User>?
                    ) {
                        if (response!!.isSuccessful) {
                            Log.d(
                                logTAG,
                                "OK--${response.body()}"
                            )
                            response.body()?.id?.let { callback(it) }
                        } else {
                            Log.d(
                                logTAG,
                                "failure--${response}"
                            )
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Log.d(logTAG, "failure--$t")
                    }
                }
            )
        }
    }

    fun sendMessageToUser(
        token: String,
        mmUrl: String,
        myUser: String,
        username: String,
        message: String,
        todoID: Long,
        callback: KFunction1<Long, Unit>
    ) {
        var user1id: String?
        var user2id: String? = null
        var createdchannelid: String?

        fun setCreatedChannel(id: String) {
            createdchannelid = id
            sendMessage(token, mmUrl, message, createdchannelid, todoID, callback)
        }

        fun setuser1(id: String) {
            user1id = id
            createPrivateChannel(token, mmUrl, user1id, user2id, ::setCreatedChannel)
        }

        fun setuser2(id: String) {
            user2id = id
            getUsernameById(token, mmUrl, myUser, ::setuser1)
        }

        getUsernameById(token, mmUrl, username, ::setuser2)

    }

    private fun sendMessage(
        token: String,
        mmUrl: String,
        message: String,
        createdchannelid: String?,
        todoID: Long,
        callback: KFunction1<Long, Unit>
    ) {
        if (token.isNotEmpty()) {
            if (createdchannelid != null) {
                sendMessageCaller(token, mmUrl, createdchannelid, message)?.enqueue(
                    object : Callback<Post> {
                        @SuppressLint("CommitPrefEdits")
                        override fun onResponse(
                            call: Call<Post>?,
                            response: Response<Post>?
                        ) {
                            if (response!!.isSuccessful) {
                                Log.d(
                                    logTAG,
                                    "OK--${response.body()}"
                                )
                                callback(todoID)
                            } else {
                                Log.d(
                                    logTAG,
                                    "failure--${response}"
                                )
                            }
                        }

                        override fun onFailure(call: Call<Post>, t: Throwable) {
                            Log.d(
                                logTAG,
                                "failure--$t"
                            )
                        }
                    }
                )
            }
        }
    }

    private fun createPrivateChannel(
        token: String,
        mmUrl: String,
        user1id: String?,
        user2id: String?,
        callback: (String) -> Unit
    ) {
        if (token.isNotEmpty()) {
            if (user1id != null) {
                if (user2id != null) {
                    createPrivateChannelCaller(token, mmUrl, user1id, user2id)?.enqueue(
                        object : Callback<Channel> {
                            @SuppressLint("CommitPrefEdits")
                            override fun onResponse(
                                call: Call<Channel>?,
                                response: Response<Channel>?
                            ) {
                                if (response!!.isSuccessful) {
                                    Log.d(
                                        logTAG,
                                        "OK--${response.body()}"
                                    )
                                    response.body()?.id?.let { callback(it) }
                                } else {
                                    Log.d(
                                        logTAG,
                                        "failure--${response}"
                                    )
                                }
                            }

                            override fun onFailure(call: Call<Channel>, t: Throwable) {
                                Log.d(
                                    logTAG,
                                    "failure--$t"
                                )
                            }
                        }
                    )
                }
            }
        }

    }
}
