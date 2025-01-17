package com.webie.structureup.activities

// OS
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer

// UI
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// References
import com.webie.structureup.R
import com.webie.structureup.adapters.TodoTaskAdapter
import com.webie.structureup.model.TodoTask
import com.webie.structureup.viewmodel.TodoViewModel
import com.webie.structureup.utils.generateUniqueId
import com.webie.structureup.utils.getStartOfDayInMilliSec
import com.webie.structureup.utils.getWeekDays

// Calendar
import java.util.*
import java.text.SimpleDateFormat
import android.graphics.Color
import android.util.Log

class TodoActivity : AppCompatActivity() {
    // UI elements
    private lateinit var addTodoTaskButton: Button
    private lateinit var todoTaskList: RecyclerView
    private val dayViews = mutableListOf<LinearLayout>()

    // Adapter
    private lateinit var todoTaskAdapter: TodoTaskAdapter

    // ViewModel
    private lateinit var todoViewModel: TodoViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

        // ViewModel
        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        /* UI */

        // Week buttons
        findViewById<Button>(R.id.nextWeekButton).setOnClickListener {
            todoViewModel.goToNextDay()
        }
        findViewById<Button>(R.id.previousWeekButton).setOnClickListener {
            todoViewModel.goToPreviousDay()
        }

        // Add button
        //addTodoTaskButton = findViewById(R.id.addTodoTaskButton)
        //addTodoTaskButton.setOnClickListener {
        //    addTodoTask()
        //}

        // List
        todoTaskList = findViewById(R.id.todoTaskList)
        todoTaskList.layoutManager = LinearLayoutManager(this)

        todoTaskAdapter = TodoTaskAdapter(todoViewModel)
        todoTaskList.adapter = todoTaskAdapter

        // observer for refreshing data
        todoViewModel.todoTasks.observe(this, Observer { taskList ->
            todoTaskAdapter.refreshData(taskList)
        })


        /* UI Logic */


        // Calendar
        todoViewModel.loadWeekDays()
        // display the week
        //todoViewModel.weekDays.observe(this, Observer { days ->
        //    setupWeekDisplay(days)
        //})

        //todoViewModel.selectedDate.observe(this, Observer { selectedDate ->
            // Update the week display when the selected date changes

        //})

        todoViewModel.weekDays.observe(this, Observer { weekDays ->
            // When the week days change, update the week display
            setupWeekDisplay(weekDays, todoViewModel.selectedDate.value ?: Calendar.getInstance())
        })

        todoViewModel.checkAndAddTasksForToday()
    }

    // To Do Tasks
    private fun addTodoTask() {
        val todoTaskName = "New Task"   // default name for new task

        // adding to UI + DB
        val newTodoTask = TodoTask(id = generateUniqueId(), title = todoTaskName, date = getStartOfDayInMilliSec(Calendar.getInstance()))
        todoViewModel.addTodoTask(newTodoTask)
    }

    // Calendar
    private fun setupWeekDisplay(days: List<Calendar>, selectedDate: Calendar) {
        val dayFormat = SimpleDateFormat("EEE", Locale.getDefault()) // Format for day names (Mon, Tue, etc.)
        val dateFormat = SimpleDateFormat("dd", Locale.getDefault())   // Format for dates (04, 05, etc.)
        val datePicker = findViewById<LinearLayout>(R.id.datePicker)

        datePicker.removeAllViews()  // Clear previous views
        dayViews.clear()  // Clear previous day views reference
        val today = Calendar.getInstance()
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)

        // Calculate the width for each day
        val dayWidth = (resources.displayMetrics.widthPixels / 7.5).toInt() // Dividing by 7.5 for better left alignment, TODO

        for (day in days) {
            val dayView = layoutInflater.inflate(R.layout.day_date, datePicker, false) as LinearLayout
            val dayTextView = dayView.findViewById<TextView>(R.id.dayTextView)
            val dateTextView = dayView.findViewById<TextView>(R.id.dateTextView)

            // Set day name and date
            dayTextView.text = dayFormat.format(day.time) // Set day name (Mon, Tue, etc.)
            dateTextView.text = dateFormat.format(day.time) // Set date (04, 05, etc.)

            // Set the width for each day item
            val layoutParams = LinearLayout.LayoutParams(dayWidth, LinearLayout.LayoutParams.WRAP_CONTENT)
            dayView.layoutParams = layoutParams

            // Set the Calendar object as the tag on each dayView
            dayView.tag = day

            // Update the selected date in the ViewModel when a day is clicked
            dayView.setOnClickListener {
                todoViewModel.setSelectedDate(day)
            }

            dayViews.add(dayView)
            datePicker.addView(dayView)
        }
        updateSelectedDateHighlight(selectedDate)
    }

    private fun updateSelectedDateHighlight(selectedDate: Calendar) {
        dayViews.forEach { dayView ->
            val dateTextView = dayView.findViewById<TextView>(R.id.dateTextView)
            val day = dayView.tag as Calendar // Assuming you set the Calendar instance as a tag on each dayView

            // Clear the previous highlight
            dateTextView.setBackgroundColor(Color.TRANSPARENT)
            dateTextView.setTextColor(Color.parseColor("#000000"))

            // Apply highlight if this day matches the selected date
            if (day.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR) &&
                day.get(Calendar.DAY_OF_YEAR) == selectedDate.get(Calendar.DAY_OF_YEAR)) {
                dateTextView.setBackgroundColor(Color.parseColor("#FFDD33"))  // Highlight background color
                dateTextView.setTextColor(Color.parseColor("#FFFFFF"))        // Text color for contrast
            }
        }
    }

}