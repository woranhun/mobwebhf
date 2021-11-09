package hu.bme.aut.android.mattermostremindus

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import hu.bme.aut.android.mattermostremindus.databinding.ActivityLoginBinding
import hu.bme.aut.android.mattermostremindus.model.UserData
import hu.bme.aut.android.mattermostremindus.network.NetworkManager
import hu.bme.aut.android.mattermostremindus.utils.SharedPreferencies.Companion.MattermostRemindUs
import hu.bme.aut.android.mattermostremindus.utils.SharedPreferencies.Companion.MmApiKey
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLogin.setOnClickListener {
            try {
                NetworkManager.login(
                    binding.etMmusername.text.toString(),
                    binding.etMmpassword.text.toString()
                )?.enqueue(
                    object : Callback<UserData> {
                        @SuppressLint("CommitPrefEdits")
                        override fun onResponse(
                            call: Call<UserData>?,
                            response: Response<UserData>?
                        ) {
                            if (response!!.isSuccessful) {

                                Toast.makeText(
                                    applicationContext,
                                    "Successful Login",
                                    Toast.LENGTH_LONG
                                ).show()
                                Log.d("MatterMost", "ok--" + response.body().toString())
                                getSharedPreferences(
                                    MattermostRemindUs,
                                    Context.MODE_PRIVATE
                                ).edit().putString(MmApiKey, response.body()?.id)
                                    .apply()

                                finish()
                            }
                            if (response.code() in 400..499) {
                                Snackbar.make(binding.root, "Login Failed", 2000).show()
                                Log.d("MatterMost", "failure--$response")
                            }
                        }

                        override fun onFailure(call: Call<UserData>?, t: Throwable?) {
                            Log.d("MatterMost", "failure--" + t.toString())
                            Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG)
                                .show()
                        }
                    })
            } catch (e: Exception) {
                Log.d("MatterMost", e.toString())
            }
        }
    }
}