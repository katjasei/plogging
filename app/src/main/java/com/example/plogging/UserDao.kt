package com.example.plogging

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User): Long

    @Delete
    fun delete(user:User)

    @Update
    fun update(user: User)

    @Query("SELECT * FROM user WHERE user.uid = :id")
    fun getById(id:Long): User

    @Query("SELECT * FROM user WHERE username = :username AND password = :password")
    fun getByUserNameAndPassword(username: String, password:String): List<User>

    @Query("SELECT * FROM user WHERE username = :username")
    fun checkIfUserNameExist(username: String): List<User>

    @Query("SELECT * FROM user WHERE email = :email")
    fun checkIfUserEmailExist(email: String): List<User>

}