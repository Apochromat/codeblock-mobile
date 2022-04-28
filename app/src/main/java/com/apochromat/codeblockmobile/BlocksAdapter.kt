package com.apochromat.codeblockmobile

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.recyclerview.widget.RecyclerView
import com.apochromat.codeblockmobile.logic.Block


class BlocksAdapter(private val listBlocks:ArrayList<Block>) : RecyclerView.Adapter<BlocksAdapter.ViewHolder>() {
    class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        val textType: TextView = itemView.findViewById(R.id.text1)
        val textStatus: TextView = itemView.findViewById(R.id.text3)
//        val edit: EditText = itemView.findViewById(R.id.edit1)
    }

    override fun getItemViewType(position: Int): Int {
        val viewType = when(listBlocks[position].getBlockType()){
            "EntryPoint" -> R.layout.item_entry_point
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
        if (position == 0){
            return
        }
//        if (listBlocks[position].getBlockType() == "DefinedVariable"){
//
//            holder.edit.setOnEditorActionListener { _, actionId, _ ->
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    listBlocks[position].inputName = holder.edit.text.toString()
//                }
//                false
//            }
//        }
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