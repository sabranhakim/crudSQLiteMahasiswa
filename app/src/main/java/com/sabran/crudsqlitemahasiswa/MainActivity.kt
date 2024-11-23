package com.sabran.crudsqlitemahasiswa

import android.app.AlertDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private var selectedStudentId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)

        val etName = findViewById<EditText>(R.id.etName)
        val etNim = findViewById<EditText>(R.id.etNim)
        val etMajor = findViewById<EditText>(R.id.etMajor)
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)
        val btnDelete = findViewById<Button>(R.id.btnDelete)
        val btnView = findViewById<Button>(R.id.btnView)
        val lvStudents = findViewById<ListView>(R.id.lvStudents)

        btnAdd.setOnClickListener {
            val name = etName.text.toString()
            val nim = etNim.text.toString()
            val major = etMajor.text.toString()

            if (name.isBlank() || nim.isBlank() || major.isBlank()) {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
            } else {
                databaseHelper.insertStudent(name, nim, major)
                Toast.makeText(this, "Student added successfully!", Toast.LENGTH_SHORT).show()
                refreshStudentList(lvStudents)
                clearFields(etName, etNim, etMajor)
            }
        }

        btnView.setOnClickListener {
            refreshStudentList(lvStudents)
        }

        lvStudents.setOnItemClickListener { _, _, position, _ ->
            val students = databaseHelper.getAllStudents()
            val student = students[position]
            selectedStudentId = student[DatabaseHelper.COLUMN_ID]?.toInt()
            etName.setText(student[DatabaseHelper.COLUMN_NAME])
            etNim.setText(student[DatabaseHelper.COLUMN_NIM])
            etMajor.setText(student[DatabaseHelper.COLUMN_MAJOR])
        }

        btnUpdate.setOnClickListener {
            val name = etName.text.toString()
            val nim = etNim.text.toString()
            val major = etMajor.text.toString()

            if (selectedStudentId == null) {
                Toast.makeText(this, "Select a student first!", Toast.LENGTH_SHORT).show()
            } else if (name.isBlank() || nim.isBlank() || major.isBlank()) {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
            } else {
                databaseHelper.updateStudent(selectedStudentId!!, name, nim, major)
                Toast.makeText(this, "Student updated successfully!", Toast.LENGTH_SHORT).show()
                refreshStudentList(lvStudents)
                clearFields(etName, etNim, etMajor)
            }
        }

        btnDelete.setOnClickListener {
            if (selectedStudentId == null) {
                Toast.makeText(this, "Select a student first!", Toast.LENGTH_SHORT).show()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Delete Student")
                    .setMessage("Are you sure you want to delete this student?")
                    .setPositiveButton("Yes") { _, _ ->
                        databaseHelper.deleteStudent(selectedStudentId!!)
                        Toast.makeText(this, "Student deleted successfully!", Toast.LENGTH_SHORT).show()
                        refreshStudentList(lvStudents)
                        clearFields(etName, etNim, etMajor)
                    }
                    .setNegativeButton("No", null)
                    .show()
            }
        }
    }

    private fun refreshStudentList(listView: ListView) {
        val students = databaseHelper.getAllStudents()
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            students.map { student ->
                "Nama: ${student[DatabaseHelper.COLUMN_NAME]}\n" +
                        "NIM: ${student[DatabaseHelper.COLUMN_NIM]}\n" +
                        "Jurusan: ${student[DatabaseHelper.COLUMN_MAJOR]}"
            }
        )
        listView.adapter = adapter
    }


    private fun clearFields(vararg fields: EditText) {
        fields.forEach { it.text.clear() }
        selectedStudentId = null
    }
}
