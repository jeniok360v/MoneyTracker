package com.example.moneytracker.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Wallet::class], version = 1)
abstract class WalletDatabase: RoomDatabase() {
    abstract fun walletDAO(): WalletDAO
}