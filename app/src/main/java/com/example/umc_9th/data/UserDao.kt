//package com.example.umc_9th.data
//
//import androidx.room.*
//
//@Dao
//interface UserDao {
//    @Insert
//    fun insert(user: User)
//
//    @Query("SELECT * FROM User_table WHERE id = :userId AND password = :userPw")
//    fun getUser(userId: String, userPw: String): User?
//
//}