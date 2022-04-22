package com.apochromat.codeblockmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.apochromat.codeblockmobile.databinding.ActivityMainBinding
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    lateinit var bindingClass : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)
        supportActionBar?.title = ""
        bindingClass.buttonCreateNewProject.setOnClickListener{
            val intent = Intent(this@MainActivity, ProjectActivity::class.java)
            startActivity(intent)
        }
        bindingClass.buttonAboutAuthors.setOnClickListener{
            val intent = Intent(this@MainActivity, AuthorsActivity::class.java)
            startActivity(intent)
        }
        bindingClass.buttonExitApp.setOnClickListener{
            finishAffinity()
        }
    }
}