package com.staszewskilucas.todoapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.staszewskilucas.todoapp.databinding.ActivitySingleListViewBinding
import com.staszewskilucas.todoapp.databinding.DialogAddItemBinding
import com.staszewskilucas.todoapp.databinding.DialogAddListBinding

class SingleListViewActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySingleListViewBinding
    private lateinit var todoList: TodoList
    private lateinit var itemAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySingleListViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        todoList = intent.getParcelableExtraProvider("todoList") ?: return

        itemAdapter = ItemAdapter(todoList.items,
            {
                item, position ->
                itemAdapter.setItemIsDone(item, position)
            })
        binding.recyclerViewItemList.apply {
            layoutManager = LinearLayoutManager(this@SingleListViewActivity)
            adapter = itemAdapter
        }

        binding.listName.text = todoList.title

        binding.backButton.setOnClickListener {
            val intent = Intent().apply {
                putExtra("updatedTodoList", todoList)
            }
            setResult(RESULT_OK, intent)
            finish()
        }

        binding.addItemButton.setOnClickListener {
            addNewItem()
        }

        binding.deleteCompletedItemsButton.setOnClickListener {
            var doneItems = mutableListOf<Item>()
            for (item in todoList.items) {
                if(item.isDone) {
                    doneItems.add(item)
                }
            }
            showAllCompletedDeleteConfirmation(doneItems)
        }
    }

    fun addNewItem() {
        val inflater = layoutInflater
        val dialogBinding = DialogAddItemBinding.inflate(inflater)
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Neues Item hinzufügen")
        builder.setView(dialogBinding.root)
        builder.setPositiveButton("Hinzufügen", null)
        builder.setNegativeButton("Abbrechen", null)

        val dialog = builder.create()
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            positiveButton.setOnClickListener {
                val itemName = dialogBinding.itemNameInput.text.toString().trim()
                if(itemName.isNotEmpty()) {
                    val newItem = Item(itemName)
                    itemAdapter.addItem(newItem)
                    dialog.dismiss()
                }
                else {
                    dialogBinding.itemErrorMessage.visibility = View.VISIBLE
                    dialogBinding.itemNameInput.requestFocus()
                }
            }

            negativeButton.setOnClickListener {
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    fun showAllCompletedDeleteConfirmation(doneItems: MutableList<Item>) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Erledigte Items löschen")
        builder.setMessage("Möchten Sie alle erledigten Items dieser Liste wirklich löschen?")
        builder.setPositiveButton("Löschen") { _, _ ->
            itemAdapter.removeCompletedItems(doneItems)
        }
        builder.setNegativeButton("Abbrechen", null)
        builder.show()
    }
}