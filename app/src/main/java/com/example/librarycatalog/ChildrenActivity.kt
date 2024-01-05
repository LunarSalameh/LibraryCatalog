package com.example.librarycatalog

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class ChildrenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_children)
        val BTN1: Button = findViewById(R.id.Button1)

        BTN1.setOnClickListener {
            val bookId = "1"
            val bookTitle = "The Very Hungry Caterpillar"
            val bookDescription = "A classic children's book about a caterpillar's journey of transformation."
            val bookPublishDate = "June 3, 1969"
            val bookAuthor = "Eric Carle"
            val bookImage = "@drawable/the_very_hungry_caterpillar.jpg"// Replace 1 with the actual book ID you want to pass
            val dialogFragment = DetailsFragment.newInstance(bookId,bookTitle)
            dialogFragment.show(supportFragmentManager, "Details Dialog")
        }

    }
}