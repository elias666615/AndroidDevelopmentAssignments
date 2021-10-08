package com.example.diceroller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val historyList: ArrayList<History_Item>): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    private var urlList = listOf<Int>(R.drawable.dice0, R.drawable.dice1, R.drawable.dice2, R.drawable.dice3, R.drawable.dice4, R.drawable.dice5, R.drawable.dice6)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = historyList[position]
        holder.rollCounter.setText(currentItem.rollCounter.toString())
        holder.rollType.setText(currentItem.rollType.toString())
        var n = 0
        while(n < currentItem.numList.size) {
            holder.imageList[n].visibility = View.VISIBLE
            holder.imageList[n].setImageResource(this.urlList[currentItem.numList[n]])
            n++
        }
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val rollType: TextView = itemView.findViewById<TextView>(R.id.tvRollType)
        val rollCounter: TextView = itemView.findViewById<TextView>(R.id.tvRollCounter)
        var imageList = listOf<ImageView>(
            itemView.findViewById<ImageView>(R.id.imageDice1),
            itemView.findViewById<ImageView>(R.id.imageDice2),
            itemView.findViewById<ImageView>(R.id.imageDice3),
            itemView.findViewById<ImageView>(R.id.imageDice4))
    }

    fun deleteItem(viewHolder: RecyclerView.ViewHolder) {
        historyList.removeAt(viewHolder.adapterPosition)
        notifyItemRemoved(viewHolder.adapterPosition)
    }
}