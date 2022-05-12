//package com.apochromat.codeblockmobile
//
//import android.R.attr.data
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//
//
//class ConsoleAdapter (private val listOutputMessage:ArrayList<String>) : RecyclerView.Adapter<ConsoleAdapter.ViewHolder>() {
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val textMessage: TextView = itemView.findViewById(R.id.textMessage)
//    }
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_console_bar, parent, false)
//        return ViewHolder(view)
//    }
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.textMessage.text = listOutputMessage[position]
//    }
//    override fun getItemCount(): Int {
//        return listOutputMessage.size
//    }
//    fun addMessage(message: String) {
//        listOutputMessage.add(message)
//        notifyItemInserted(listOutputMessage.size-1)
//    }
//    fun clearListMessages() {
//        val size = listOutputMessage.size
//        if (size > 0) {
//            for (i in 0 until size) {
//                listOutputMessage.removeAt(0)
//            }
//            notifyItemRangeRemoved(0, size)
//        }
//    }
//}