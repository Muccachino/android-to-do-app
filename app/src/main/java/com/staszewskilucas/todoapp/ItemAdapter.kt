package com.staszewskilucas.todoapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.staszewskilucas.todoapp.databinding.ListItemBinding

class ItemAdapter(
    private val items: MutableList<Item>,
    private val onItemClicked: (Item, Int) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.itemName.text = item.name
            if(item.isDone) {
                binding.itemName.paintFlags = binding.itemName.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                binding.itemName.setTextColor(binding.root.context.getColor(R.color.gray))
            } else {
                binding.itemName.paintFlags = binding.itemName.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
                binding.itemName.setTextColor(binding.root.context.getColor(android.R.color.black))
            }

            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if(position != RecyclerView.NO_POSITION) {
                    onItemClicked(item, position)
                }
            }

            binding.deleteIcon.setOnClickListener {
                val position = bindingAdapterPosition
                if(position != RecyclerView.NO_POSITION) {
                    removeSingleItem(position)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun addItem(item: Item) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun setItemIsDone(item: Item, position: Int) {
        item.isDone = !item.isDone
        notifyItemChanged(position)
    }

    fun removeSingleItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeCompletedItems(doneItems: MutableList<Item>) {
        val newList = items.filter { item -> !doneItems.contains(item) }
        items.clear()
        if(newList.isNotEmpty()) {
            for (item in newList) {
                items.add(item)

            }
        }
        notifyDataSetChanged()
    }
}