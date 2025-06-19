package com.example.proyectoapuntate_apli_movil_i.Entidades

import java.util.Date

data class Tarea(
    var ApunteId:Int=0,
    var TareaId:Int=0,
    var Titulo:String="",
    var Descripcion:String="",
    var Estado:String="",
    var Fecha: Date = Date()
)
