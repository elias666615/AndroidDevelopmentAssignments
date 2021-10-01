package com.example.diceroller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import java.io.Serializable
import kotlin.random.Random

data class History_Item(var numList: List<Int>, var rollType: String, var rollCounter: Int) : Serializable

object Singleton{
    init {
    }
    var list = ArrayList<History_Item>()
    fun deleteAll(){
        list.clear()
    }
}

class MainActivity : AppCompatActivity() {
    private var rollcounter = 1
    private var diceRolled = 0
    private var urlList = listOf<Int>(R.drawable.dice0, R.drawable.dice1, R.drawable.dice2, R.drawable.dice3, R.drawable.dice4, R.drawable.dice5, R.drawable.dice6)
    private var Histroy_List = ArrayList<History_Item>()
    private lateinit var ImageList: List<ImageView>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tvCounter = findViewById<TextView>(R.id.tvRollCounter)
        tvCounter.setText(this.rollcounter.toString())
        ImageList = listOf<ImageView>(findViewById(R.id.imgDice1), findViewById(R.id.imgDice2), findViewById(R.id.imgDice3), findViewById(R.id.imgDice4))
    }

    fun onIncrementCounter(btnIncrement: View) {
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
        var numbers = mutableListOf<Int>()
        var counter = 0
        //var ImageList = listOf<ImageView>(findViewById(R.id.imgDice1), findViewById(R.id.imgDice2), findViewById(R.id.imgDice3), findViewById(R.id.imgDice4))
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
        this.Histroy_List.add(History_Item(numbers, "Roll All Dice", this.rollcounter))
        Singleton.list.add(History_Item(numbers, "Roll All Dice", this.rollcounter))
        this.diceRolled = 0
    }

    fun rollNextDice(btnRollNextDice: View) {
        var numbers = mutableListOf<Int>()
        //var ImageList = listOf<ImageView>(findViewById(R.id.imgDice1), findViewById(R.id.imgDice2), findViewById(R.id.imgDice3), findViewById(R.id.imgDice4))
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
        this.Histroy_List.add(History_Item(numbers, "Roll Next Dice", this.rollcounter))
        Singleton.list.add(History_Item(numbers, "Roll Next Dice", this.rollcounter))
        this.diceRolled++
    }

    fun openHistory(btnHistory: View) {
        val intent = Intent(this, History::class.java)
        //intent.putExtra("history", this.Histroy_List)
        startActivity(intent)
    }
}