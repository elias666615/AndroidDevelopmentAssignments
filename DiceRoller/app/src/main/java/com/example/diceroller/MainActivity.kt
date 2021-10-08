package com.example.diceroller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import java.io.Serializable
import kotlin.random.Random
import org.json.JSONException

import org.json.JSONObject




data class History_Item(var numList: List<Int>, var rollType: String, var rollCounter: Int) : Serializable

object Singleton{
    var list = ArrayList<History_Item>()
    var initiated: Boolean = false
    fun deleteAll(){
        list.clear()
    }
}

class MainActivity : AppCompatActivity() {
    var numbers = mutableListOf<Int>()
    private var rollcounter = 1
    private var diceRolled = 0
    private var urlList = listOf<Int>(R.drawable.dice0, R.drawable.dice1, R.drawable.dice2, R.drawable.dice3, R.drawable.dice4, R.drawable.dice5, R.drawable.dice6)
    private lateinit var ImageList: List<ImageView>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tvCounter = findViewById<TextView>(R.id.tvRollCounter)
        tvCounter.setText(this.rollcounter.toString())
        ImageList = listOf<ImageView>(findViewById(R.id.imgDice1), findViewById(R.id.imgDice2), findViewById(R.id.imgDice3), findViewById(R.id.imgDice4))
    }

    fun onIncrementCounter(btnIncrement: View) {
        this.numbers.clear()
        val btnDecrement = findViewById<Button>(R.id.btnDecrement)
        val tvCounter = findViewById<TextView>(R.id.tvRollCounter)
        this.rollcounter++
        if(this.rollcounter >= 4) {
            btnIncrement.setEnabled(false)
        }
        else if(this.rollcounter > 1) {
            btnDecrement.setEnabled(true)
        }
        tvCounter.setText(this.rollcounter.toString())
        this.ImageList[rollcounter-1].visibility = View.VISIBLE
        this.ImageList[rollcounter-1].setImageResource(urlList[0])
    }

    fun onDecrementCounter(btnDecrement: View) {
        this.numbers.clear()
        this.ImageList[rollcounter-1].visibility = View.INVISIBLE
        this.ImageList[rollcounter-1].setImageResource(urlList[0])
        val btnIncrement = findViewById<Button>(R.id.btnIncrement)
        val tvCounter = findViewById<TextView>(R.id.tvRollCounter)
        this.rollcounter--
        if(this.rollcounter <= 1) {
            btnDecrement.setEnabled(false)
        }
        else if(this.rollcounter < 4) {
            btnIncrement.setEnabled(true)
        }
        tvCounter.setText(this.rollcounter.toString())
    }

    fun rollAllDice(btnRollAllDice: View) {
        this.numbers.clear()
        var numbers = mutableListOf<Int>()
        var counter = 0
        for(image in ImageList) {
            image.visibility = View.INVISIBLE
        }
        while(counter < this.rollcounter) {
            val diceNumber = Random.nextInt(1, 7)
            numbers.add(diceNumber)
            ImageList[counter].setImageResource(this.urlList[diceNumber])
            ImageList[counter].visibility = View.VISIBLE
            counter++
        }
        val queue = Volley.newRequestQueue(this)
        val Posturl = "https://615860ac5167ba00174bbac7.mockapi.io/results/Results"
        val postData = JSONObject()
        try {
            if(rollcounter >= 1) postData.put("Dice_1", numbers[0])
            else postData.put("Dice_1", 0)
            if(rollcounter >= 2) postData.put("Dice_2", numbers[1])
            else postData.put("Dice_2", 0)
            if(rollcounter >= 3) postData.put("Dice_3", numbers[2])
            else postData.put("Dice_3", 0)
            if(rollcounter >= 4) postData.put("Dice_4", numbers[3])
            else postData.put("Dice_4", 0)
            postData.put("Roll_Type", "Roll All")
            postData.put("Roll_Counter", rollcounter)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        println(postData)
        val JsonObjectRequest = JsonObjectRequest(Request.Method.POST, Posturl, postData,
            Response.Listener { response -> println(response) },
            Response.ErrorListener { println("************ FAILED ************") })
        queue.add(JsonObjectRequest)
        if(Singleton.initiated) Singleton.list.add(History_Item(numbers, "Roll All Dice", this.rollcounter))
        this.diceRolled = 0
    }

    fun rollNextDice(btnRollNextDice: View) {
        if(this.diceRolled == 0 || this.diceRolled >= this.rollcounter) {
            for(image in ImageList) {
                image.setImageResource(urlList[0])
            }
            this.diceRolled = 0
        }
        var diceNumber = Random.nextInt(1, 7)
        numbers.add(diceNumber)
        ImageList[diceRolled].setImageResource(this.urlList[diceNumber])
        ImageList[diceRolled].visibility = View.VISIBLE
        this.diceRolled++
        val queue = Volley.newRequestQueue(this)
        val Posturl = "https://615860ac5167ba00174bbac7.mockapi.io/results/Results"
        val postData = JSONObject()
        try {
            if(diceRolled >= 1 && rollcounter >= 1) postData.put("Dice_1", numbers[0])
            else postData.put("Dice_1", 0)
            if(diceRolled >= 2 && rollcounter >= 2) postData.put("Dice_2", numbers[1])
            else postData.put("Dice_2", 0)
            if(diceRolled >= 3 && rollcounter >= 3) postData.put("Dice_3", numbers[2])
            else postData.put("Dice_3", 0)
            if(diceRolled >= 4 && rollcounter >= 4) postData.put("Dice_4", numbers[3])
            else postData.put("Dice_4", 0)
            postData.put("Roll_Type", "Roll Next")
            postData.put("Roll_Counter", rollcounter)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val JsonObjectRequest = JsonObjectRequest(Request.Method.POST, Posturl, postData,
            Response.Listener { response -> println(response) },
            Response.ErrorListener { println("************ FAILED ************") })
        queue.add(JsonObjectRequest)
        if(Singleton.initiated) Singleton.list.add(History_Item(numbers, "Roll Next Dice", this.rollcounter))
    }

    fun openHistory(btnHistory: View) {
        val intent = Intent(this, History::class.java)
        startActivity(intent)
    }
}