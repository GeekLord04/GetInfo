package com.geekster.getinfo.fragmentList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.geekster.getinfo.R
import com.geekster.getinfo.databinding.ListItemBinding
import com.geekster.getinfo.models.ListResponseItem

class ItemAdapter(private var ItemList: List<ListResponseItem>) : ListAdapter<ListResponseItem, ItemAdapter.ListViewHolder>(
    ComparatorDiffUtil()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }

    inner class ListViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ListResponseItem) {
            binding.prodName.text = item.product_name
            binding.prodType.text = item.product_type
            binding.prodTax.text = "Tax = ${item.tax.toString()}"
            binding.prodPrice.text = item.price.toString()
            Glide.with(itemView.context)
                .load(item.image)
                .placeholder(R.drawable.product_image)
                .error(R.drawable.product_image)
                .into(binding.image)
        }

    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<ListResponseItem>() {
        override fun areItemsTheSame(oldItem: ListResponseItem, newItem: ListResponseItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ListResponseItem, newItem: ListResponseItem): Boolean {
            return oldItem == newItem
        }
    }
}