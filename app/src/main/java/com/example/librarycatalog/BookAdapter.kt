package com.example.librarycatalog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class BookAdapter(private val books: List<Book>) : BaseAdapter() {
    override fun getCount(): Int {
        return books.size
    }

    override fun getItem(position: Int): Any {
        return books[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(parent.context).inflate(R.layout.activity_children, parent, false)
        val book = books[position]

        val titleTextView: TextView = view.findViewById(R.id.BookTitle1)
        val descriptionTextView: TextView = view.findViewById(R.id.DescriptionBook1)


        titleTextView.text = book.name
        descriptionTextView.text = book.description

        return view
    }
}