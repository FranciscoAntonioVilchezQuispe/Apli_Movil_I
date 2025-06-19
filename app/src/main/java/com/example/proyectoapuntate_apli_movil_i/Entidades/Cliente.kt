package com.example.proyectoapuntate_apli_movil_i.Entidades

import java.util.Date

data class Cliente(
    var IdCliente:Int=0,
    var NombreCompleto:String="",
    var Telefono:String="",
    var Direccion:String="",
    var Correo:String="",
    var Fecha:Date=Date(),

)
