package com.webie.structureup.adapters

// UI
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AlertDialog


// References
import com.webie.structureup.R
import com.webie.structureup.model.DailyTask
import com.webie.structureup.viewmodel.DailyViewModel

class DailyTaskAdapter(private val viewModel: DailyViewModel, private val onLongClick: (DailyTask) -> Unit) : RecyclerView.Adapter<DailyTaskAdapter.DailyTaskHolder>() {
    private var dailyTasks: List<DailyTask> = listOf()


    // ViewHolder class to hold references to the views
    inner class DailyTaskHolder(dailyTaskView: View) : RecyclerView.ViewHolder(dailyTaskView) {
        private val dailyTask: ConstraintLayout = itemView.findViewById(R.id.dailytask_container)
        private val dailyTaskName: TextView = itemView.findViewById((R.id.dailytask_title))

        fun bind(task: DailyTask) {
            dailyTaskName.text = task.title

            dailyTask.setOnLongClickListener {
                onLongClick(task)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyTaskHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.component_dailytask, parent, false)
        return DailyTaskHolder(itemView)
    }

    override fun onBindViewHolder(holder: DailyTaskHolder, position: Int) {
        val dailyTask = dailyTasks[position]
        holder.bind(dailyTask)
    }

    override fun getItemCount(): Int = dailyTasks.size

    fun refreshData(data: List<DailyTask>) {
        dailyTasks = data
        notifyDataSetChanged()
    }
}