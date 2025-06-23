package com.example.proyectoapuntate_apli_movil_i.Entidades

import java.util.Date

data class Cliente(
    var IdCliente:Int=0,
    var Documento:String="",
    var Nombres:String="",
    var Apellidop:String="",
    var Apellidom:String="",
    var Telefono:String="",
    var Direccion:String="",
    var Correo:String="",
    var Fecha:Date=Date(),

)
