package com.example.bitirmeprojesi.ui.address

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bitirmeprojesi.R
import com.example.bitirmeprojesi.data.model.Adres

class AddressAdapter(
    private val adresList: List<Adres>,
    private val onDelete: (Adres) -> Unit
) : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_address, parent, false)
        return AddressViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.bind(adresList[position])
    }

    override fun getItemCount() = adresList.size

    inner class AddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(adres: Adres) {
            itemView.findViewById<TextView>(R.id.tvTitle).text = adres.baslik
            itemView.findViewById<TextView>(R.id.tvAddress).text = adres.adres
            itemView.findViewById<TextView>(R.id.tvPhone).text = adres.telefon
            itemView.findViewById<ImageButton>(R.id.btnDelete).setOnClickListener {
                onDelete(adres)
            }
        }
    }
} 