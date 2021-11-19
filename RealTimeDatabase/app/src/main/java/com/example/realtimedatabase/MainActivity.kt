package com.example.realtimedatabase

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.random.Random


object RecordService {
    var records = ArrayList<DiceRecord>()
    var record_ids = ArrayList<String>()
    fun populateList(Records: HashMap<String, HashMap<String, HashMap<String, String>>>, adapter: RecordAdapter) {
        for ((key, value) in Records.get("records")?.entries!!) {
            if (!record_ids.contains(key)) {
                records.add(DiceRecord(value.get("username"),
                    value.get("dice1").toString(),
                    value.get("dice2").toString(),
                    value.get("dice3").toString(),
                    value.get("dice4").toString()))
                record_ids.add(key)
                adapter.notifyDataSetChanged()
            }
        }
    }
}

class MainActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    private lateinit var dice1: TextView
    private lateinit var dice2: TextView
    private lateinit var dice3: TextView
    private lateinit var dice4: TextView

    private lateinit var RecordRecyclerView: RecyclerView

    private lateinit var adapter: RecordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Write a message to the database
        database = Firebase.database.reference

        // Read from the database
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.value
                if(value != null) {
                    val records = value as HashMap<String, HashMap<String, HashMap<String, String>>>
                    RecordService.populateList(records, adapter)
                }


                //Log.d(TAG, "Value is: $value")
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
        dice1 = findViewById(R.id.tv_dice1)
        dice2 = findViewById(R.id.tv_dice2)
        dice3 = findViewById(R.id.tv_dice3)
        dice4 = findViewById(R.id.tv_dice4)

        adapter = RecordAdapter(RecordService.records)

        RecordRecyclerView = findViewById(R.id.rv_recordList)
        RecordRecyclerView.layoutManager = LinearLayoutManager(this)
        RecordRecyclerView.adapter = adapter
    }

    fun rollDice(view: View) {
        val et_username = findViewById<EditText>(R.id.et_username)
        if(!et_username.text.isNotEmpty()) {
            Toast.makeText(this, "the username is required", Toast.LENGTH_SHORT).show()
        }
        else {
            val _dice1: Int = Random.nextInt(1, 7)
            val _dice2: Int = Random.nextInt(1, 7)
            val _dice3: Int = Random.nextInt(1, 7)
            val _dice4: Int = Random.nextInt(1, 7)

            dice1.setText(_dice1.toString())
            dice2.setText(_dice2.toString())
            dice3.setText(_dice3.toString())
            dice4.setText(_dice4.toString())

            addRecord(et_username.text.toString(), _dice1.toString(), _dice2.toString(), _dice3.toString(), _dice4.toString())
        }
    }

    private fun generateRandomId(): String {
        val id_characters: List<String> = listOf("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
                                                 "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3",
                                                 "4", "5", "6", "7", "8", "9")
        println(id_characters.size)
        var id: String = ""
        for(i in 1..10) {
            id += id_characters[Random.nextInt(0, 36)]
        }
        return id
    }

    private fun addRecord(username: String, dice1: String, dice2: String, dice3: String, dice4: String) {
        val record = DiceRecord(username, dice1, dice2, dice3, dice4)
        database.child("records").child(generateRandomId()).setValue(record)
    }
}