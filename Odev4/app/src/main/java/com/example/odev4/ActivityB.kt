package com.example.odev4

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ActivityB : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b)

        val buttonToY = findViewById<Button>(R.id.buttonToY)

        buttonToY.setOnClickListener {
            val intent = Intent(this, ActivityY::class.java)
            startActivity(intent)
        }
    }
} 