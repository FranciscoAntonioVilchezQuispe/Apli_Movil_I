package com.example.proyectoapuntate_apli_movil_i

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectoapuntate_apli_movil_i.Entidades.Cliente
import com.example.proyectoapuntate_apli_movil_i.Entidades.Login
import com.example.proyectoapuntate_apli_movil_i.database.DBHelper

class RegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)

        val BRegresar = findViewById<ImageButton>(R.id.btRegresar)
        val dbHelper = DBHelper(this)
        val botonCrear = findViewById<Button>(R.id.btCrear)
        BRegresar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        botonCrear.setOnClickListener {
            val documento = findViewById<EditText>(R.id.editTextNumber).text.toString()
            val nombres = findViewById<EditText>(R.id.editTextText2).text.toString()
            val apellidop = findViewById<EditText>(R.id.editTextText3).text.toString()
            val apellidom = findViewById<EditText>(R.id.editTextText4).text.toString()
            val telefono = findViewById<EditText>(R.id.editTextText).text.toString()
            val direccion = findViewById<EditText>(R.id.editTextText5).text.toString()
            val correo = findViewById<EditText>(R.id.editTextText6).text.toString()
            val contrasena = findViewById<EditText>(R.id.editTextTextPassword2).text.toString()
            val repetirContrasena =
                findViewById<EditText>(R.id.editTextTextPassword3).text.toString()


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
}