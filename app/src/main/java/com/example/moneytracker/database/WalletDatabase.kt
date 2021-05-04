package com.example.moneytracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Wallet::class], version = 1)
abstract class WalletDatabase: RoomDatabase() {
    abstract fun walletDAO(): WalletDAO

    companion object
    {
        private var INSTANCE: WalletDatabase? = null

        fun getInstance(context: Context): WalletDatabase?
        {
            if (INSTANCE == null)
            {
                synchronized(WalletDatabase::class)
                {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        WalletDatabase::class.java, "wallet.db").build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance()
        {
            INSTANCE = null
        }
    }

}