package com.example.diceroller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class History : AppCompatActivity() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<History_Item>
    private lateinit var adapter: MyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val queue = Volley.newRequestQueue(this)
        val url = "https://615860ac5167ba00174bbac7.mockapi.io/results/Results"
        val JsonObjectRequest = JsonArrayRequest(Request.Method.GET, url, null,  Response.Listener { response -> InitializeRecyclerView(response) }, Response.ErrorListener {})
        queue.add(JsonObjectRequest)
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
        newRecyclerView.adapter = MyAdapter(Singleton.list)
    }

    fun InitializeRecyclerView(response: JSONArray) {
        if(!Singleton.initiated) {
            var n = 0
            while(n < response.length()) {
                var item = response.getJSONObject(n)
                Singleton.list.add(History_Item(
                    listOf<Int>(item.getInt("Dice_1"), item.getInt("Dice_2"), item.getInt("Dice_3"), item.getInt("Dice_4")),
                    item.getString("Roll_Type"),
                    item.getInt("Roll_Counter")))
                n++
            }
            Singleton.initiated = true
        }
        newRecyclerView = findViewById(R.id.rvHistoryList)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(false)
        newRecyclerView.adapter = MyAdapter(Singleton.list)
        adapter = MyAdapter(Singleton.list)
        setItemTouchHelper(adapter)
        var loading_screen: ConstraintLayout = findViewById(R.id.CL_loading_screen)
        var recyclerview_list: RecyclerView = findViewById(R.id.rvHistoryList)
        loading_screen.visibility = View.INVISIBLE
        recyclerview_list.visibility = View.VISIBLE
    }
}