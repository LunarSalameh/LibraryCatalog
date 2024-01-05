package com.example.librarycatalog
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class DetailsFragment : DialogFragment() {

    companion object {
        fun newInstance(bookId: String, bookTitle: String): DetailsFragment {
            val fragment = DetailsFragment()
            val bundle = Bundle()
            bundle.putString("1", bookId)
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val arguments = requireArguments()
        val bookId = arguments.getInt("1")
        val book = getBookDetailsFromDatabase(bookId)

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(book.name)
        builder.setMessage(book.description)

        builder.setPositiveButton("OK", null)

        return builder.create()
    }

    private fun getBookDetailsFromDatabase(bookId: Int): Book {

        val projection = arrayOf(
            BookClass._ID,
            BookClass.Book_Name,
            BookClass.Book_Description,
            BookClass.Book_Publish_Date,
            BookClass.Book_Author,
            BookClass.Image_Link
        )
        val selection = "${BookClass._ID} = ?"
        val selectionArgs = arrayOf(bookId.toString())

        val cursor = context?.contentResolver?.query(
            BookClass.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )

        cursor?.use { cursor ->
            if (cursor.moveToFirst()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(BookClass._ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(BookClass.Book_Name))
                val description = cursor.getString(cursor.getColumnIndexOrThrow(BookClass.Book_Description))
                val publishDate = cursor.getString(cursor.getColumnIndexOrThrow(BookClass.Book_Publish_Date))
                val author = cursor.getString(cursor.getColumnIndexOrThrow(BookClass.Book_Author))
                val imageLink = cursor.getString(cursor.getColumnIndexOrThrow(BookClass.Image_Link))

                return Book(id, name, description, publishDate, author, imageLink)
            }
        }

        // Return a default book if no book found or an error occurred
        return Book(
            bookId,
            "Book Title Not Found",
            "Book Description Not Found",
            "Publish Date Not Found",
            "Author Not Found",
            "Image Link Not Found"
        )
    }


}
