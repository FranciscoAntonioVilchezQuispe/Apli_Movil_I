package com.example.proyectoapuntate_apli_movil_i.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.proyectoapuntate_apli_movil_i.Entidades.Cliente
import com.example.proyectoapuntate_apli_movil_i.Entidades.Login
import com.example.proyectoapuntate_apli_movil_i.Entidades.Notas
import com.example.proyectoapuntate_apli_movil_i.Entidades.Tarea
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DBHelper(context: Context) : SQLiteOpenHelper(context, "BdApuntes.db", null, 4) {
    companion object {
        private const val DATABASE_NAME = "AppDatabase.db"
        private const val DATABASE_VERSION = 1

        // Tabla Cliente
        private const val TABLE_CLIENTE = "Cliente"
        private const val COL_ID_CLIENTE = "IdCliente"
        private const val COL_NOMBRES = "Nombres"
        private const val COL_APELLIDO_P = "Apellidop"
        private const val COL_APELLIDO_M = "Apellidom"
        private const val COL_TELEFONO = "Telefono"
        private const val COL_DIRECCION = "Direccion"
        private const val COL_CORREO = "Correo"
        private const val COL_DOCUMENTO = "Documento"
        private const val COL_FECHA_CLIENTE = "Fecha"

        // Tabla Login
        private const val TABLE_LOGIN = "Login"
        private const val COL_ID_LOGIN = "IdLogin"
        private const val COL_ID_CLIENTE_LOGIN = "IdCliente"
        private const val COL_CLAVE = "Clave"
        private const val COL_FECHA_LOGIN = "Fecha"



        // Tabla Tarea
        private const val TABLE_TAREA = "Tarea"
        private const val COL_APUNTE_ID_TAREA = "ApunteId"
        private const val COL_TAREA_ID = "TareaId"
        private const val COL_TITULO_TAREA = "Titulo"
        private const val COL_DESCRIPCION_TAREA = "Descripcion"
        private const val COL_ESTADO = "Estado"
        private const val COL_FECHA_TAREA = "Fecha"

        //Tabla notas
        private const val TABLE_NOTAS = "notas"
        private const val COLUMN_ID_NOTAS = "id"
        private const val COLUMN_TITULO_NOTAS = "titulo"
        private const val COLUMN_PROGRESO_NOTAS = "progreso"
    }

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    override fun onCreate(db: SQLiteDatabase?) {


        // Crear tabla Cliente
        val createClienteTable = """
            CREATE TABLE $TABLE_CLIENTE (
                $COL_ID_CLIENTE INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NOMBRES TEXT NOT NULL,
                $COL_DOCUMENTO TEXT NOT NULL,
                $COL_APELLIDO_P TEXT NOT NULL,
                $COL_APELLIDO_M TEXT NOT NULL,
                $COL_TELEFONO TEXT,
                $COL_DIRECCION TEXT,
                $COL_CORREO TEXT,
                $COL_FECHA_CLIENTE TEXT
            )
        """.trimIndent()

        // Crear tabla Login
        val createLoginTable = """
            CREATE TABLE $TABLE_LOGIN (
                $COL_ID_LOGIN INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_ID_CLIENTE_LOGIN INTEGER,
                $COL_CLAVE TEXT NOT NULL,
                $COL_FECHA_LOGIN TEXT,
                FOREIGN KEY($COL_ID_CLIENTE_LOGIN) REFERENCES $TABLE_CLIENTE($COL_ID_CLIENTE)
            )
        """.trimIndent()


        // Crear tabla Tarea
        val createTareaTable = """
            CREATE TABLE $TABLE_TAREA (
                $COL_APUNTE_ID_TAREA INTEGER,
                $COL_TAREA_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_TITULO_TAREA TEXT NOT NULL,
                $COL_DESCRIPCION_TAREA TEXT,
                $COL_ESTADO TEXT,
                $COL_FECHA_TAREA TEXT
            )
        """.trimIndent()


        val createTableNotas = """
            CREATE TABLE $TABLE_NOTAS (
                $COLUMN_ID_NOTAS INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITULO_NOTAS TEXT,
                $COLUMN_PROGRESO_NOTAS INTEGER
            )
        """.trimIndent()

        db?.execSQL(createTableNotas)

        db?.execSQL(createClienteTable)
        db?.execSQL(createLoginTable)
        db?.execSQL(createTareaTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        db?.execSQL("DROP TABLE IF EXISTS $TABLE_TAREA")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_LOGIN")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CLIENTE")
     //   db?.execSQL("DROP TABLE IF EXISTS $TABLE_NOTAS")

        onCreate(db)


    }

    // MÉTODOS PARA CLIENTE
    fun insertCliente(cliente: Cliente): Long {
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put(COL_NOMBRES, cliente.Nombres)
                put(COL_DOCUMENTO, cliente.Documento)
                put(COL_APELLIDO_P, cliente.Apellidop)
                put(COL_APELLIDO_M, cliente.Apellidom)
                put(COL_TELEFONO, cliente.Telefono)
                put(COL_DIRECCION, cliente.Direccion)
                put(COL_CORREO, cliente.Correo)
                put(COL_FECHA_CLIENTE, dateFormat.format(cliente.Fecha))
            }

            // insertOrThrow lanza excepción en lugar de devolver -1
         return db.insertOrThrow(TABLE_CLIENTE, null, values)

        } catch (e: SQLiteConstraintException) {
            Log.e("DatabaseError", "Violación de restricción: ${e.message}")
            -1L
        } catch (e: SQLiteException) {
            Log.e("DatabaseError", "Error SQLite: ${e.message}")
            -1L
        } finally {
            db.close()
        }
    }
    fun updateCliente(cliente: Cliente): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_NOMBRES, cliente.Nombres)
            put(COL_DOCUMENTO, cliente.Documento)
            put(COL_APELLIDO_P, cliente.Apellidop)
            put(COL_APELLIDO_M, cliente.Apellidom)
            put(COL_TELEFONO, cliente.Telefono)
            put(COL_DIRECCION, cliente.Direccion)
            put(COL_CORREO, cliente.Correo)
            put(COL_FECHA_CLIENTE, dateFormat.format(cliente.Fecha))
        }
        val result = db.update(
            TABLE_CLIENTE,
            values,
            "$COL_ID_CLIENTE = ?",
            arrayOf(cliente.IdCliente.toString())
        )
        db.close()
        return result
    }

    fun getClienteById(id: Int): Cliente? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_CLIENTE,
            null,
            "$COL_ID_CLIENTE = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        var cliente: Cliente? = null
        if (cursor.moveToFirst()) {
            cliente = Cliente(
                IdCliente = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID_CLIENTE)),
                Nombres = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOMBRES)),
                Documento = cursor.getString(cursor.getColumnIndexOrThrow(COL_DOCUMENTO)),
                Apellidop = cursor.getString(cursor.getColumnIndexOrThrow(COL_APELLIDO_P)),
                Apellidom = cursor.getString(cursor.getColumnIndexOrThrow(COL_APELLIDO_M)),
                Telefono = cursor.getString(cursor.getColumnIndexOrThrow(COL_TELEFONO)) ?: "",
                Direccion = cursor.getString(cursor.getColumnIndexOrThrow(COL_DIRECCION)) ?: "",
                Correo = cursor.getString(cursor.getColumnIndexOrThrow(COL_CORREO)) ?: "",
                Fecha = dateFormat.parse(
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            COL_FECHA_CLIENTE
                        )
                    )
                ) ?: Date()
            )
        }
        cursor.close()
        db.close()
        return cliente
    }
    fun getClienteByDocumento(documento: String): Cliente? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_CLIENTE,
            null,
            "$COL_DOCUMENTO = ?",
            arrayOf(documento.toString()),
            null,
            null,
            null
        )

        var cliente: Cliente? = null
        if (cursor.moveToFirst()) {
            cliente = Cliente(
                IdCliente = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID_CLIENTE)),
                Nombres = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOMBRES)),
                Documento = cursor.getString(cursor.getColumnIndexOrThrow(COL_DOCUMENTO)),
                Apellidop = cursor.getString(cursor.getColumnIndexOrThrow(COL_APELLIDO_P)),
                Apellidom = cursor.getString(cursor.getColumnIndexOrThrow(COL_APELLIDO_M)),
                Telefono = cursor.getString(cursor.getColumnIndexOrThrow(COL_TELEFONO)) ?: "",
                Direccion = cursor.getString(cursor.getColumnIndexOrThrow(COL_DIRECCION)) ?: "",
                Correo = cursor.getString(cursor.getColumnIndexOrThrow(COL_CORREO)) ?: "",
                Fecha = dateFormat.parse(
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            COL_FECHA_CLIENTE
                        )
                    )
                ) ?: Date()
            )
        }
        cursor.close()
        db.close()
        return cliente
    }
    fun getAllClientes(): List<Cliente> {
        val clientes = mutableListOf<Cliente>()
        val db = readableDatabase
        val cursor = db.query(TABLE_CLIENTE, null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val cliente = Cliente(
                    IdCliente = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID_CLIENTE)),
                    Nombres = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOMBRES)),
                    Documento = cursor.getString(cursor.getColumnIndexOrThrow(COL_DOCUMENTO)),
                    Apellidop = cursor.getString(cursor.getColumnIndexOrThrow(COL_APELLIDO_P)),
                    Apellidom = cursor.getString(cursor.getColumnIndexOrThrow(COL_APELLIDO_M)),
                    Telefono = cursor.getString(cursor.getColumnIndexOrThrow(COL_TELEFONO)) ?: "",
                    Direccion = cursor.getString(cursor.getColumnIndexOrThrow(COL_DIRECCION)) ?: "",
                    Correo = cursor.getString(cursor.getColumnIndexOrThrow(COL_CORREO)) ?: "",
                    Fecha = dateFormat.parse(
                        cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                COL_FECHA_CLIENTE
                            )
                        )
                    ) ?: Date()
                )
                clientes.add(cliente)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return clientes
    }

    // MÉTODOS PARA LOGIN
    fun insertLogin(login: Login): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_ID_CLIENTE_LOGIN, login.IdCliente)
            put(COL_CLAVE, login.Clave)
            put(COL_FECHA_LOGIN, dateFormat.format(login.Fecha))
        }
        val result = db.insert(TABLE_LOGIN, null, values)
        db.close()
        return result
    }

    fun updateLogin(login: Login): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_ID_CLIENTE_LOGIN, login.IdCliente)
            put(COL_CLAVE, login.Clave)
            put(COL_FECHA_LOGIN, dateFormat.format(login.Fecha))
        }
        val result =
            db.update(TABLE_LOGIN, values, "$COL_ID_LOGIN = ?", arrayOf(login.IdLogin.toString()))
        db.close()
        return result
    }

    fun getLoginByCredenciales(request: Login): Login? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_LOGIN,
            null,
            "$COL_ID_CLIENTE_LOGIN = ? AND $COL_CLAVE = ?",
            arrayOf(request.IdCliente.toString(),request.Clave.toString()),
            null,
            null,
            null
        )

        var login: Login? = null
        if (cursor.moveToFirst()) {
            login = Login(
                IdLogin = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID_LOGIN)),
                IdCliente = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID_CLIENTE_LOGIN)),
                Clave = cursor.getString(cursor.getColumnIndexOrThrow(COL_CLAVE)),
                Fecha = dateFormat.parse(
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            COL_FECHA_LOGIN
                        )
                    )
                ) ?: Date()
            )
        }
        cursor.close()
        db.close()
        return login
    }

    fun getLoginById(id: Int): Login? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_LOGIN,
            null,
            "$COL_ID_LOGIN = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        var login: Login? = null
        if (cursor.moveToFirst()) {
            login = Login(
                IdLogin = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID_LOGIN)),
                IdCliente = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID_CLIENTE_LOGIN)),
                Clave = cursor.getString(cursor.getColumnIndexOrThrow(COL_CLAVE)),
                Fecha = dateFormat.parse(
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            COL_FECHA_LOGIN
                        )
                    )
                ) ?: Date()
            )
        }
        cursor.close()
        db.close()
        return login
    }

    fun getAllLogins(): List<Login> {
        val logins = mutableListOf<Login>()
        val db = readableDatabase
        val cursor = db.query(TABLE_LOGIN, null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val login = Login(
                    IdLogin = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID_LOGIN)),
                    IdCliente = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID_CLIENTE_LOGIN)),
                    Clave = cursor.getString(cursor.getColumnIndexOrThrow(COL_CLAVE)),
                    Fecha = dateFormat.parse(
                        cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                COL_FECHA_LOGIN
                            )
                        )
                    ) ?: Date()
                )
                logins.add(login)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return logins
    }

    // MÉTODOS PARA TAREA
    fun insertTarea(tarea: Tarea): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_APUNTE_ID_TAREA, tarea.ApunteId)
            put(COL_TITULO_TAREA, tarea.Titulo)
            put(COL_DESCRIPCION_TAREA, tarea.Descripcion)
            put(COL_ESTADO, tarea.Estado)
            put(COL_FECHA_TAREA, dateFormat.format(tarea.Fecha))
        }
        val result = db.insert(TABLE_TAREA, null, values)
        db.close()
        return result
    }

    fun updateTarea(tarea: Tarea): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_APUNTE_ID_TAREA, tarea.ApunteId)
            put(COL_TITULO_TAREA, tarea.Titulo)
            put(COL_DESCRIPCION_TAREA, tarea.Descripcion)
            put(COL_ESTADO, tarea.Estado)
            put(COL_FECHA_TAREA, dateFormat.format(tarea.Fecha))
        }
        val result =
            db.update(TABLE_TAREA, values, "$COL_TAREA_ID = ?", arrayOf(tarea.TareaId.toString()))
        db.close()
        return result
    }

    fun getTareaById(id: Int): Tarea? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_TAREA,
            null,
            "$COL_TAREA_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        var tarea: Tarea? = null
        if (cursor.moveToFirst()) {
            tarea = Tarea(
                ApunteId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_APUNTE_ID_TAREA)),
                TareaId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_TAREA_ID)),
                Titulo = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITULO_TAREA)),
                Descripcion = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPCION_TAREA))
                    ?: "",
                Estado = cursor.getString(cursor.getColumnIndexOrThrow(COL_ESTADO)) ?: "",
                Fecha = dateFormat.parse(
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            COL_FECHA_TAREA
                        )
                    )
                ) ?: Date()
            )
        }
        cursor.close()
        db.close()
        return tarea
    }

    fun getAllTareas(): List<Tarea> {
        val tareas = mutableListOf<Tarea>()
        val db = readableDatabase
        val cursor = db.query(TABLE_TAREA, null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val tarea = Tarea(
                    ApunteId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_APUNTE_ID_TAREA)),
                    TareaId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_TAREA_ID)),
                    Titulo = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITULO_TAREA)),
                    Descripcion = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            COL_DESCRIPCION_TAREA
                        )
                    ) ?: "",
                    Estado = cursor.getString(cursor.getColumnIndexOrThrow(COL_ESTADO)) ?: "",
                    Fecha = dateFormat.parse(
                        cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                COL_FECHA_TAREA
                            )
                        )
                    ) ?: Date()
                )
                tareas.add(tarea)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return tareas
    }

    // MÉTODO AUXILIAR PARA ELIMINAR REGISTROS
    fun deleteCliente(id: Int): Int {
        val db = writableDatabase
        val result = db.delete(TABLE_CLIENTE, "$COL_ID_CLIENTE = ?", arrayOf(id.toString()))
        db.close()
        return result
    }

    fun deleteLogin(id: Int): Int {
        val db = writableDatabase
        val result = db.delete(TABLE_LOGIN, "$COL_ID_LOGIN = ?", arrayOf(id.toString()))
        db.close()
        return result
    }


    fun deleteTarea(id: Int): Int {
        val db = writableDatabase
        val result = db.delete(TABLE_TAREA, "$COL_TAREA_ID = ?", arrayOf(id.toString()))
        db.close()
        return result
    }


    fun insertarNota(nota: Notas): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITULO_NOTAS, nota.titulo)
            put(COLUMN_PROGRESO_NOTAS, nota.progreso)
        }
        val newRowId = db.insert(TABLE_NOTAS, null, values)
        db.close()
        return newRowId
    }
    fun obtenerTodasLasNotas(): List<Notas> {
        val listaNotas = mutableListOf<Notas>()

        return try {
            val db = readableDatabase
            val cursor = db.rawQuery("SELECT * FROM $TABLE_NOTAS", null)

            cursor.use { c ->
                if (c.moveToFirst()) {
                    do {
                        val id = c.getInt(c.getColumnIndexOrThrow(COLUMN_ID_NOTAS))
                        val titulo = c.getString(c.getColumnIndexOrThrow(COLUMN_TITULO_NOTAS))
                        val progreso = c.getInt(c.getColumnIndexOrThrow(COLUMN_PROGRESO_NOTAS))

                        listaNotas.add(Notas(id, titulo, progreso))
                    } while (c.moveToNext())
                }
            }

            db.close()
            listaNotas

        } catch (e:Exception ) {
            Log.e("DatabaseError", "Error al obtener notas: ${e.message}", e)
            emptyList()
        }
    }
    fun obtenerTodasLasNotas_old(): List<Notas> {
        val listaNotas = mutableListOf<Notas>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NOTAS", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_NOTAS))
                val titulo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITULO_NOTAS))
                val progreso = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PROGRESO_NOTAS))
                listaNotas.add(Notas(id, titulo, progreso))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return listaNotas
    }

    fun actualizarNota(nota: Notas): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITULO_NOTAS, nota.titulo)
            put(COLUMN_PROGRESO_NOTAS, nota.progreso)
        }
        val rowsAffected = db.update(TABLE_NOTAS, values, "$COLUMN_ID_NOTAS = ?", arrayOf(nota.id.toString()))
        db.close()
        return rowsAffected
    }

    fun eliminarNota(id: Int): Int {
        val db = writableDatabase
        val rowsAffected = db.delete(TABLE_NOTAS, "$COLUMN_ID_NOTAS = ?", arrayOf(id.toString()))
        db.close()
        return rowsAffected
    }

}