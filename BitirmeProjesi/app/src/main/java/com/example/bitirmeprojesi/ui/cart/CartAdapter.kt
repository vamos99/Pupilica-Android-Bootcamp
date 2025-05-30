package com.example.bitirmeprojesi.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bitirmeprojesi.R
import com.example.bitirmeprojesi.data.model.SepetYemek
import com.example.bitirmeprojesi.databinding.ItemCartBinding

class CartAdapter(
    private val onQuantityChanged: (SepetYemek, Int) -> Unit,
    private val onDeleteClicked: (SepetYemek) -> Unit
) : ListAdapter<SepetYemek, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CartViewHolder(private val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(sepetYemek: SepetYemek) {
            binding.apply {
                // Set food details
                tvFoodName.text = sepetYemek.yemek_adi
                tvQuantity.text = sepetYemek.yemek_siparis_adet
                
                // Calculate and display total price for this item
                val totalPrice = sepetYemek.yemek_fiyat.toInt() * sepetYemek.yemek_siparis_adet.toInt()
                tvFoodPrice.text = "${totalPrice}₺"
                
                // Load food image
                val imageUrl = "http://kasimadalan.pe.hu/yemekler/resimler/${sepetYemek.yemek_resim_adi}"
                Glide.with(itemView.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_food_placeholder)
                    .error(R.drawable.ic_food_placeholder)
                    .into(ivFoodImage)
                
                // Quantity controls
                btnDecrease.setOnClickListener {
                    val currentQuantity = sepetYemek.yemek_siparis_adet.toInt()
                    if (currentQuantity > 1) {
                        onQuantityChanged(sepetYemek, currentQuantity - 1)
                    }
                }
                
                btnIncrease.setOnClickListener {
                    val currentQuantity = sepetYemek.yemek_siparis_adet.toInt()
                    onQuantityChanged(sepetYemek, currentQuantity + 1)
                }
                
                // Delete button
                btnDelete.setOnClickListener {
                    onDeleteClicked(sepetYemek)
                }
                
                // Quantity controls
                btnDecrease.setOnClickListener {
                    val currentQuantity = sepetYemek.yemek_siparis_adet.toInt()
                    if (currentQuantity > 1) {
                        onQuantityChanged(sepetYemek, currentQuantity - 1)
                    }
                }
                
                btnIncrease.setOnClickListener {
                    val currentQuantity = sepetYemek.yemek_siparis_adet.toInt()
                    onQuantityChanged(sepetYemek, currentQuantity + 1)
                }
                
                // Delete button
                btnDelete.setOnClickListener {
                    onDeleteClicked(sepetYemek)
                }
            }
        }
    }

    class CartDiffCallback : DiffUtil.ItemCallback<SepetYemek>() {
        override fun areItemsTheSame(oldItem: SepetYemek, newItem: SepetYemek): Boolean {
            return oldItem.sepet_yemek_id == newItem.sepet_yemek_id
        }

        override fun areContentsTheSame(oldItem: SepetYemek, newItem: SepetYemek): Boolean {
            return oldItem == newItem
        }
    }
}
