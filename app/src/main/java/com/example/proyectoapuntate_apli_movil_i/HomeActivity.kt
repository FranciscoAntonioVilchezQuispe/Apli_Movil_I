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
    private var userId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        // Recibir el ID del usuario desde Login
        userId = intent.getStringExtra("USER_ID")
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
            intent.putExtra("USER_ID", userId) // Enviar ID del usuario
            startActivity(intent)
        }

        btnVerPerfil.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            intent.putExtra("USER_ID", userId) // Enviar ID del usuario
            intent.putExtra("IS_VIEW_MODE", true) // Modo visualización
            startActivity(intent)
        }
    }
}