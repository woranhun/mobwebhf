package hu.bme.aut.android.mattermostremindus.activities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import hu.bme.aut.android.mattermostremindus.databinding.ActivityLoginBinding
import hu.bme.aut.android.mattermostremindus.model.login.User
import hu.bme.aut.android.mattermostremindus.network.NetworkManager
import hu.bme.aut.android.mattermostremindus.utils.Log.Companion.logTAG
import hu.bme.aut.android.mattermostremindus.utils.SharedPreferencies.Companion.MattermostRemindUs
import hu.bme.aut.android.mattermostremindus.utils.SharedPreferencies.Companion.MmApiKey
import hu.bme.aut.android.mattermostremindus.utils.SharedPreferencies.Companion.MmMyUser
import hu.bme.aut.android.mattermostremindus.utils.SharedPreferencies.Companion.MmUrl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val previousUrl = getSharedPreferences(MattermostRemindUs, Context.MODE_PRIVATE).getString(
            MmUrl, null
        )
        if (!previousUrl.isNullOrEmpty()) {
            binding.etMmurl.setText(previousUrl.toString())
        }
        binding.btnLogin.setOnClickListener {
            try {
                NetworkManager.login(
                    binding.etMmusername.text.toString(),
                    binding.etMmurl.text.toString(),
                    binding.etMmpassword.text.toString(),
                )?.enqueue(
                    object : Callback<User> {
                        @SuppressLint("CommitPrefEdits")
                        override fun onResponse(
                            call: Call<User>?,
                            response: Response<User>?
                        ) {
                            if (response!!.isSuccessful) {

                                Toast.makeText(
                                    applicationContext,
                                    "Successful Login",
                                    Toast.LENGTH_LONG
                                ).show()
                                Log.d(logTAG, "ok--" + response.body().toString())
                                getSharedPreferences(
                                    MattermostRemindUs,
                                    Context.MODE_PRIVATE
                                ).edit().putString(MmApiKey, response.headers().get("Token"))
                                    .putString(MmUrl, binding.etMmurl.text.toString())
                                    .putString(MmMyUser, binding.etMmusername.text.toString())
                                    .apply()

                                finish()
                            }
                            if (response.code() in 400..499) {
                                Snackbar.make(binding.root, "Login Failed", 2000).show()
                                Log.d(logTAG, "failure--$response")
                            }
                        }

                        override fun onFailure(call: Call<User>?, t: Throwable?) {
                            Log.d(logTAG, "failure--" + t.toString())
                            Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG)
                                .show()
                        }
                    })
            } catch (e: Exception) {
                Log.d(logTAG, e.toString())
            }
        }
    }
}