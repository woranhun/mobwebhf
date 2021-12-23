package hu.bme.aut.android.mattermostremindus.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.mattermostremindus.R
import hu.bme.aut.android.mattermostremindus.adapter.TodoAdapter
import hu.bme.aut.android.mattermostremindus.data.TodoItem
import hu.bme.aut.android.mattermostremindus.data.TodoListDatabase
import hu.bme.aut.android.mattermostremindus.databinding.ActivityMainBinding
import hu.bme.aut.android.mattermostremindus.eventbus.BusHolder
import hu.bme.aut.android.mattermostremindus.eventbus.BusHolderListener
import hu.bme.aut.android.mattermostremindus.eventbus.MessageSentEvent
import hu.bme.aut.android.mattermostremindus.fragments.DeleteAllDialogFragment
import hu.bme.aut.android.mattermostremindus.fragments.NewTodoItemDialogFragment
import hu.bme.aut.android.mattermostremindus.services.MessageManagger
import hu.bme.aut.android.mattermostremindus.utils.SharedPreferencies
import org.greenrobot.eventbus.Subscribe
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity(), TodoAdapter.TodoItemClickListener,
    NewTodoItemDialogFragment.NewTodoItemDialogListener,
    DeleteAllDialogFragment.DeleteAllDialogListener, BusHolderListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: TodoListDatabase
    private lateinit var adapter: TodoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        database = TodoListDatabase.getDatabase(applicationContext)


        if (SharedPreferencies.getToken(applicationContext).isNullOrEmpty()) {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.fab.setOnClickListener {
            NewTodoItemDialogFragment(null).show(
                supportFragmentManager,
                NewTodoItemDialogFragment.TAG
            )
        }
        startService(Intent(this, MessageManagger::class.java))
        BusHolder.register(this)
        initRecyclerView()
    }

    override fun onDestroy() {
        super.onDestroy()
        BusHolder.unregister(this)
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
                SharedPreferencies.removeToken(applicationContext)
                if (SharedPreferencies.getToken(applicationContext).isNullOrEmpty()) Toast.makeText(
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

    override fun onItemChanged(item: TodoItem) {
        thread {
            database.todoItemDao().update(item)
            startService(Intent(this, MessageManagger::class.java))
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

    @Subscribe
    override fun onMessageSent(event: MessageSentEvent) {
        val todoItem = adapter.getItemById(event.todoId)
        if (todoItem != null) {
            onTodoItemEdited(todoItem)
        }
    }
}