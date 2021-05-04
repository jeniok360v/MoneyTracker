package com.example.moneytracker.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "wallet")
data class Wallet (
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id : Long = 0,
    @ColumnInfo(name = "name") var name : String
)
