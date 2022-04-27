package com.apochromat.codeblockmobile

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
    
    fun getViewTypeCount(): Int {
        return 8
    }

    override fun getItemViewType(position: Int): Int {
        val viewType = when(listBlocks[position].getBlockType()){
            "UndefinedVariable" -> 1
            "Assignment" -> 2
            "ConditionIf" -> 3
            "ConditionIfElse" -> 4
            "CycleWhile" -> 5
            "ConsoleOutput" -> 6
            "ConsoleInputOne" -> 7
            else -> 0
        }
        return viewType;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            0 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.defined_var_rv, parent, false)
                ViewHolder(view)
            }
            1 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.undefined_var_rv, parent, false)
                ViewHolder(view)
            }
            2 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.assignment_rv, parent, false)
                ViewHolder(view)
            }
            3 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.condition_if_rv, parent, false)
                ViewHolder(view)
            }
            4 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.condition_if_else_rv, parent, false)
                ViewHolder(view)
            }
            5 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.cycle_while_rv, parent, false)
                ViewHolder(view)
            }
            6 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.console_output_rv, parent, false)
                ViewHolder(view)
            }
            7 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.console_input_one_rv, parent, false)
                ViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.defined_var_rv, parent, false)
                ViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textType.text = listBlocks[position].getBlockType()
        holder.textStatus.text = listBlocks[position].getBlockStatus()
    }

    override fun getItemCount(): Int {
        return listBlocks.size
    }
    fun addBlock(block : Block){
        listBlocks.add(block)
        notifyDataSetChanged()
    }
}