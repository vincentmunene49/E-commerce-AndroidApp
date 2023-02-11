package com.example.ecommerceapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.databinding.SizeRvItemBinding

class SizesAdapter : RecyclerView.Adapter<SizesAdapter.MyViewHolder>() {
    private var selectedPosition =-1
    inner class MyViewHolder(private val binding: SizeRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(size: String,position: Int) {
            binding.tvSize.text = size
            if(position == selectedPosition){
                binding.apply {
                    imgShadow.visibility = View.VISIBLE
                }
            }else{
                binding.imgShadow.visibility = View.INVISIBLE
            }

        }
    }

    val diffCallBack = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            SizeRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val current_size = differ.currentList[position]
        holder.bind(current_size,position)

        holder.itemView.setOnClickListener {
            if(selectedPosition >=0)
                notifyItemChanged(selectedPosition)
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
            onItemClicked?.invoke(current_size)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onItemClicked : ((String)->Unit)?=null

}