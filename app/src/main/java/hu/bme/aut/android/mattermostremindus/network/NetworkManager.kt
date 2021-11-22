package hu.bme.aut.android.mattermostremindus.network

import hu.bme.aut.android.mattermostremindus.model.Channels
import hu.bme.aut.android.mattermostremindus.model.UserData
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
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
    ): Call<UserData>? {
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
