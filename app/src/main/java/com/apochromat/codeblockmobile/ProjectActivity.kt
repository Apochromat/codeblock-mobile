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

        val blocksView : RecyclerView = findViewById(R.id.recyclerView)
        blocksView.layoutManager = LinearLayoutManager(this)
        listBlocks = fetchData()
        blocksAdapter = BlocksAdapter(listBlocks)
        blocksView.adapter = blocksAdapter

        val consoleView : RecyclerView = bindingClass.messageRV
        consoleView.layoutManager = LinearLayoutManager(this)
        listMessage = setListMessage()
        consoleAdapter = ConsoleAdapter(listMessage)
        consoleView.adapter = consoleAdapter

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(blocksView)
        init()

    }
    private fun init(){
        bindingClass.apply{
            buttonDefinedVar.setOnClickListener{
                blocksAdapter.addBlock(DefinedVariable())
            }
            buttonUndefinedVar.setOnClickListener{
                blocksAdapter.addBlock(UndefinedVariable())
            }
            buttonAssignment.setOnClickListener{
                blocksAdapter.addBlock(Assignment())
            }
            buttonConditionIf.setOnClickListener{
                blocksAdapter.addBlock(ConditionIf())
            }
            buttonConditionIfElse.setOnClickListener{
                blocksAdapter.addBlock(ConditionIfElse())
            }
            buttonCycleWhile.setOnClickListener{
                blocksAdapter.addBlock(CycleWhile())
            }
            buttonConsoleOutput.setOnClickListener{
                blocksAdapter.addBlock(ConsoleOutput())
            }
            buttonConsoleInputOne.setOnClickListener{
                blocksAdapter.addBlock(ConsoleInputOne())
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
                runProject(listBlocks)
                Toast.makeText(this, "Запуск...", Toast.LENGTH_SHORT).show()
            }
            R.id.menuSave -> {
                Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show()
            }
            R.id.menuConsole -> {
                bindingClass.drawer.openDrawer(GravityCompat.START)
            }
        }
        return true
    }

    private val simpleCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END, 0){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPosition = viewHolder.adapterPosition
            val toPosition = target.adapterPosition
            Collections.swap(listBlocks, fromPosition, toPosition)

            blocksAdapter.notifyItemMoved(fromPosition, toPosition)

            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        }
    }

    private fun fetchData() : ArrayList<Block>{
        val list = ArrayList<Block>()
        list.add(EntryPoint())
        return list
    }
    private fun setListMessage(): ArrayList<String> {
        return ArrayList()
    }

    private fun runProject(listBlocks : ArrayList<Block>){
        for (i in 0 until listBlocks.size-1){
            connectBlocks(listBlocks[i], listBlocks[i+1], true, false)
        }
        for (i in 0 until listBlocks.size){
            consoleAdapter.addMessage(listBlocks[i].getBlockType())
        }

    }
}