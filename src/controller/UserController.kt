package com.aridwiprayogo.controller

import com.aridwiprayogo.entity.Users
import com.aridwiprayogo.models.User
import com.aridwiprayogo.models.UserDTO
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class UserController {
    init {
        transaction {
            create(Users)
        }
    }
    private val users = mutableListOf<User>()
    fun getAll(): List<User>{
        transaction {
            Users.selectAll().asSequence().map{ row ->
                users.add(
                    User(
                        id = row[Users.id],
                        firstName = row[Users.firstName],
                        lastName = row[Users.lastName],
                        age = row[Users.age]
                    )
                )
            }
        }
        return users
    }
    fun save(user: UserDTO){
        transaction {
            Users.insert { statement ->
                statement[id] = UUID.randomUUID()
                statement[firstName] = user.firstName
                statement[lastName] = user.lastName
                statement[age] = user.age
            }
        }
    }

    fun update(user: UserDTO, id: UUID){
        transaction {
            Users.update({Users.id eq id}) {statement->
                statement[age] = user.age
                statement[firstName] = user.firstName
                statement[lastName] = user.lastName
            }
        }
    }

    fun delete(id: UUID){
        transaction {
            Users.deleteWhere { Users.id eq id }
        }
    }
}