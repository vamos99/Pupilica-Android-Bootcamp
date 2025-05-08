package com.example.odev4

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonToA = findViewById<Button>(R.id.buttonToA)
        val buttonToX = findViewById<Button>(R.id.buttonToX)

        buttonToA.setOnClickListener {
            val intent = Intent(this, ActivityA::class.java)
            startActivity(intent)
        }

        buttonToX.setOnClickListener {
            val intent = Intent(this, ActivityX::class.java)
            startActivity(intent)
        }
    }
}