package com.apochromat.codeblockmobile

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ConsoleAdapter (private val listOutputMessage:ArrayList<String>) : RecyclerView.Adapter<ConsoleAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textMessage: TextView = itemView.findViewById(R.id.textMessage)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_console_bar, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textMessage.text = listOutputMessage[position]
    }
    override fun getItemCount(): Int {
        return listOutputMessage.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun addMessage(message: String) {
        listOutputMessage.add(message)
        notifyDataSetChanged()
    }
}