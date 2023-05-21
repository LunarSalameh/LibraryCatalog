package com.example.librarycatalog

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        val SearchBTN: Button = findViewById(R.id.buttonSearch)

        SearchBTN.setOnClickListener{
            val intentCatalog = Intent(this@MainActivity, CatalogActivity::class.java)
            startActivity(intentCatalog)
        }
    }
}