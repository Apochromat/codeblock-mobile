//package com.apochromat.codeblockmobile
//
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.view.Menu
//import android.view.MenuItem
//import android.widget.Toast
//import androidx.core.view.GravityCompat
//import androidx.recyclerview.widget.ItemTouchHelper
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.apochromat.codeblockmobile.databinding.ActivityProjectBinding
//import com.apochromat.codeblockmobile.logic.*
//import java.util.*
//import kotlin.collections.ArrayList
//
//class ProjectActivity : AppCompatActivity() {
//    private lateinit var listBlocks : ArrayList<Block>
//    private lateinit var listMessage : ArrayList<String>
//    private lateinit var blocksAdapter : BlocksAdapter
//    lateinit var consoleAdapter : ConsoleAdapter
//    lateinit var bindingClass : ActivityProjectBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        bindingClass = ActivityProjectBinding.inflate(layoutInflater)
//        setContentView(bindingClass.root)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.title = "newProject"
//
//        bindingClass.blocksRV.isDrawingCacheEnabled = true
//        bindingClass.blocksRV.setItemViewCacheSize(100);
//
//        createConsoleView()
//        createBlocksView()
//
//        init()
//    }
//    private fun init(){
//        bindingClass.apply{
//            buttonDefinedVar.setOnClickListener{
//                blocksAdapter.addBlock(DefinedVariable())
//                listBlocks[listBlocks.size-1].adapter = consoleAdapter
//            }
//            buttonUndefinedVar.setOnClickListener{
//                blocksAdapter.addBlock(UndefinedVariable())
//                listBlocks[listBlocks.size-1].adapter = consoleAdapter
//            }
//            buttonAssignment.setOnClickListener{
//                blocksAdapter.addBlock(Assignment())
//                listBlocks[listBlocks.size-1].adapter = consoleAdapter
//            }
//            buttonConditionIf.setOnClickListener{
//                blocksAdapter.addBlock(ConditionIf())
//                blocksAdapter.addBlock(Begin())
//                blocksAdapter.addBlock(End())
//                listBlocks[listBlocks.size-3].adapter = consoleAdapter
//                listBlocks[listBlocks.size-3].begin = Begin()
//                listBlocks[listBlocks.size-3].end = End()
//                listBlocks[listBlocks.size-2].adapter = consoleAdapter
//                listBlocks[listBlocks.size-1].adapter = consoleAdapter
//            }
//            buttonConditionIfElse.setOnClickListener{
//                blocksAdapter.addBlock(ConditionIfElse())
//                blocksAdapter.addBlock(Begin())
//                blocksAdapter.addBlock(End())
//                blocksAdapter.addBlock(Else())
//                blocksAdapter.addBlock(Begin())
//                blocksAdapter.addBlock(End())
//                listBlocks[listBlocks.size-6].begin = Begin()
//                listBlocks[listBlocks.size-6].end = End()
//                listBlocks[listBlocks.size-6].beginElse = Begin()
//                listBlocks[listBlocks.size-6].endElse = End()
//                for (i in 1..6){
//                    listBlocks[listBlocks.size-i].adapter = consoleAdapter
//                }
//
//            }
//            buttonCycleWhile.setOnClickListener{
//                blocksAdapter.addBlock(CycleWhile())
//                blocksAdapter.addBlock(Begin())
//                blocksAdapter.addBlock(End())
//                listBlocks[listBlocks.size-3].adapter = consoleAdapter
//                listBlocks[listBlocks.size-3].begin = Begin()
//                listBlocks[listBlocks.size-3].end = End()
//                listBlocks[listBlocks.size-2].adapter = consoleAdapter
//                listBlocks[listBlocks.size-1].adapter = consoleAdapter
//            }
//            buttonConsoleOutput.setOnClickListener{
//                blocksAdapter.addBlock(ConsoleOutput())
//                listBlocks[listBlocks.size-1].adapter = consoleAdapter
//            }
//            buttonConsoleInputOne.setOnClickListener{
//                blocksAdapter.addBlock(ConsoleInputOne())
//                listBlocks[listBlocks.size-1].adapter = consoleAdapter
//            }
//        }
//    }
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.project_menu, menu)
//        return true
//    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId){
//            android.R.id.home -> {
//                val intent = Intent(this@ProjectActivity, MainActivity::class.java)
//                startActivity(intent)
//            }
//            R.id.menuRun -> {
//                Toast.makeText(this, "Запуск...", Toast.LENGTH_SHORT).show()
//                runProject(listBlocks)
//            }
//            R.id.menuSave -> {
//                blocksAdapter.saveAllData()
//                Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show()
//            }
//            R.id.menuConsole -> {
//                bindingClass.drawer.openDrawer(GravityCompat.START)
//            }
//        }
//        return true
//    }
//
//    private val simpleCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN or
//            ItemTouchHelper.START or ItemTouchHelper.END, ItemTouchHelper.LEFT){
//        override fun onMove(
//            recyclerView: RecyclerView,
//            viewHolder: RecyclerView.ViewHolder,
//            target: RecyclerView.ViewHolder
//        ): Boolean {
//            if (viewHolder.adapterPosition == 0 || target.adapterPosition == 0){
//                return true
//            }
//            val fromPosition = viewHolder.adapterPosition
//            val toPosition = target.adapterPosition
//            Collections.swap(listBlocks, fromPosition, toPosition)
//
//            blocksAdapter.notifyItemMoved(fromPosition, toPosition)
//
//            return false
//        }
//
//        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//            val position =  viewHolder.adapterPosition
//            if (viewHolder.adapterPosition == 0){
//                return
//            }
//            listBlocks.remove(listBlocks[position])
//            blocksAdapter.notifyItemRemoved(position)
//        }
//    }
//    private fun createBlocksView(){
//        val blocksView : RecyclerView = findViewById(R.id.blocksRV)
//        blocksView.layoutManager = LinearLayoutManager(this)
//        listBlocks = setListBlocks()
//        blocksAdapter = BlocksAdapter(listBlocks)
//        blocksView.adapter = blocksAdapter
//        val itemTouchHelper = ItemTouchHelper(simpleCallback)
//        itemTouchHelper.attachToRecyclerView(blocksView)
//    }
//    private fun createConsoleView(){
//        val consoleView : RecyclerView = bindingClass.messageRV
//        consoleView.layoutManager = LinearLayoutManager(this)
//        listMessage = setListMessage()
//        consoleAdapter = ConsoleAdapter(listMessage)
//        consoleView.adapter = consoleAdapter
//    }
//    private fun setListBlocks() : ArrayList<Block>{
//        val list = ArrayList<Block>()
//        list.add(EntryPoint())
//        list[0].adapter = consoleAdapter
//        return list
//    }
//    private fun setListMessage(): ArrayList<String> {
//        return ArrayList()
//    }
//
//    private fun check(index : Int) : Pair<Int, Int>{
//        var i = index
//        while (listBlocks[i+1].getBlockType() != "End"){
//            if (i+1 == listBlocks.size) {
//                consoleAdapter.addMessage("В строке $i ожидался End")
//                return Pair(0, 0)
//            }
//            if (listBlocks[i].getBlockType() == "ConditionIf" || listBlocks[i].getBlockType() == "ConditionIfElse" || listBlocks[i].getBlockType() == "CycleWhile"){
//                val temp : Int
//                if (listBlocks[i].getBlockType() == "ConditionIfElse")
//                    temp = checkIfElse(i)
//                else
//                    temp = checkIf(i)
//
//                if(temp == 0)
//                    return Pair(0, 0)
//                else {
//                    if (temp < listBlocks.size && listBlocks[temp].getBlockType() != "End"){
//                        connectBlocks(listBlocks[i], listBlocks[temp])
//                        i = temp
//                    }
//                    else if(temp < listBlocks.size && listBlocks[temp].getBlockType() == "End"){
//                        return Pair(i, temp)
//                    }
//                    else if(temp >= listBlocks.size){
//                        consoleAdapter.addMessage("В строке $temp ожидался End")
//                        return Pair(0, 0)
//                    }
//                    else
//                        break
//                }
//            }
//            else {
//                connectBlocks(listBlocks[i], listBlocks[i+1])
//                i += 1
//            }
//        }
//        return Pair(i, 0)
//    }
//
//    private fun checkIfElse(index : Int): Int{
//        var i = index + 1
//        if (listBlocks[i].getBlockType() == "Begin"){
//            i += 1
//            if (listBlocks[i].getBlockType() != "End"){
//                connectBlocks(listBlocks[index].begin, listBlocks[i])
//                val (j, temp) = check(i)
//                if (j == 0)
//                    return 0
//                else if (listBlocks[temp].getBlockType() == "End") {
//                    connectBlocks(listBlocks[j], listBlocks[index].end)
//                    i = temp + 1
//                }
//                else{
//                    connectBlocks(listBlocks[j], listBlocks[index].end)
//                    i = j + 2
//                }
//            }
//            else{
//                connectBlocks(listBlocks[index].begin, listBlocks[index].end)
//                i += 1
//            }
//            if (listBlocks[i].getBlockType() == "Else"){
//                i += 1
//                if (listBlocks[i].getBlockType() == "Begin"){
//                    i += 1
//                    if (listBlocks[i].getBlockType() != "End"){
//                        connectBlocks(listBlocks[index].beginElse, listBlocks[i])
//                        val (i, temp) = check(i)
//                        if (i == 0)
//                            return 0
//                        else if (listBlocks[temp].getBlockType() == "End") {
//                            connectBlocks(listBlocks[i], listBlocks[index].end)
//                            return temp + 1
//                        }
//                        connectBlocks(listBlocks[i], listBlocks[index].endElse)
//                        return i + 2
//                    }
//                    else
//                        connectBlocks(listBlocks[index].beginElse, listBlocks[index].endElse)
//                        return i + 1
//                }
//                else {
//                    consoleAdapter.addMessage("В строке $i ожидался Begin")
//                    return 0
//                }
//            }
//            else {
//                consoleAdapter.addMessage("В строке $i ожидался Else")
//                return 0
//            }
//        }
//        else {
//            consoleAdapter.addMessage("В строке $i ожидался Begin")
//            return 0
//        }
//    }
//
//    private fun checkIf(index : Int): Int{
//        var i = index + 1
//        if (listBlocks[i].getBlockType() == "Begin"){
//            i += 1
//            if (listBlocks[i].getBlockType() == "End"){
//                connectBlocks(listBlocks[index].begin, listBlocks[index].end)
//                return i + 1
//            }
//            connectBlocks(listBlocks[index].begin, listBlocks[i])
//            val (i, temp) = check(i)
//            if (i == 0)
//                return 0
//            else if (listBlocks[temp].getBlockType() == "End") {
//                connectBlocks(listBlocks[i], listBlocks[index].end)
//                return temp + 1
//            }
//            connectBlocks(listBlocks[i], listBlocks[index].end)
//            return i + 2
//        }
//        else {
//            consoleAdapter.addMessage("В строке $i ожидался Begin")
//            return 0
//        }
//    }
//
//    private fun connectionBlocks() : Boolean{
//        var i = 0
//        while (i < listBlocks.size-1) {
//            if(listBlocks[i].getBlockType() == "Begin"){
//                consoleAdapter.addMessage("Обнаружен не к месту Begin в строке $i")
//                return false
//            }
//            else if(listBlocks[i].getBlockType() == "End"){
//                consoleAdapter.addMessage("Обнаружен не к месту End в строке $i")
//                return false
//            }
//            else if(listBlocks[i].getBlockType() == "Else"){
//                consoleAdapter.addMessage("Обнаружен не к месту Else в строке $i")
//                return false
//            }
//            else if (listBlocks[i].getBlockType() == "ConditionIf" || listBlocks[i].getBlockType() == "ConditionIfElse" || listBlocks[i].getBlockType() == "CycleWhile") {
//                val temp : Int
//                if(listBlocks[i].getBlockType() == "ConditionIfElse")
//                    temp = checkIfElse(i)
//                else
//                    temp = checkIf(i)
//                if(temp == 0)
//                    return false
//                else {
//                    if (temp < listBlocks.size){
//                        connectBlocks(listBlocks[i], listBlocks[temp])
//                        i = temp
//                    }
//                    else
//                        break
//                }
//            }
//            else{
//                connectBlocks(listBlocks[i], listBlocks[i + 1])
//                i += 1
//            }
//        }
//        return true
//    }
//
//    private fun runProject(listBlocks : ArrayList<Block>){
//        consoleAdapter.clearListMessages()
//        if (!connectionBlocks()) {
//            consoleAdapter.addMessage("Program finished with status: Fail")
//            return
//        }
//        blocksAdapter.saveAllData()
//        listBlocks[0].run()
//        blocksAdapter.notifyItemRangeChanged(0, listBlocks.size)
//    }
//
//    private fun printAllConnections(){
//        for (i in 0 until listBlocks.size){
//            consoleAdapter.addMessage("$i "+ (listBlocks[i].getPrevBlock()
//                ?.getBlockType() ?: "null") + "-"+ listBlocks[i].getBlockType() + "-" +
//                    (listBlocks[i].getNextBlock()?.getBlockType() ?: "null"))
//
//        }
//    }
//}