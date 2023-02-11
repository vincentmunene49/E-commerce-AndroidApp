package com.example.ecommerceapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerceapp.databinding.ViewPagerImageItemBinding

class ImagesViewPagerAdapter : RecyclerView.Adapter<ImagesViewPagerAdapter.MyViewHolder>() {
    inner class MyViewHolder(private val binding: ViewPagerImageItemBinding) :
        RecyclerView.ViewHolder(binding.root){
          fun bind(imagePath:String){
                Glide.with(itemView).load(imagePath).into(binding.productImage)
            }
        }

    private val diffCallBack = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return newItem == oldItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return newItem == oldItem
        }

    }


    val differ = AsyncListDiffer(this, diffCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ViewPagerImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val current_image = differ.currentList[position]
        holder.bind(current_image)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}