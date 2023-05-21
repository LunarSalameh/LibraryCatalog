package com.example.librarycatalog

import android.content.*
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.text.TextUtils


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
        const val Link = "URL"

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
                "CREATE TABLE $Children_Books_Table_NAME($_ID,$Book_Name,$Book_Description,$Book_Publish_Date,$Book_Author,$Link)"
                )

        private class DatabaseHelper internal constructor(context: Context?) :
            SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

            override fun onCreate(dp: SQLiteDatabase) {
                dp.execSQL(Children_Book_Table)
            }

            override fun onUpgrade(dp: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
                dp.execSQL("DROP TABLE IF EXISTS $Children_Books_Table_NAME ")
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
        return count
    }

}
