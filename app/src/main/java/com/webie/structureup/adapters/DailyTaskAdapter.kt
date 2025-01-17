package com.webie.structureup.adapters

// UI
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// References
import com.webie.structureup.R
import com.webie.structureup.model.DailyTask
import com.webie.structureup.model.TodoTask
import com.webie.structureup.viewmodel.DailyViewModel

class DailyTaskAdapter(private val viewModel: DailyViewModel) : RecyclerView.Adapter<DailyTaskAdapter.DailyTaskHolder>() {
    private var dailyTasks: List<DailyTask> = listOf()

    // ViewHolder class to hold references to the views
    inner class DailyTaskHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dailyTaskName: TextView = view.findViewById(R.id.dailytask_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyTaskHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.component_dailytask, parent, false)
        return DailyTaskHolder(itemView)
    }

    override fun onBindViewHolder(holder: DailyTaskHolder, position: Int) {
        val dailyTask = dailyTasks[position]
        holder.dailyTaskName.text = dailyTask.title
    }

    override fun getItemCount(): Int = dailyTasks.size

    fun refreshData(data: List<DailyTask>) {
        dailyTasks = data
        notifyDataSetChanged()
    }
}