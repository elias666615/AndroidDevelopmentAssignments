package com.example.realtimedatabase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecordAdapter(private val dataSet: ArrayList<DiceRecord>): RecyclerView.Adapter<RecordAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username: TextView = view.findViewById(R.id.tv_rec_username)
        val dice_numbers: TextView = view.findViewById(R.id.tv_rec_dice)
        val timestamp: TextView = view.findViewById(R.id.tv_rec_timestamp)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.record, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val record = dataSet[position]
        viewHolder.username.setText(record.username)
        viewHolder.dice_numbers.setText(record.dice1.toString() + " - " + record.dice2.toString() + " - " + record.dice3.toString() + " - " + record.dice4.toString())
        viewHolder.timestamp.setText(record.timestamp)
    }

    override fun getItemCount() = dataSet.size
}