package hu.bme.aut.android.mattermostremindus.network

import android.annotation.SuppressLint
import android.util.Log
import hu.bme.aut.android.mattermostremindus.model.channels.Channel
import hu.bme.aut.android.mattermostremindus.model.channels.Channels
import hu.bme.aut.android.mattermostremindus.model.login.User
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {
    private lateinit var retrofit: Retrofit
    private lateinit var mattermostApi: MattermostApi

    //private const val SERVICE_URL = "https://mattermost.kszk.bme.hu"
    //private const val SERVICE_URL = "http://172.17.0.1:8065"
    //teszt - teszt
    //teszt2 - teszt2

    fun login(
        login_id: String,
        password: String,
        mmUrl: String
    ): Call<User>? {
        val json = JSONObject()
        json.put("login_id", login_id)
        json.put("password", password)
        init(mmUrl)
        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("application/json"), json.toString())
        return mattermostApi.login(requestBody)
    }

    private fun init(url: String) {
        retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create()).build()
        mattermostApi = retrofit.create(MattermostApi::class.java)
    }

    private fun getUserByIDCaller(login_id: String, username: String): Call<User>? {
        return mattermostApi.getUserIDByName(login_id, username)
    }

    //    private fun sendMessageToUserCaller(
//        login_id: String,
//        user_id: String,
//        message: String
//    ): Call<User>? {
//        return mattermostApi.sendMessage(login_id, user_id, message)
//    }
    private fun createPrivateChannelCaller(
        login_id: String,
        user_id1: String,
        user_id2: String,
    ): Call<Channel>? {
        val json = "[\"$user_id1\",\"$user_id2\"]"
        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("application/json"), json)
        return mattermostApi.createDirectMessageChannel(login_id, requestBody)
    }


    fun getUsernameById(token: String, username: String, callback: Callback) {
        if (token.isNotEmpty()) {
            getUserByIDCaller(token, username)?.enqueue(
                object : Callback<User> {
                    @SuppressLint("CommitPrefEdits")
                    override fun onResponse(
                        call: Call<User>?,
                        response: Response<User>?
                    ) {
                        if (response!!.isSuccessful) {
                            Log.d(
                                hu.bme.aut.android.mattermostremindus.utils.Log.logTAG,
                                "OK--${response.body()}"
                            )

                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Log.d(hu.bme.aut.android.mattermostremindus.utils.Log.logTAG, "failure--$t")
                    }
                }
            )
        }
    }

    fun sendMessageToUser(token: String, username: String, message: String) {
        var user1: String? = null
        getUsernameById(token, username, { _ -> user1 = _ })

        if (token.isNotEmpty()) {
            getUserByIDCaller(token, username)?.enqueue(
                object : Callback<User> {
                    @SuppressLint("CommitPrefEdits")
                    override fun onResponse(
                        call: Call<User>?,
                        response: Response<User>?
                    ) {
                        if (response!!.isSuccessful) {
                            Log.d(
                                hu.bme.aut.android.mattermostremindus.utils.Log.logTAG,
                                "OK--${response.body()}"
                            )
                            //response.body()?.let { sendMessageToUserCaller(token, it.id, message) }


                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Log.d(hu.bme.aut.android.mattermostremindus.utils.Log.logTAG, "failure--$t")
                    }
                }
            )
        }
    }


//fun sendMessage(login_id: String, toUser: String, message: String): Call<UserData>? {
//    val json = JSONObject()
//    json.put("login_id", login_id)
//    val requestBody: RequestBody =
//        RequestBody.create(MediaType.parse("application/json"), json.toString())
//    return mattermostApi.login(requestBody)
//}

    fun getChannels(
        authHeader: String?
    ): Array<Channels>? {
//        return if(authHeader.isNullOrEmpty()) null
//        else{
//            mattermostApi.getChannels(authHeader).enqueue(
//                object : Callback<Channels> {
//                    @SuppressLint("CommitPrefEdits")
//                    override fun onResponse(
//                        call: Call<Channels>?,
//                        response: Response<Channels>?
//                    ) {
//                        if (response!!.isSuccessful) {
//
//                            return
//                        }
//                    }
//
//                    override fun onFailure(call: Call<UserData>?, t: Throwable?) {
//                        Log.d("MatterMost", "failure--" + t.toString())
//                        Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG)
//                            .show()
//                    }
//
//                    override fun onFailure(call: Call<Channels>, t: Throwable) {
//                        return null
//                    }
//                })
//            return data
//        }
        return null
    }
}
