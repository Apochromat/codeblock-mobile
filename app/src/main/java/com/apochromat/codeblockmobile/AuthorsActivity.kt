//package com.apochromat.codeblockmobile
//
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.view.MenuItem
//import android.widget.Toast
//import com.apochromat.codeblockmobile.databinding.ActivityAuthorsBinding
//import com.apochromat.codeblockmobile.databinding.ActivityMainBinding
//
//class AuthorsActivity : AppCompatActivity() {
//    lateinit var bindingClass : ActivityAuthorsBinding
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        bindingClass = ActivityAuthorsBinding.inflate(layoutInflater)
//        setContentView(bindingClass.root)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.title = ""
//    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId){
//            android.R.id.home -> {
//                val intent = Intent(this@AuthorsActivity, MainActivity::class.java)
//                startActivity(intent)
//            }
//        }
//        return true
//    }
//}