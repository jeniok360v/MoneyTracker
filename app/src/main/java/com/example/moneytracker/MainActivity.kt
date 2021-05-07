package com.example.moneytracker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moneytracker.database.Wallet
import com.example.moneytracker.database.WalletDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton
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

    override fun onStart() {
        super.onStart()
        val tmpAddWallet = findViewById<FloatingActionButton>(R.id.tmpAddWallet)
        tmpAddWallet.setOnClickListener{

        }
    }


/*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        btnAdd.setOnClickListener {
            val dao = EventDatabase.getInstance(this.context!!)?.eventDao()
            GlobalScope.launch {
                dao?.getAll()
            }
            listener?.goToNewEventFragment()
        }
        walletRecyclerView.layoutManager = LinearLayoutManager(activity)
        walletRecyclerView.itemAnimator = DefaultItemAnimator()
        walletRecyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
    }
 */

    fun generateData(): List<String>{
        val data: MutableList<String> = ArrayList()
        data.add("todo gradient colors")
        for(i in 1..10){
            data.add("wallet_" + i.toString())
        }
        return data
    }
}