package com.webie.structureup.adapters

// OS

// UI
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// References
import com.webie.structureup.R
import com.webie.structureup.model.TodoTask
import com.webie.structureup.viewmodel.TodoViewModel


class TodoTaskAdapter(private val viewModel: TodoViewModel) : RecyclerView.Adapter<TodoTaskAdapter.TodoTaskHolder>() {
    private var todoTasks: List<TodoTask> = listOf()


    // create new list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoTaskHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.component_todotask, parent, false)
        return TodoTaskHolder(view)
    }

    // bind element
    override fun onBindViewHolder(holder: TodoTaskHolder, position: Int) {
        val todoTask = todoTasks[position]
        holder.bind(todoTask)
    }

    // get count
    override fun getItemCount(): Int = todoTasks.size

    // refresh data
    fun refreshData(data: List<TodoTask>) {
        todoTasks = data
        notifyDataSetChanged()
    }

    // reference to list elements
    inner class TodoTaskHolder(todoTaskView: View) : RecyclerView.ViewHolder(todoTaskView) {
        // TODO outsource todoTaskView?
        private val titleTodoTaskView: TextView = itemView.findViewById(R.id.todotask_title)
        private val descriptionTodoTaskView: TextView = itemView.findViewById(R.id.todotask_description)
        private val checkBoxTodoTask: CheckBox = itemView.findViewById(R.id.todotask_checkbox)

        fun bind(task: TodoTask) {
            // TITLE
            titleTodoTaskView.text = task.title

            // DESCRIPTION
            descriptionTodoTaskView.text = task.description
            descriptionTodoTaskView.visibility = GONE

            // CHECK Box
            checkBoxTodoTask.isChecked = task.isCompleted

            // toggle task completion state
            checkBoxTodoTask.setOnClickListener  {
                viewModel.toggleTodoTask(task)
                true
            }

            // style changes depending on the state
            if (task.isCompleted) {
                titleTodoTaskView.alpha = 0.5f // dimmed
                titleTodoTaskView.paintFlags = titleTodoTaskView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                titleTodoTaskView.alpha = 1.0f
                titleTodoTaskView.paintFlags = titleTodoTaskView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }
    }
}