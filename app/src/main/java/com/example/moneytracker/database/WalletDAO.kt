package com.example.moneytracker.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface WalletDAO {

    @Query("select * from wallet")
    fun getAll(): List<Wallet>

    @Insert
    fun insertAll(vararg wallet : Wallet)

}