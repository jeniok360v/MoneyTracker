package com.example.moneytracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moneytracker.database.Wallet
import com.example.moneytracker.database.WalletDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var db : WalletDatabase//? = WalletDatabase.getInstance(applicationContext)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //db test
        GlobalScope.launch {
            try{
                db = WalletDatabase.getInstance(applicationContext)!!
            }catch(e: Exception) {
                Log.d("wallet_insert", e.message.toString())
            }

            val s = Wallet(0, "testString")
            db.walletDAO().insertAll(s)
        }
        val walletRecyclerView = findViewById<RecyclerView>(R.id.walletRecyclerView)
        walletRecyclerView.layoutManager = LinearLayoutManager(this)
        //walletRecyclerView.adapter = WalletRecyclerAdapter(listOf("e_wallet_1", "e_wallet_2", "wallet_3"))
        walletRecyclerView.adapter = WalletRecyclerAdapter(generateData())

    }


    fun generateData(): List<String>{
        val data: MutableList<String> = ArrayList()
        data.add("todo gradient colors")
        for(i in 1..10){
            data.add("wallet_" + i.toString())
        }
        return data
    }
}