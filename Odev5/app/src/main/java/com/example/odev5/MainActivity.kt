package com.example.odev5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var resultTextView: TextView
    private var firstNumber = 0.0
    private var operation = ""
    private var isNewNumber = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultTextView = findViewById(R.id.resultTextView)

        // Sayı butonları
        val numberButtons = arrayOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )

        // İşlem butonları
        val operationButtons = arrayOf(
            R.id.btnPlus, R.id.btnMinus, R.id.btnMultiply, R.id.btnDivide
        )

        // Sayı butonlarına tıklama olayları
        numberButtons.forEach { buttonId ->
            findViewById<Button>(buttonId).setOnClickListener {
                onNumberClick((it as Button).text.toString())
            }
        }

        // İşlem butonlarına tıklama olayları
        operationButtons.forEach { buttonId ->
            findViewById<Button>(buttonId).setOnClickListener {
                onOperationClick((it as Button).text.toString())
            }
        }

        // Diğer butonlar
        findViewById<Button>(R.id.btnEquals).setOnClickListener { onEqualsClick() }
        findViewById<Button>(R.id.btnClear).setOnClickListener { onClearClick() }
    }

    private fun onNumberClick(number: String) {
        try {
            if (isNewNumber) {
                resultTextView.text = number
                isNewNumber = false
            } else {
                resultTextView.append(number)
            }
        } catch (e: Exception) {
            showError("Sayı girişi hatası")
        }
    }

    private fun onOperationClick(op: String) {
        try {
            val currentText = resultTextView.text.toString()
            if (currentText.isNotEmpty()) {
                firstNumber = currentText.toDouble()
                operation = op
                isNewNumber = true
            }
        } catch (e: Exception) {
            showError("İşlem hatası")
        }
    }

    private fun onEqualsClick() {
        try {
            if (operation.isEmpty()) return

            val secondNumber = resultTextView.text.toString().toDouble()
            val result = when (operation) {
                "+" -> firstNumber + secondNumber
                "-" -> firstNumber - secondNumber
                "×" -> firstNumber * secondNumber
                "÷" -> {
                    if (secondNumber == 0.0) {
                        showError("Sıfıra bölme hatası")
                        return
                    }
                    firstNumber / secondNumber
                }
                else -> return
            }

            resultTextView.text = if (result == result.toLong().toDouble()) {
                result.toLong().toString()
            } else {
                result.toString()
            }
            
            operation = ""
            isNewNumber = true
        } catch (e: Exception) {
            showError("Hesaplama hatası")
        }
    }

    private fun onClearClick() {
        resultTextView.text = "0"
        firstNumber = 0.0
        operation = ""
        isNewNumber = true
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        onClearClick()
    }
}