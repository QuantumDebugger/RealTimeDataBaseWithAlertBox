package com.example.realtimedatabasewithalertbox

import android.app.Dialog
import android.nfc.Tag
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.realtimedatabasewithalertbox.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity(), RecyclerInterface {
    private lateinit var binding: ActivityMainBinding
    private var dataArray = arrayListOf<ItemData>()
    var recyclerAdapter = RecyclerAdapter(dataArray, this)
    lateinit var linearLayoutManager: LinearLayoutManager
    // Fire Base (DATABASE REAL TIME)
    var dbReference : DatabaseReference = FirebaseDatabase.getInstance().reference




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


        // REALTIME FUNCTIONS IMPLEMENTATION ROW-------------
        dbReference.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                var itemModel : ItemData?= snapshot.getValue(ItemData::class.java)
                itemModel?.id = snapshot.key
                dataArray.add(itemModel!!)

                recyclerAdapter.notifyDataSetChanged()

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                var itemModel : ItemData?= snapshot.getValue(ItemData::class.java)
                itemModel?.id = snapshot.key

                if(itemModel != null){
                    dataArray.forEachIndexed { index, itemDataModel ->
                        if(itemDataModel.id == itemModel.id){

                            dataArray[index] = itemModel

                            recyclerAdapter.notifyDataSetChanged()

                        }



                    }

                }

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                var itemModel : ItemData?= snapshot.getValue(ItemData::class.java)
                itemModel?.id = snapshot.key
                if (itemModel != null){
                    dataArray.remove(itemModel)

                    recyclerAdapter.notifyDataSetChanged()
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        // for locally add data

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
                       // dataArray.add(ItemData(studentName,className,rollNo))  locally
                        var key = dbReference.push().key.toString() // database realtime
                        dbReference.child(key).setValue(ItemData(id = key, studentName, className, rollNo))

                        // recyclerAdapter.notifyDataSetChanged()// locally
                        dismiss()
                    }
                }


            }.show()
        }

        // to show on recycler view when data added

        binding.recyclerView.adapter = recyclerAdapter
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = linearLayoutManager

    }


    override fun materalButtonAdd(position: Int) {

        // for update locally code

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
                     val itemDataModel = ItemData(        // .set before ItemData and we are able to change only on recycler
                         id = dataArray[position].id,
                        stdName = nameET.text.toString(), stdClass = classET.text.toString(), stdRollNumber = rollNoET.text.toString())
                    val hashMap = itemDataModel.toMap()
                    dbReference.child(dataArray[position].id.toString()).updateChildren(hashMap)

                    //recyclerAdapter.notifyDataSetChanged() // locally
                    dismiss()
                }
            }


        }.show()

    }

    // locally update finish

    override fun materalButtonDelete(position: Int) {
        //dataArray.removeAt(position) // for delete locally

        //--------------Alert Box on Delete---------------------

        AlertDialog.Builder(this).apply {
            setTitle("Do you want to delete record :${dataArray[position].id}")
            setPositiveButton("YES"){_,_ ->

                dbReference.child(dataArray[position].id.toString()).removeValue() // for realtime database deletion

            }
            setNegativeButton("NO"){dialog,_ ->
                dialog.dismiss()


            }

        }.show()





        //recyclerAdapter.notifyDataSetChanged() locally

    }
}