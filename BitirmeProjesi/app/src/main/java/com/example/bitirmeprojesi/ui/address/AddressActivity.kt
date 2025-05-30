package com.example.bitirmeprojesi.ui.address

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bitirmeprojesi.data.model.Adres
import com.example.bitirmeprojesi.databinding.ActivityAddressBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.UUID

class AddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddressBinding
    private val adresList = mutableListOf<Adres>()
    private lateinit var adapter: AddressAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = AddressAdapter(adresList, onDelete = { adres ->
            deleteAddress(adres)
        })
        binding.recyclerViewAddresses.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewAddresses.adapter = adapter

        binding.fabAddAddress.setOnClickListener { showAddAddressDialog() }
        loadAddresses()

        // Toolbar'a geri butonu ekle
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun loadAddresses() {
        val prefs = getSharedPreferences("GetFoodPrefs", Context.MODE_PRIVATE)
        val json = prefs.getString("addresses", null)
        adresList.clear()
        if (!json.isNullOrBlank()) {
            val type = object : TypeToken<List<Adres>>() {}.type
            adresList.addAll(Gson().fromJson(json, type))
        }
        adapter.notifyDataSetChanged()
        binding.emptyAddressLayout.visibility = if (adresList.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun saveAddresses() {
        val prefs = getSharedPreferences("GetFoodPrefs", Context.MODE_PRIVATE)
        prefs.edit().putString("addresses", Gson().toJson(adresList)).apply()
    }

    private fun showAddAddressDialog() {
        val dialogView = LayoutInflater.from(this).inflate(com.example.bitirmeprojesi.R.layout.dialog_add_address, null)
        val etTitle = dialogView.findViewById<android.widget.EditText>(com.example.bitirmeprojesi.R.id.etTitle)
        val etAddress = dialogView.findViewById<android.widget.EditText>(com.example.bitirmeprojesi.R.id.etAddress)
        val etPhone = dialogView.findViewById<android.widget.EditText>(com.example.bitirmeprojesi.R.id.etPhone)
        AlertDialog.Builder(this)
            .setTitle("Adres Ekle")
            .setView(dialogView)
            .setPositiveButton("Ekle") { _, _ ->
                val title = etTitle.text.toString().trim()
                val address = etAddress.text.toString().trim()
                val phone = etPhone.text.toString().trim()
                if (title.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(this, "Tüm alanları doldurun", Toast.LENGTH_SHORT).show()
                } else {
                    val adres = Adres(UUID.randomUUID().toString(), title, address, phone)
                    adresList.add(adres)
                    saveAddresses()
                    loadAddresses()
                }
            }
            .setNegativeButton("İptal", null)
            .show()
    }

    private fun deleteAddress(adres: Adres) {
        adresList.remove(adres)
        saveAddresses()
        loadAddresses()
    }
} 