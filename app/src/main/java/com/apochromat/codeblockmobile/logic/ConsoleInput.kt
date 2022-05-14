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
    private var nextB: Block? = null

    init {
        setBlockType("ConsoleInput")
    }

    fun initVar(){
        message = inputLeftEdit
        name = inputRightEdit
        nextB = getNextBlock()
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

        buttonEdit.setOnClickListener(){
            if (editVar.text.toString() != "") {
                valueVar = editVar.text.toString()
                dialog.dismiss()
                runConsoleInput()
            }
        }
        isProgramRunning = false
        dialog.show()
    }

    private fun inputAssignment(){
        val stringList = stringToList(valueVar)
        val obj = defineInput(heap, name)
        if (stringList.size > 1) {
            if (obj.first == "Variable") {
                setBlockStatus("Impossible to assign sequence to variable")
                return
            }
            if (obj.first != "Array") {
                setBlockStatus(obj.first)
                return
            }
            if (stringList.size != heap.getArraySize(obj.second)) {
                setBlockStatus("Sizes mismatch")
                return
            }
            for (i in stringList.indices) {
                val calculated = arithmetics(heap, stringList[i])
                if (calculated.first != "OK") {
                    setBlockStatus(calculated.first)
                    return
                }
                heap.setArrayValue(obj.second, i, calculated.second)
            }
            return
        }
        val calculated = arithmetics(accessHeap(), valueVar)
        setBlockStatus(calculated.first)
        if (calculated.first != "OK") return
        if (obj.first !in listOf("Array", "Variable")) {
            setBlockStatus(obj.first)
            return
        }
        value = calculated.second
        when (obj.first) {
            "Array" -> {
                heap.setArrayValue(name, obj.third, value)
            }
            "Variable" -> {
                heap.setVariableValue(name, value)
            }
        }
    }

    private fun runConsoleInput(){
        inputAssignment()
        when {
            nextB == null -> {
                isProgramRunning = false
                println("Program finished with status: ${getBlockStatus()}")
                adapterConsole.addMessage("Program finished with status: ${getBlockStatus()}")
                adapterBlocks.notifyItemChanged(indexListBlocks)
            }
            getBlockStatus() == "OK" -> {
                callStack.push(nextB)
            }
            else -> {
                isProgramRunning = false
                println("Program finished with status: ${getBlockStatus()}")
                adapterConsole.addMessage("Program finished with status: ${getBlockStatus()}")
                adapterBlocks.notifyItemChanged(indexListBlocks)
            }
        }
        isProgramRunning = false
    }
}
