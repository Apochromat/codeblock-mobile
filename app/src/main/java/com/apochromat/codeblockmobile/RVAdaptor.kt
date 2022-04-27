package com.apochromat.codeblockmobile

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RVAdaptor(private val listBlocks:ArrayList<Block>) : RecyclerView.Adapter<RVAdaptor.ViewHolder>() {
    class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        val textType: TextView = itemView.findViewById(R.id.text1)
        val textStatus: TextView = itemView.findViewById(R.id.text3)
    }

    override fun getItemViewType(position: Int): Int {
        val viewType = when(listBlocks[position].getBlockType()){
            "UndefinedVariable" -> R.layout.item_undefined_var
            "Assignment" -> R.layout.item_assignment
            "ConditionIf" -> R.layout.item_condition_if
            "ConditionIfElse" -> R.layout.item_condition_if_else
            "CycleWhile" -> R.layout.item_cycle_while
            "ConsoleOutput" -> R.layout.item_console_output
            "ConsoleInputOne" -> R.layout.item_console_input_one
            else -> R.layout.item_defined_var
        }
        return viewType;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textType.text = listBlocks[position].getBlockType()
        holder.textStatus.text = listBlocks[position].getBlockStatus()
    }

    override fun getItemCount(): Int {
        return listBlocks.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun addBlock(block : Block){
        listBlocks.add(block)
        notifyDataSetChanged()
    }
}