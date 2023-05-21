package com.example.librarycatalog

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast

class ChildrenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_children)

        val Description1: TextView = findViewById(R.id.DescriptionBook1)

        fun onClickAddName(view: View?) {
            val values = ContentValues()
            values.put(
                BookClass.Book_Name,
                (findViewById<View>(R.id.BookTitle1) as TextView).text.toString()
            )
            values.put(
                BookClass.Book_Description,
                (findViewById<View>(R.id.DescriptionBook1) as TextView).text.toString()
            )
            val uri = contentResolver.insert(
                BookClass.CONTENT_URI, values
            )
            Toast.makeText(baseContext, uri.toString(), Toast.LENGTH_LONG).show()
        }

    }
}