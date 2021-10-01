package com.example.diceroller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class History : AppCompatActivity() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<History_Item>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        newRecyclerView = findViewById(R.id.rvHistoryList)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(false)
        //newArrayList = intent.getSerializableExtra("history") as ArrayList<History_Item>
        newArrayList = Singleton.list
        newRecyclerView.adapter = MyAdapter(newArrayList)
        val adapter = MyAdapter(newArrayList)
        setItemTouchHelper(adapter)
    }

    private fun setItemTouchHelper(adapter: MyAdapter) {
        ItemTouchHelper(object: ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = 0
                val swipeFlags = ItemTouchHelper.LEFT
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if(4 == direction) {
                    adapter.deleteItem(viewHolder)
                }
            }

        }).apply {
            attachToRecyclerView(newRecyclerView)
        }
    }

    fun deleteAll(btnDeleteAll: View) {
        Singleton.deleteAll()
        newRecyclerView.adapter = MyAdapter(newArrayList)
    }
}