package com.webie.structureup.viewmodel

// OS
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch

// Data types
import androidx.lifecycle.MutableLiveData

// References
import com.webie.structureup.model.DailyTask
import com.webie.structureup.StructureUp
import com.webie.structureup.utils.generateUniqueId


class DailyViewModel : ViewModel() {
    // UI
    private val _dailyTasks = MutableLiveData<MutableList<DailyTask>>(mutableListOf())
    val dailyTasks: LiveData<MutableList<DailyTask>> get() = _dailyTasks

    // Database
    private val dao = StructureUp.getDatabase().dailyTaskDao()

    fun loadDailyTasks() {
        StructureUp.coroutineScope.launch {
            val dailyTasks = dao.getAllDailyTasks().toMutableList() // Call the DAO to fetch users
            _dailyTasks.postValue(dailyTasks) // Update LiveDate
        }
    }

    fun addDailyTask(title: String) {
        val newDailyTask = DailyTask(id = generateUniqueId(), title = title)
        _dailyTasks.value?.add(newDailyTask)
        _dailyTasks.value = _dailyTasks.value // trigger LiveData observer

        // Database
        StructureUp.coroutineScope.launch {
            dao.insert(newDailyTask)
        }
    }

    fun deleteDailyTask(deletingDailyTask: DailyTask) {
        // UI
        _dailyTasks.value =
            _dailyTasks.value?.filter { it.id != deletingDailyTask.id }!!.toMutableList()  // trigger LiveData observer

        // Database
        StructureUp.coroutineScope.launch {
            dao.delete(deletingDailyTask)
        }
    }


}