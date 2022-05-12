//package com.apochromat.codeblockmobile
//
//import android.annotation.SuppressLint
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.view.inputmethod.EditorInfo
//import android.widget.EditText
//import android.widget.Spinner
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.apochromat.codeblockmobile.logic.Block
//import com.apochromat.codeblockmobile.logic.EntryPoint
//import com.apochromat.codeblockmobile.logic.connectBlocks
//import com.apochromat.codeblockmobile.logic.disconnectBlocks
//
//class BlocksAdapter(private val listBlocks:ArrayList<Block>) : RecyclerView.Adapter<BlocksAdapter.ViewHolder>() {
//    class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
//        val textType: TextView = itemView.findViewById(R.id.textType)
//        val textStatus: TextView = itemView.findViewById(R.id.textStatus)
//        val spinnerComparator: Spinner = itemView.findViewById(R.id.spinnerComparator)
//        val editLeft: EditText = itemView.findViewById(R.id.editLeft)
//        val editRight: EditText = itemView.findViewById(R.id.editRight)
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        val viewType = when(listBlocks[position].getBlockType()){
//            "EntryPoint" -> R.layout.item_entry_point
//            "UndefinedVariable" -> R.layout.item_undefined_var
//            "Assignment" -> R.layout.item_assignment
//            "ConditionIf" -> R.layout.item_condition_if
//            "ConditionIfElse" -> R.layout.item_condition_if_else
//            "CycleWhile" -> R.layout.item_cycle_while
//            "ConsoleOutput" -> R.layout.item_console_output
//            "ConsoleInputOne" -> R.layout.item_console_input_one
//            "Begin" -> R.layout.item_begin
//            "End" -> R.layout.item_end
//            "Else" -> R.layout.item_else
//            else -> R.layout.item_defined_var
//        }
//        return viewType;
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(viewType, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        listBlocks[position].holder = holder
//        if (listBlocks[position].getBlockType() == "EntryPoint" || listBlocks[position].getBlockType() == "Begin" ||
//            listBlocks[position].getBlockType() == "End"){
//            return
//        }
//        holder.editLeft.setOnEditorActionListener { _, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
//                listBlocks[position].inputLeftEdit = holder.editLeft.text.toString()
//            }
//            false
//        }
//        holder.editRight.setOnEditorActionListener { _, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
//                listBlocks[position].inputRightEdit = holder.editRight.text.toString()
//                holder.editRight.clearFocus()            }
//            false
//        }
//
//
//        holder.textType.text = listBlocks[position].getBlockType()
//        holder.textStatus.text = listBlocks[position].getBlockStatus()
//
//        holder.editLeft.setText(listBlocks[position].inputLeftEdit)
//        holder.spinnerComparator.setSelection(0)
//        holder.editRight.setText(listBlocks[position].inputRightEdit)
//    }
//
//    override fun getItemCount(): Int {
//        return listBlocks.size
//    }
//
//    fun addBlock(block : Block){
//        listBlocks.add(block)
//        notifyItemInserted(listBlocks.size-1)
//    }
//
//    fun saveAllData(){
//        for (i in 0 until listBlocks.size){
//            listBlocks[i].inputLeftEdit =  listBlocks[i].holder.editLeft.text.toString()
//            listBlocks[i].inputRightEdit =  listBlocks[i].holder.editRight.text.toString()
//            listBlocks[i].inputComparator =  listBlocks[i].holder.spinnerComparator.selectedItem.toString()
//        }
//    }
//}