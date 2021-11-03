package com.example.cam_map

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // navigates to the camera and gallery activity
    fun Cam_Gallery(View: View) {
        val intent = Intent(this, Camera_Gallery::class.java)
        startActivity(intent)
    }

    // navigates to the GPS and map activity
    fun GPS_Map(View: View) {
        val intent = Intent(this, GPS_Map::class.java)
        startActivity(intent)
    }
}