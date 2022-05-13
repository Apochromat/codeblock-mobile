package com.apochromat.codeblockmobile.logic

import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import com.apochromat.codeblockmobile.R

/**
 *  Блок одиночного консольного ввода.
 *  Позволяет вывести в консоль сообщение и считать введеное значение, присвоить его переменной.
 **/
class ConsoleInput : Block() {
    private var message: String = ""
    private var name: String = ""
    private var value: Int = 0

    init {
        setBlockType("ConsoleInput")
    }

    fun initVar(){
        message = inputLeftEdit
        name = inputRightEdit
    }

    fun setBlockInput(_name: String, _message: String = "") {
        name = _name
        message = if (_message == "") "" else "$_message "
    }

    override fun executeBlock(){
        initVar()
        val builder = AlertDialog.Builder(activity)
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_entry, null)
        val dialog = builder.create()

        dialog.setTitle(message)
        dialog.setMessage(name)
        dialog.setView(view)
        val buttonEdit = view.findViewById<Button>(R.id.buttonEditDialog)
        val editVar = view.findViewById<EditText>(R.id.editVarDialog)
        dialog.show()

        buttonEdit.setOnClickListener(){
            if (editVar.text.toString() != "") {
                valueVar = editVar.text.toString()
                runConsoleInput()
                dialog.dismiss()
            }
        }
    }

    private fun inputAssignment(){
        val calculated = arithmetics(accessHeap(), valueVar)
        setBlockStatus(calculated.first)
        if (calculated.first == "OK") {
            value = calculated.second
            accessHeap().setVariableValue(name, value)
        }
    }

    private fun runConsoleInput(){
        inputAssignment()
        when {
            getNextBlock() == null -> {
                println("Program finished with status: ${getBlockStatus()}")
                adapterConsole.addMessage("Program finished with status: ${getBlockStatus()}")
                adapterBlocks.notifyItemChanged(indexListBlocks)
            }
            getBlockStatus() == "OK" -> {
                callStack.push(getNextBlock())
            }
            else -> {
                println("Program finished with status: ${getBlockStatus()}")
                adapterConsole.addMessage("Program finished with status: ${getBlockStatus()}")
                adapterBlocks.notifyItemChanged(indexListBlocks)
            }
        }

    }

//    override fun executeBlock() {
//
//        print(message)
//        val inputValue: String = readln()
//        val calculated = arithmetics(accessHeap(), inputValue)
//        setBlockStatus(calculated.first)
//        if (calculated.first == "OK") {
//            value = calculated.second
//            accessHeap().setVariableValue(name, value)
//        }
//    }
}
