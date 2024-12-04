package com.example.realtimedatabasewithalertbox

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.realtimedatabasewithalertbox.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), RecyclerInterface {
    private lateinit var binding: ActivityMainBinding
    private var dataArray = arrayListOf<ItemData>()
    var recyclerAdapter = RecyclerAdapter(dataArray, this)
    lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.fabBtn.setOnClickListener {
            Dialog(this).apply {
                setContentView(R.layout.custom_layout)
                window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                val sNameEt = findViewById<EditText>(R.id.nameEt)
                val classEt = findViewById<EditText>(R.id.classEt)
                val rollNoEt = findViewById<EditText>(R.id.rollNumberEt)
                val addBtn = findViewById<Button>(R.id.btnUpdate)

                addBtn.setOnClickListener {
                    if (sNameEt.text.trim().isNullOrEmpty()) {
                        sNameEt.error = "enter student name"
                    } else if (classEt.text.trim().isNullOrEmpty()) {
                        classEt.error = "enter class name"
                    } else if (rollNoEt.text.trim().isNullOrEmpty()) {
                        rollNoEt.error = "enter roll number"
                    }else{
                        val studentName = sNameEt.text.toString()
                        val className = classEt.text.toString()
                        val rollNo = rollNoEt.text.toString()
                        dataArray.add(ItemData(studentName,className,rollNo))
                        recyclerAdapter.notifyDataSetChanged()
                        dismiss()
                    }
                }


            }.show()
        }

        binding.recyclerView.adapter = recyclerAdapter
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = linearLayoutManager

    }


    override fun materalButtonAdd(position: Int) {
        Toast.makeText(this, "$position", Toast.LENGTH_SHORT).show()
        Dialog(this).apply {
            setContentView(R.layout.custom_layout)
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            val nameET = findViewById<EditText>(R.id.nameEt)
            val classET = findViewById<EditText>(R.id.classEt)
            val rollNoET = findViewById<EditText>(R.id.rollNumberEt)
            val update = findViewById<Button>(R.id.btnUpdate)

            val nameData = dataArray[position].stdName
            val classData = dataArray[position].stdClass
            val rollNoData = dataArray[position].stdRollNumber


            nameET.setText(nameData)
            classET.setText(classData)
            rollNoET.setText(rollNoData)

            update.setOnClickListener {
                if (nameET.text.isNullOrEmpty()) {
                    nameET.error = "Enter Name"
                } else if (classET.text.isNullOrEmpty()) {
                    classET.error = "Enter Class"
                } else if (rollNoET.text.isNullOrEmpty()) {
                    rollNoET.error = "Enter Roll Number"
                } else {
                    dataArray.set(position,ItemData(nameET.text.toString(),classET.text.toString(),rollNoET.text.toString()))
                    recyclerAdapter.notifyDataSetChanged()
                    dismiss()
                }
            }


        }.show()

    }

    override fun materalButtonDelete(position: Int) {
        dataArray.removeAt(position)
        recyclerAdapter.notifyDataSetChanged()

    }
}