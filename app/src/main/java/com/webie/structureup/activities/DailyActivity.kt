package com.webie.structureup.activities

// OS
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.webie.structureup.R

// UI
import android.widget.LinearLayout
import android.widget.TextView
import android.view.Gravity
import android.view.ViewGroup
import android.graphics.Color

class DailyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily)

        // data for the table
        val tableData = listOf("Sport", "Sprache", "Wissen", "Natur", "Gaming", "Coding")

        // reference to the LinearLayout container
        val tableContainer: LinearLayout = findViewById(R.id.table_container)

        // populate the table dynamically
        for (rowText in tableData) {
            val row = TextView(this)
            row.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 16, 0, 16) // Add some spacing between rows
            }
            row.text = rowText
            row.textSize = 18f
            row.setPadding(32, 32, 32, 32)
            row.setTextColor(Color.RED)
            row.gravity = Gravity.LEFT
            row.setBackgroundColor(Color.BLACK)

            // Add the row to the container
            tableContainer.addView(row)
        }
    }
}
