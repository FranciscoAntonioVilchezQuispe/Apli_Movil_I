package com.example.proyectoapuntate_apli_movil_i.Entidades

import java.util.Date

data class Apunte(
    var ApunteId:Int=0,
    var ClienteId:Int=0,
    var Titulo:String="",
    var Descripcion:String="",
    var Porcentaje:Double=0.0,
    var Fecha: Date =Date()

)
