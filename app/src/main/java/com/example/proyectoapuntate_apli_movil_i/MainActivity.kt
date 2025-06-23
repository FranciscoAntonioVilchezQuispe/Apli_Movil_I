package com.example.proyectoapuntate_apli_movil_i

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val BRegistrar=findViewById<Button>(R.id.btRegistrar)
        val btnIngresar=findViewById<Button>(R.id.btIngresar)

        BRegistrar.setOnClickListener{
            val intent= Intent(this,RegistroActivity::class.java)
            startActivity(intent)
        }

        btnIngresar.setOnClickListener{
            val intent= Intent(this,NotasActivity::class.java)
            startActivity(intent)
        }
        val btnTareas = findViewById<Button>(R.id.btnTareas)
        btnTareas.setOnClickListener {
            val intent = Intent(this, TareasActivity::class.java)
            startActivity(intent)
        }
    }
}