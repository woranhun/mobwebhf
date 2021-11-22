package hu.bme.aut.android.mattermostremindus.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import hu.bme.aut.android.mattermostremindus.R
import hu.bme.aut.android.mattermostremindus.adapter.TodoAdapter
import hu.bme.aut.android.mattermostremindus.data.TodoItem
import hu.bme.aut.android.mattermostremindus.data.TodoListDatabase
import hu.bme.aut.android.mattermostremindus.databinding.ActivityMainBinding
import hu.bme.aut.android.mattermostremindus.fragments.DeleteAllDialogFragment
import hu.bme.aut.android.mattermostremindus.fragments.NewTodoItemDialogFragment
import hu.bme.aut.android.mattermostremindus.network.NetworkManager.getChannels
import hu.bme.aut.android.mattermostremindus.utils.SharedPreferencies.Companion.MattermostRemindUs
import hu.bme.aut.android.mattermostremindus.utils.SharedPreferencies.Companion.MmApiKey
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity(), TodoAdapter.TodoItemClickListener,
    NewTodoItemDialogFragment.NewTodoItemDialogListener,
    DeleteAllDialogFragment.DeleteAllDialogListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: TodoListDatabase
    private lateinit var adapter: TodoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        database = TodoListDatabase.getDatabase(applicationContext)


        if (getToken().isNullOrEmpty()) {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.fab.setOnClickListener {
            NewTodoItemDialogFragment(null).show(
                supportFragmentManager,
                NewTodoItemDialogFragment.TAG
            )
        }
        initRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.Login -> {
                startActivity(Intent(this, LoginActivity::class.java))
                true
            }
            R.id.Logout -> {
                removeToken()
                if (getToken().isNullOrEmpty()) Toast.makeText(
                    applicationContext,
                    "Successful Logout",
                    Toast.LENGTH_LONG
                ).show()
                true
            }
            R.id.DeleteAll -> {
                DeleteAllDialogFragment().show(
                    supportFragmentManager,
                    DeleteAllDialogFragment.TAG
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initRecyclerView() {
        adapter = TodoAdapter(this)
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = adapter
        loadItemsInBackground()
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.todoItemDao().getAll()
            runOnUiThread {
                adapter.update(items)
            }
        }
    }


    override fun onResume() {
        super.onResume()
        if (getChannels(getToken())?.count() == 0) {
            Snackbar.make(binding.root, "Login Expired!", 2000).show()
        }
    }

    private fun getToken(): String? {
        return getSharedPreferences(MattermostRemindUs, Context.MODE_PRIVATE).getString(
            MmApiKey,
            null
        )
    }

    private fun removeToken() {
        getSharedPreferences(MattermostRemindUs, Context.MODE_PRIVATE).edit()
            .putString(MmApiKey, null).apply()
    }

    override fun onItemChanged(item: TodoItem) {
        thread {
            database.todoItemDao().update(item)
            Log.d("Mattermost", "TodoItem update was successful")
        }
    }

    override fun onTodoItemCreated(newItem: TodoItem) {
        thread {
            val insertId = database.todoItemDao().insert(newItem)
            newItem.id = insertId
            runOnUiThread {
                adapter.addItem(newItem)
            }
        }
    }

    override fun onTodoItemEdited(newItem: TodoItem) {
        thread {
            database.todoItemDao().update(newItem)

            runOnUiThread {
                adapter.editItem(newItem)
            }
        }
    }

    override fun onDeleteAll() {
        thread {
            database.todoItemDao().deleteAll()
            runOnUiThread {
                adapter.deleteAll()
            }
        }
    }

    override fun onItemDeleted(item: TodoItem) {
        thread {
            database.todoItemDao().deleteItem(item)
            Log.d("MatterMost", "TodoItem delete was successful")
            runOnUiThread {
                adapter.deleteItem(item)
            }
        }
    }

    override fun onItemEdited(item: TodoItem) {
        NewTodoItemDialogFragment(item).show(
            supportFragmentManager,
            NewTodoItemDialogFragment.TAG
        )
    }
}