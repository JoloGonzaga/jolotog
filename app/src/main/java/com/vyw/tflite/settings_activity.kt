package com.vyw.tflite

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.vyw.tflite.R

class settings_activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

    }




        fun exit2_btnclick(view: View) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }



    }

