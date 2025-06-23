package com.example.proyectoapuntate_apli_movil_i

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomeActivity : AppCompatActivity() {
    private lateinit var btnTarea: AppCompatButton
    private lateinit var btnApunte: AppCompatButton
    private lateinit var btnVerPerfil: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initViews()
        setupClickListeners()
    }

    private fun initViews() {
        btnTarea = findViewById(R.id.btn_tarea)
        btnApunte = findViewById(R.id.btn_apunte)
        btnVerPerfil = findViewById(R.id.btn_ver_perfil)
    }

    private fun setupClickListeners() {
        btnTarea.setOnClickListener {
            val intent = Intent(this, TareasActivity::class.java)
            startActivity(intent)
        }

        btnApunte.setOnClickListener {
            val intent = Intent(this, NotasActivity::class.java)
            startActivity(intent)
        }

        btnVerPerfil.setOnClickListener {
            // Navegar a la pantalla de Perfil
            Toast.makeText(this, "Navegando a Perfil", Toast.LENGTH_SHORT).show()
            // startActivity(Intent(this, PerfilActivity::class.java))
        }
    }
}