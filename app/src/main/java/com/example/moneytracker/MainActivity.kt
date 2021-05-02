package com.example.moneytracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val walletRecyclerView = findViewById<RecyclerView>(R.id.walletRecyclerView)
        walletRecyclerView.layoutManager = LinearLayoutManager(this)
        //walletRecyclerView.adapter = WalletRecyclerAdapter(listOf("e_wallet_1", "e_wallet_2", "wallet_3"))
        walletRecyclerView.adapter = WalletRecyclerAdapter(generateData())
    }


    fun generateData(): List<String>{
        val data: MutableList<String> = ArrayList()
        for(i in 1..10){
            data.add("wallet_" + i.toString())
        }
        return data
    }
}