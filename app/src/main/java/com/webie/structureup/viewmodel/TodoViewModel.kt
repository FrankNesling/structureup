package com.webie.structureup.viewmodel

// OS
import android.util.Log
import androidx.lifecycle.ViewModel

// Database (coroutines)
import kotlinx.coroutines.launch
import kotlinx.coroutines.async

// Data Types
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

// References
import com.webie.structureup.StructureUp
import com.webie.structureup.model.DailyTask
import com.webie.structureup.model.TodoTask
import com.webie.structureup.utils.generateUniqueId
import com.webie.structureup.utils.getStartOfDayInMilliSec

// Calendar
import java.util.Calendar
import java.util.Date

class TodoViewModel : ViewModel() {
    // UI
    private val _todoTasks = MutableLiveData<MutableList<TodoTask>>(mutableListOf())
    val todoTasks: LiveData<MutableList<TodoTask>> get() = _todoTasks

    // Database
    private val dao = StructureUp.getDatabase().todoTaskDao()
    private val dailyDao = StructureUp.getDatabase().dailyTaskDao()

    // Calendar
    private val _selectedDate = MutableLiveData(Calendar.getInstance())  // today
    val selectedDate: LiveData<Calendar> get() = _selectedDate

    private val _weekDays = MutableLiveData<List<Calendar>>()
    val weekDays: LiveData<List<Calendar>> get() = _weekDays


    /* CALENDAR */
    fun loadWeekDays() {
        val daysInWeek = mutableListOf<Calendar>()
        val calendar = Calendar.getInstance()
        calendar.time = _selectedDate.value?.time ?: Date()

        // Set calendar to the Monday of the current week
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        // Add each day from Monday to Sunday
        for (i in 0..6) {
            val day = Calendar.getInstance()
            day.time = calendar.time
            daysInWeek.add(day)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        _weekDays.value = daysInWeek
    }

    fun setSelectedDate(date: Calendar) {
        _selectedDate.value = date
        loadWeekDays()  // Refresh week days based on the new selected date
        loadTodoTasksForDate(getStartOfDayInMilliSec(date))
    }

    fun goToNextDay() {
        val updatedDate = _selectedDate.value?.clone() as Calendar
        updatedDate.add(Calendar.DAY_OF_YEAR, 1)
        setSelectedDate(updatedDate)
    }

    fun goToPreviousDay() {
        val updatedDate = _selectedDate.value?.clone() as Calendar
        updatedDate.add(Calendar.DAY_OF_YEAR, -1)
        setSelectedDate(updatedDate)
    }



    /* TO-DO LIST */
    // Load
    fun loadTodoTasks() {
        StructureUp.coroutineScope.launch {
            _todoTasks.postValue(dao.getAllTodoTasks().toMutableList())
        }
    }

    fun loadTodoTasksForDate(date: Long) {
        StructureUp.coroutineScope.launch {
            _todoTasks.postValue(dao.getTasksForDate(date).toMutableList())
        }
    }

    // Add
    fun addTodoTask(newTodoTask: TodoTask) {
        _todoTasks.value?.add(newTodoTask)
        _todoTasks.value = _todoTasks.value // trigger LiveData observer

        // Database
        StructureUp.coroutineScope.launch {
            dao.insert(newTodoTask)
        }
    }

    // Update
    fun updateTodoTask(updatedTodoTask: TodoTask) {
        StructureUp.coroutineScope.launch {
            dao.update(updatedTodoTask)
        }

        // UI
        _todoTasks.value = _todoTasks.value?.map {  // trigger LiveData observer
            if (it.id == updatedTodoTask.id) updatedTodoTask else it
        }!!.toMutableList()
    }

    // Delete
    fun deleteTodoTask(deletingTodoTask: TodoTask) {
        // UI
        _todoTasks.value =
            _todoTasks.value?.filter { it.id != deletingTodoTask.id }!!.toMutableList()  // trigger LiveData observer

        // Database
        StructureUp.coroutineScope.launch {
            dao.delete(deletingTodoTask)
        }
    }

    // Checkbox
    fun toggleTodoTask(todoTask: TodoTask) {
        // Database
        val updatedTask = todoTask.copy(isCompleted = !todoTask.isCompleted)
        StructureUp.coroutineScope.launch {
            dao.update(updatedTask)
        }

        // UI
        _todoTasks.value = _todoTasks.value?.map {  // trigger LiveData observer
            if (it.id == todoTask.id) updatedTask else it
        }!!.toMutableList()
    }

    /* Database */
    fun checkAndAddTasksForToday() {
        val todayTimestamp = getStartOfDayInMilliSec(Calendar.getInstance())


        val insertJob = StructureUp.coroutineScope.launch {
            // Check if tasks for today already exist
            val existingTasks = dao.getTasksForDate(todayTimestamp)
            Log.d("FOUNDYOU", existingTasks.size.toString())
            if (existingTasks.isEmpty()) {
                // No tasks for today, add new task
                val tasksToAdd = getTasksForToday(todayTimestamp)
                dao.insertTasks(tasksToAdd)
            }
            loadTodoTasksForDate(todayTimestamp)
        }
    }

    private suspend fun getTasksForToday(date: Long): List<TodoTask> {
        var dailyTasksFromDB = emptyList<DailyTask>()
        var todoTasks = mutableListOf<TodoTask>()

        val getJob = StructureUp.coroutineScope.async {
            dailyTasksFromDB = dailyDao.getAllDailyTasks()
        }.await()

        for (dailyTask in dailyTasksFromDB) {
            todoTasks.add(TodoTask(id = generateUniqueId(), date = date, title = dailyTask.title))
        }

        return todoTasks.toList()

    }
}