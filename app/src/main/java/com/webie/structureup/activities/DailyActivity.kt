package com.webie.structureup.activities

// OS
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.webie.structureup.R
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer


// UI
import android.widget.LinearLayout
import android.widget.TextView
import android.view.Gravity
import android.view.ViewGroup
import android.graphics.Color
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

// References
import com.webie.structureup.adapters.DailyTaskAdapter
import com.webie.structureup.adapters.TodoTaskAdapter
import com.webie.structureup.viewmodel.DailyViewModel

class DailyActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dailyTaskAdapter: DailyTaskAdapter

    // ViewModel
    private lateinit var dailyViewModel: DailyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily)

        // ViewModel
        dailyViewModel = ViewModelProvider(this).get(DailyViewModel::class.java)

        recyclerView = findViewById(R.id.dailyTaskList)

        recyclerView.layoutManager = LinearLayoutManager(this)
        dailyTaskAdapter = DailyTaskAdapter(dailyViewModel)
        recyclerView.adapter = dailyTaskAdapter


        // observer for refreshing data
        dailyViewModel.dailyTasks.observe(this, Observer { taskList ->
            dailyTaskAdapter.refreshData(taskList)
        })

        dailyViewModel.loadDailyTasks()

        // Add button
        val fabAdd: FloatingActionButton = findViewById(R.id.fab_add)
        fabAdd.setOnClickListener {
            // Show dialog to add a new user
            showAddDailyTaskDialog()
        }
    }

    private fun showAddDailyTaskDialog() {
        val input = EditText(this)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Add Daily Task")
            .setMessage("Enter the title of the new daily task:")
            .setView(input)
            .setPositiveButton("Add") { _, _ ->
                val title = input.text.toString().trim()
                if (title.isNotEmpty()) {
                    dailyViewModel.addDailyTask(title)
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }
}
