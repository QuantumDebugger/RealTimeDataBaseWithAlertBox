package com.example.realtimedatabasewithalertbox

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(
    private var itemData: ArrayList<ItemData>,
    var recyclerInterface: RecyclerInterface
) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    class ViewHolder(private var view: View) : RecyclerView.ViewHolder(view) {

        var simage: ImageView = view.findViewById(R.id.imageV)
        var sName: TextView = view.findViewById(R.id.nameTv)
        var sClass: TextView = view.findViewById(R.id.classTv)
        var sRollNumber: TextView = view.findViewById(R.id.rollNumberTv)
        var cardV: CardView = view.findViewById(R.id.cvCard)
        val addButton: Button = view.findViewById(R.id.materalButtonAdd)
        val delButton: Button = view.findViewById(R.id.materalButtonDelete)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_custom_view, parent, false)
        )


    }

    override fun getItemCount(): Int {
        return itemData.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.sName.setText(itemData[position].stdName)
        holder.sClass.setText(itemData[position].stdClass)
        holder.sRollNumber.setText(itemData[position].stdRollNumber.toString())

        holder.addButton.setOnClickListener {
            recyclerInterface.materalButtonAdd(position)
        }
        holder.delButton.setOnClickListener {
            recyclerInterface.materalButtonDelete(position)
        }


    }


}
