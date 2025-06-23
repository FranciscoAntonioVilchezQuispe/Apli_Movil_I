package com.example.proyectoapuntate_apli_movil_i

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyectoapuntate_apli_movil_i.Entidades.Cliente
import com.example.proyectoapuntate_apli_movil_i.Entidades.Login
import com.example.proyectoapuntate_apli_movil_i.database.DBHelper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val BRegistrar=findViewById<TextView>(R.id.tvRegistrar)
        val btnIngresar=findViewById<Button>(R.id.btIngresar)


        val dbHelper = DBHelper(this)
        BRegistrar.setOnClickListener{
            val intent= Intent(this,RegistroActivity::class.java)
            startActivity(intent)
        }


        btnIngresar.setOnClickListener {
            val documento=findViewById<EditText>(R.id.etDocumento).text.toString()
            val clave=findViewById<EditText>(R.id.etContrasena).text.toString()
var cliente:Cliente
            cliente= dbHelper.getClienteByDocumento(documento)!!
            if(cliente==null || cliente.IdCliente==0){
                Toast.makeText(this, "El cliente no existe", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            var login:Login=Login()
            login.IdCliente=cliente.IdCliente
            login.Clave=clave
            login= dbHelper.getLoginByCredenciales(login)!!
            if (login==null || login.IdLogin==0){
                Toast.makeText(this, "El cliente tiene Acceso", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("USER_ID", login.IdLogin.toString())
            startActivity(intent)
        }
    }
}