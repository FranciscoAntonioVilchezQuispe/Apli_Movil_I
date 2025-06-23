package com.example.proyectoapuntate_apli_movil_i

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.proyectoapuntate_apli_movil_i.Entidades.Cliente
import com.example.proyectoapuntate_apli_movil_i.Entidades.Login
import com.example.proyectoapuntate_apli_movil_i.database.DBHelper
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegistroActivity : AppCompatActivity() {
    private var isViewMode: Boolean = false
    private var userId: String? = null
    private val dbHelper = DBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)
        // Recibir parámetros
        isViewMode = intent.getBooleanExtra("IS_VIEW_MODE", false)
        userId = intent.getStringExtra("USER_ID")
        configurarPantalla()
        if (isViewMode) {
            cargarDatosUsuario(userId.toString())
        }
        val BRegresar = findViewById<MaterialButton>(R.id.btRegresar)

        val botonCrear = findViewById<MaterialButton>(R.id.btCrear)
        BRegresar.setOnClickListener {
            if (isViewMode) {
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("USER_ID", userId)
                startActivity(intent)
            }
            else{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

        }
        botonCrear.setOnClickListener {
            val documento = findViewById<TextInputEditText>(R.id.etDocumento).text.toString()
            val nombres = findViewById<TextInputEditText>(R.id.etNombres).text.toString()
            val apellidop = findViewById<TextInputEditText>(R.id.etApellidoPaterno).text.toString()
            val apellidom = findViewById<TextInputEditText>(R.id.etApellidoMaterno).text.toString()
            val telefono = findViewById<TextInputEditText>(R.id.etTelefono).text.toString()
            val direccion = findViewById<TextInputEditText>(R.id.etDireccion).text.toString()
            val correo = findViewById<TextInputEditText>(R.id.etCorreo).text.toString()
            val contrasena = findViewById<TextInputEditText>(R.id.etContrasena).text.toString()
            val repetirContrasena =
                findViewById<TextInputEditText>(R.id.etRepetirContrasena).text.toString()


            if (
                documento.isBlank() || nombres.isBlank() || apellidop.isBlank() ||
                apellidom.isBlank() || contrasena.isBlank() || repetirContrasena.isBlank()
            ) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (contrasena != repetirContrasena) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var cliente: Cliente = Cliente()
            cliente.Documento = documento
            cliente.Nombres = nombres
            cliente.Apellidop = apellidop
            cliente.Apellidom = apellidom
            cliente.Telefono = telefono
            cliente.Correo = correo
            cliente.Direccion = direccion


            val exito = dbHelper.insertCliente(cliente)

            if (exito > 0) {
                var user: Login = Login()
                user.IdCliente = exito.toInt()
                user.Clave = contrasena
                var respuesta = dbHelper.insertLogin(user)
                if (respuesta.toInt() == 0) {
                    Toast.makeText(this, "Error al registrar", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                Toast.makeText(this, "Cliente registrado correctamente", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Error al registrar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }

    }

    fun configurarPantalla() {
        val titulo = findViewById<TextView>(R.id.textView25)
        val completa = findViewById<TextView>(R.id.etcompleta)
        val datasacceso = findViewById<TextView>(R.id.etdatasacceso)

        val documento = findViewById<TextInputEditText>(R.id.etDocumento)
        val nombres = findViewById<TextInputEditText>(R.id.etNombres)
        val apellidop = findViewById<TextInputEditText>(R.id.etApellidoPaterno)
        val apellidom = findViewById<TextInputEditText>(R.id.etApellidoMaterno)
        val telefono = findViewById<TextInputEditText>(R.id.etTelefono)
        val direccion = findViewById<TextInputEditText>(R.id.etDireccion)
        val correo = findViewById<TextInputEditText>(R.id.etCorreo)
        val contrasena = findViewById<TextInputLayout>(R.id.ilclave)
        val repetirContrasena = findViewById<TextInputLayout>(R.id.ilrepetirclave)
        val botonCrear = findViewById<Button>(R.id.btCrear)
        if (isViewMode) {
            // Modo visualización - campos deshabilitados
            titulo.text = "Ver Perfil"
            completa.isVisible = false
            datasacceso.isVisible = false
            documento.isEnabled = false
            nombres.isEnabled = false
            apellidop.isEnabled = false
            apellidom.isEnabled = false
            telefono.isEnabled = false
            direccion.isEnabled = false
            correo.isEnabled = false
            contrasena.isVisible = false
            repetirContrasena.isVisible = false
            botonCrear.isVisible = false
        } else {
            // Modo registro - campos habilitados
            titulo.text = "Registrar Usuario"
            completa.isVisible = true
            datasacceso.isVisible = true
            documento.isEnabled = true
            nombres.isEnabled = true
            apellidop.isEnabled = true
            apellidom.isEnabled = true
            telefono.isEnabled = true
            direccion.isEnabled = true
            correo.isEnabled = true
            contrasena.isVisible = true
            repetirContrasena.isVisible = true
            botonCrear.isVisible = true
        }
    }

    fun cargarDatosUsuario(userId: String) {

        val documento = findViewById<TextInputEditText>(R.id.etDocumento)
        val nombres = findViewById<TextInputEditText>(R.id.etNombres)
        val apellidop = findViewById<TextInputEditText>(R.id.etApellidoPaterno)
        val apellidom = findViewById<TextInputEditText>(R.id.etApellidoMaterno)
        val telefono = findViewById<TextInputEditText>(R.id.etTelefono)
        val direccion = findViewById<TextInputEditText>(R.id.etDireccion)
        val correo = findViewById<TextInputEditText>(R.id.etCorreo)
        val userData = dbHelper.getLoginById(userId.toInt())
        val clidata = userData?.let { dbHelper.getClienteById(it.IdCliente) }
        clidata?.let {
            documento.setText(it.Documento)
            nombres.setText(it.Nombres)
            apellidop.setText(it.Apellidop)
            apellidom.setText(it.Apellidom)
            telefono.setText(it.Telefono)
            direccion.setText(it.Direccion)
            correo.setText(it.Correo)

        }
    }

}