package com.example.librarycatalog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class CatalogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog)

        val eduBTN :Button = findViewById(R.id.Educational_Category)
        val poetryBTN :Button = findViewById(R.id.Poetry_Category)
        val childBTN :Button = findViewById(R.id.Children_Category)
        val novelBTN :Button = findViewById(R.id.Novels_Category)
        val comicBTN :Button = findViewById(R.id.Comic_Category)

        eduBTN.setOnClickListener{
            val intent_Catalog = Intent(this, EducationActivity::class.java)
            startActivity(intent_Catalog)
        }

        poetryBTN.setOnClickListener{
            val intent_Catalog = Intent(this, PoetryActivity::class.java)
            startActivity(intent_Catalog)
        }

        childBTN.setOnClickListener{
            val intent_Catalog = Intent(this, ChildrenActivity::class.java)
            startActivity(intent_Catalog)
        }

        novelBTN.setOnClickListener{
            val intent_Catalog = Intent(this, NovelsActivity::class.java)
            startActivity(intent_Catalog)
        }

        comicBTN.setOnClickListener{
            val intent_Catalog = Intent(this, ComicActivity::class.java)
            startActivity(intent_Catalog)
        }
    }
}