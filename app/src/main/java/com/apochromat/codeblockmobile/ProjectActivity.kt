package com.apochromat.codeblockmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apochromat.codeblockmobile.databinding.ActivityProjectBinding
import com.apochromat.codeblockmobile.logic.*
import java.util.*
import kotlin.collections.ArrayList

class ProjectActivity : AppCompatActivity() {
    private lateinit var listBlocks : ArrayList<Block>
    private lateinit var listMessage : ArrayList<String>
    private lateinit var blocksAdapter : BlocksAdapter
    lateinit var consoleAdapter : ConsoleAdapter
    lateinit var bindingClass : ActivityProjectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityProjectBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "newProject"

        bindingClass.blocksRV.isDrawingCacheEnabled = true
        bindingClass.blocksRV.setItemViewCacheSize(100);

        createConsoleView()
        createBlocksView()

        init()
    }
    private fun init(){
        bindingClass.apply{
            buttonDefinedVar.setOnClickListener{
                blocksAdapter.addBlock(DefinedVariable())
                listBlocks[listBlocks.size-1].adapter = consoleAdapter
            }
            buttonUndefinedVar.setOnClickListener{
                blocksAdapter.addBlock(UndefinedVariable())
                listBlocks[listBlocks.size-1].adapter = consoleAdapter
            }
            buttonAssignment.setOnClickListener{
                blocksAdapter.addBlock(Assignment())
                listBlocks[listBlocks.size-1].adapter = consoleAdapter
            }
            buttonConditionIf.setOnClickListener{
                blocksAdapter.addBlock(ConditionIf())
                blocksAdapter.addBlock(Begin())
                blocksAdapter.addBlock(End())
                listBlocks[listBlocks.size-3].adapter = consoleAdapter
                listBlocks[listBlocks.size-3].begin = Begin()
                listBlocks[listBlocks.size-3].end = End()
                listBlocks[listBlocks.size-2].adapter = consoleAdapter
                listBlocks[listBlocks.size-1].adapter = consoleAdapter
            }
            buttonConditionIfElse.setOnClickListener{
                blocksAdapter.addBlock(ConditionIfElse())
                listBlocks[listBlocks.size-1].adapter = consoleAdapter
            }
            buttonCycleWhile.setOnClickListener{
                blocksAdapter.addBlock(CycleWhile())
                listBlocks[listBlocks.size-1].adapter = consoleAdapter
            }
            buttonConsoleOutput.setOnClickListener{
                blocksAdapter.addBlock(ConsoleOutput())
                listBlocks[listBlocks.size-1].adapter = consoleAdapter
            }
            buttonConsoleInputOne.setOnClickListener{
                blocksAdapter.addBlock(ConsoleInputOne())
                listBlocks[listBlocks.size-1].adapter = consoleAdapter
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.project_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                val intent = Intent(this@ProjectActivity, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.menuRun -> {
                Toast.makeText(this, "Запуск...", Toast.LENGTH_SHORT).show()
                runProject(listBlocks)
            }
            R.id.menuSave -> {
                blocksAdapter.saveAllData()
                Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show()
            }
            R.id.menuConsole -> {
                bindingClass.drawer.openDrawer(GravityCompat.START)
            }
        }
        return true
    }

    private val simpleCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN or
            ItemTouchHelper.START or ItemTouchHelper.END, ItemTouchHelper.LEFT){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            if (viewHolder.adapterPosition == 0 || target.adapterPosition == 0){
                return true
            }
            val fromPosition = viewHolder.adapterPosition
            val toPosition = target.adapterPosition
            Collections.swap(listBlocks, fromPosition, toPosition)

            blocksAdapter.notifyItemMoved(fromPosition, toPosition)

            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position =  viewHolder.adapterPosition
            if (viewHolder.adapterPosition == 0){
                return
            }
            listBlocks.remove(listBlocks[position])
            blocksAdapter.notifyItemRemoved(position)
        }
    }
    private fun createBlocksView(){
        val blocksView : RecyclerView = findViewById(R.id.blocksRV)
        blocksView.layoutManager = LinearLayoutManager(this)
        listBlocks = setListBlocks()
        blocksAdapter = BlocksAdapter(listBlocks)
        blocksView.adapter = blocksAdapter
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(blocksView)
    }
    private fun createConsoleView(){
        val consoleView : RecyclerView = bindingClass.messageRV
        consoleView.layoutManager = LinearLayoutManager(this)
        listMessage = setListMessage()
        consoleAdapter = ConsoleAdapter(listMessage)
        consoleView.adapter = consoleAdapter
    }
    private fun setListBlocks() : ArrayList<Block>{
        val list = ArrayList<Block>()
        list.add(EntryPoint())
        list[0].adapter = consoleAdapter
        return list
    }
    private fun setListMessage(): ArrayList<String> {
        return ArrayList()
    }

    private fun check(index : Int): Int{
        var i = index + 1
        if (listBlocks[i].getBlockType() == "Begin"){
            i += 1
            if (listBlocks[i].getBlockType() == "End"){
                return -1
            }
            connectBlocks(listBlocks[index].begin, listBlocks[i])
            consoleAdapter.addMessage("$index $i")
            while (listBlocks[i+1].getBlockType() != "End"){
                connectBlocks(listBlocks[i], listBlocks[i+1])
                consoleAdapter.addMessage("$i $i+1")
                i += 1
            }
//            if (i+1 == listBlocks.size-1){
//                return 0
//            }
            connectBlocks(listBlocks[i], listBlocks[index].end)
            consoleAdapter.addMessage("$i $index")
            return i + 2
        }
        else{
            return 0
        }
    }

    private fun runProject(listBlocks : ArrayList<Block>){
        consoleAdapter.clearListMessages()
        var i = 0
        while (i < listBlocks.size-1) {
            if (listBlocks[i].getBlockType() != "ConditionIf" ) {
                connectBlocks(listBlocks[i], listBlocks[i + 1])
                i += 1
            }
            else{
                val cur = check(i)
                if (cur == -1){
                    i += 3
                }
                else if(cur == 0){
                    consoleAdapter.addMessage("Ошибка соединения в if под номером $i")
                    return
                }
                else{
                    if (cur < listBlocks.size){
                        connectBlocks(listBlocks[i], listBlocks[cur])
                    }
                    i = cur
                }
            }
        }
        blocksAdapter.saveAllData()
        listBlocks[0].run()
//        for (i in 0 until listBlocks.size-1){
//            disconnectBlocks(listBlocks[i], listBlocks[i+1])
//        }
        blocksAdapter.notifyItemRangeChanged(0, listBlocks.size)
    }
}