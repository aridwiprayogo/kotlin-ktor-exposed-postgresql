package com.aridwiprayogo.entity

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.util.*

object Users : Table() {
    val id        : Column<UUID>   = uuid(name = "id")
    val firstName : Column<String> = text(name = "first_name")
    val lastName  : Column<String> = text(name = "last_name")
    val age       : Column<Int>    = integer(name = "age")
    override val primaryKey = PrimaryKey(id)
}