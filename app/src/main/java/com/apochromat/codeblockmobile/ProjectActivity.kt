package com.apochromat.codeblockmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apochromat.codeblockmobile.databinding.ActivityProjectBinding
import com.apochromat.codeblockmobile.logic.*
import java.util.*
import kotlin.collections.ArrayList

class ProjectActivity : AppCompatActivity() {
    private lateinit var items : ArrayList<Block>
    private lateinit var adaptor : RVAdaptor
    lateinit var bindingClass : ActivityProjectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityProjectBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "newProject"

        val recyclerView : RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        items = fetchData()
        adaptor = RVAdaptor(items)
        recyclerView.adapter = adaptor

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        init()

    }
    private fun init(){
        bindingClass.apply{
            buttonDefinedVar.setOnClickListener{
                adaptor.addBlock(DefinedVariable())
            }
            buttonUndefinedVar.setOnClickListener{
                adaptor.addBlock(UndefinedVariable())
            }
            buttonAssignment.setOnClickListener{
                adaptor.addBlock(Assignment())
            }
            buttonConditionIf.setOnClickListener{
                adaptor.addBlock(ConditionIf())
            }
            buttonConditionIfElse.setOnClickListener{
                adaptor.addBlock(ConditionIfElse())
            }
            buttonCycleWhile.setOnClickListener{
                adaptor.addBlock(CycleWhile())
            }
            buttonConsoleOutput.setOnClickListener{
                adaptor.addBlock(ConsoleOutput())
            }
            buttonConsoleInputOne.setOnClickListener{
                adaptor.addBlock(ConsoleInputOne())
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
            }
            R.id.menuSave -> {
                Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show()
            }
            R.id.menuConsole -> {
                Toast.makeText(this, "Консоль открывается...", Toast.LENGTH_SHORT).show()
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
            Collections.swap(items, fromPosition, toPosition)

            adaptor.notifyItemMoved(fromPosition, toPosition)

            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        }

    }

    private fun fetchData() : ArrayList<Block>{
        val list = ArrayList<Block>()
        return list
    }
}