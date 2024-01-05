package com.example.librarycatalog

import android.content.*
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.text.TextUtils
import com.example.librarycatalog.BookClass.Companion.Book


class BookClass : ContentProvider() {

    companion object {

        val AUTHORITY = "com.example.provider.BookClass"
        private val URL = "content://$AUTHORITY/Books"
        val CONTENT_URI: Uri = Uri.parse(URL)

        // database content
        const val _ID = "_id"
        const val Book_Name = "Name"
        const val Book_Description = "Description"
        const val Book_Publish_Date = "Date"
        const val Book_Author = "Author"
        const val Image_Link = "Image Link"

        private val LIBRARY_PROJECTION_MAP: HashMap<String, String>? = null// for database

        val Book = 1
        val Book_ID = 2

        // to check is it a string or not
        private var B_UriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            B_UriMatcher.addURI(AUTHORITY, "Book", Book)
            B_UriMatcher.addURI(AUTHORITY, "Book/#", Book_ID)
        }

        //Creating DataBase
        private lateinit var db: SQLiteDatabase; //private
        const val DATABASE_NAME = "Books"
        const val DATABASE_VERSION = 1
        const val Children_Books_Table_NAME = "Children_Books"

        const val Children_Book_Table = (
                "CREATE TABLE $Children_Books_Table_NAME($_ID,$Book_Name,$Book_Description,$Book_Publish_Date,$Book_Author,$Image_Link)"
                )

        private class DatabaseHelper internal constructor(context: Context?) :
            SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

            override fun onCreate(dp: SQLiteDatabase) {
                db = dp
                //dp.execSQL("DROP TABLE IF EXISTS $Children_Books_Table_NAME")
                val query = "SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = '$Children_Books_Table_NAME'"
                val cursor = db.rawQuery(query, null)
                if (cursor != null && cursor.count > 0) {
                    cursor.close()
                    return
                }
                cursor.close()
                dp.execSQL(Children_Book_Table)
                // initializing data
                val bookData = arrayOf(
                    ContentValues().apply {
                        put(_ID,"1")
                        put(Book_Name, "The Very Hungry Caterpillar")
                        put(Book_Description, "A classic children's book about a caterpillar's journey of transformation.")
                        put(Book_Publish_Date, "June 3, 1969")
                        put(Book_Author, "Eric Carle")
                        put(Image_Link, "@drawable/the_very_hungry_caterpillar.jpg")
                    },
                    ContentValues().apply {
                        put(_ID,"2")
                        put(Book_Name, "Goodnight Moon")
                        put(Book_Description, "A soothing bedtime story with beautiful illustrations.")
                        put(Book_Publish_Date, "September 3, 1947")
                        put(Book_Author, "Margaret Wise Brown")
                        put(Image_Link, "@drawable/goodnight_moon.jpg")
                    },
                    ContentValues().apply {
                        put(_ID,"3")
                        put(Book_Name, "Where the Wild Things Are")
                        put(Book_Description, "A story about a young boy named Max who travels to a land of wild creatures.")
                        put(Book_Publish_Date, "April 19, 1963")
                        put(Book_Author, "Maurice Sendak")
                        put(Image_Link, "@drawable/where_the_wild_things_are.jpg")
                    },
                    ContentValues().apply {
                        put(_ID,"4")
                        put(Book_Name, "The Giving Tree")
                        put(Book_Description, "A heartwarming tale of a tree's selfless love for a boy.")
                        put(Book_Publish_Date, "October 7, 1964")
                        put(Book_Author, "Shel Silverstein")
                        put(Image_Link, "@drawable/the_giving_tree.jpg")
                    },
                    ContentValues().apply {
                        put(_ID,"5")
                        put(Book_Name, "Oh, the Places You'll Go!")
                        put(Book_Description, "Dr. Seuss's uplifting book about the journey of life.")
                        put(Book_Publish_Date, "January 22, 1990")
                        put(Book_Author, "Dr. Seuss")
                        put(Image_Link, "@drawable/oh_the_places_you_ll_go_.jpg")
                    }
                )
                for (book in bookData) {
                    db.insert(Children_Books_Table_NAME, null, book)
                }
            }

            override fun onUpgrade(dp: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
                dp.execSQL("DROP TABLE IF EXISTS $Children_Books_Table_NAME")
                onCreate(dp)
            }
        }

    }


    override fun getType(uri: Uri): String {
        val book_Uri = B_UriMatcher.match(uri)

        if (book_Uri != -1) {
            return when (book_Uri) {
                Book -> "Info about Book"
                Book_ID -> "Info About specific Book"
                else -> throw IllegalArgumentException("Unknown URI $uri")
            }
        } else
            throw IllegalArgumentException("Unknown URI $uri")
    }


    // CALLING THE CREATED DATABASE
    override fun onCreate(): Boolean {
        val context = context
        val dbHelper = DatabaseHelper(context)
        db = dbHelper.writableDatabase
        dbHelper.onCreate(db)
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        val rowID = db.insert(Children_Books_Table_NAME, "", values)

        if (rowID > 0) {
            val uri1 = ContentUris.withAppendedId(CONTENT_URI, rowID)
            context?.contentResolver?.notifyChange(uri1, null)
            return uri1
        }
        throw SQLException("Failed to add a record info$uri")
    }

    // reading DataBase
    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        val ChildrenBookUri = B_UriMatcher.match(uri)
        var sortOrder = sortOrder
        val qb = SQLiteQueryBuilder()
        if (ChildrenBookUri != -1) {
            qb.tables = Children_Books_Table_NAME
            when (ChildrenBookUri) {
                Book_ID -> qb.appendWhere(_ID + "=" + uri.pathSegments[1])
                else -> null
            }
        }
        if (sortOrder == null || sortOrder === "") {
            sortOrder = _ID
        }

        val query = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder)
        query.setNotificationUri(context!!.contentResolver, uri)
        return query
    }


    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        var count = 0
        count = db.delete(Children_Books_Table_NAME, selection, selectionArgs)
        context?.contentResolver?.notifyChange(uri, null)
        return count
    }


    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        var count = 0;
        val ChildrenBookUri = B_UriMatcher.match(uri)
        if (ChildrenBookUri != -1) {
            // Delete from questions table
            when (ChildrenBookUri) {
                Book -> count = db.update(
                    Children_Books_Table_NAME, values, selection,
                    selectionArgs
                )
                Book_ID -> {
                    val id = uri.pathSegments[1]
                    count = db.update(
                        Children_Books_Table_NAME,
                        values,
                        _ID + " = " + id + if (!TextUtils.isEmpty(selection)) " AND ($selection)" else "",
                        selectionArgs,
                    )
                }
                else -> throw IllegalArgumentException("Unknown URI $uri")
            }

        }
        context!!.contentResolver.notifyChange(uri, null)

        refreshData()

        return count
    }

    private fun refreshData() {
        // Re-query the database to retrieve the updated data
        val projection = arrayOf(
            BookClass._ID,
            BookClass.Book_Name,
            BookClass.Book_Description,
            BookClass.Book_Publish_Date,
            BookClass.Book_Author,
            BookClass.Image_Link
        )

        val cursor = context?.contentResolver?.query(
            BookClass.CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        // Update your app's data structures or UI components with the new data
        if (cursor != null) {
            // Clear the existing data structures or UI components and repopulate them with the new data
            // For example, if you have a list of books, you can clear the list and add the updated books from the cursor

            // Clear existing data structures or UI components

            // Repopulate with new data
            val updatedBooks = mutableListOf<Book>()

            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(BookClass._ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(BookClass.Book_Name))
                val description = cursor.getString(cursor.getColumnIndexOrThrow(BookClass.Book_Description))
                val publishDate = cursor.getString(cursor.getColumnIndexOrThrow(BookClass.Book_Publish_Date))
                val author = cursor.getString(cursor.getColumnIndexOrThrow(BookClass.Book_Author))
                val imageLink = cursor.getString(cursor.getColumnIndexOrThrow(BookClass.Image_Link))

                val book = Book(id, name, description, publishDate, author, imageLink)
                updatedBooks.add(book)
            }

            // Update your app's data structures or UI components with the updatedBooks list

            // Close the cursor
            cursor.close()
        }
    }

}