package com.sabran.crudsqlitemahasiswa

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "StudentDB"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "students"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_NIM = "nim"
        const val COLUMN_MAJOR = "major"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_NAME TEXT," +
                "$COLUMN_NIM TEXT," +
                "$COLUMN_MAJOR TEXT)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Create
    fun insertStudent(name: String, nim: String, major: String): Long {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_NAME, name)
        contentValues.put(COLUMN_NIM, nim)
        contentValues.put(COLUMN_MAJOR, major)
        return db.insert(TABLE_NAME, null, contentValues)
    }

    // Read
    fun getAllStudents(): List<Map<String, String>> {
        val db = readableDatabase
        val students = mutableListOf<Map<String, String>>()
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        if (cursor.moveToFirst()) {
            do {
                val student = mapOf(
                    COLUMN_ID to cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)).toString(),
                    COLUMN_NAME to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    COLUMN_NIM to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NIM)),
                    COLUMN_MAJOR to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MAJOR))
                )
                students.add(student)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return students
    }

    // Update
    fun updateStudent(id: Int, name: String, nim: String, major: String): Int {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_NAME, name)
        contentValues.put(COLUMN_NIM, nim)
        contentValues.put(COLUMN_MAJOR, major)
        return db.update(TABLE_NAME, contentValues, "$COLUMN_ID=?", arrayOf(id.toString()))
    }

    // Delete
    fun deleteStudent(id: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(id.toString()))
    }
}
