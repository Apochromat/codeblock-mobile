package com.apochromat.codeblockmobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RVAdaptor(private val items:ArrayList<String>) : RecyclerView.Adapter<RVAdaptor.ViewHolder>() {
    class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        val textView : TextView = itemView.findViewById(R.id.text)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rv, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = items[position]
    }

    override fun getItemCount(): Int {
        return items.size
    }
}