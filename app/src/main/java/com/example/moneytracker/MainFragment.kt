package com.example.moneytracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        view.findViewById<FloatingActionButton>(R.id.tmpAddWallet).setOnClickListener { onNewWalletButton(it) }
        return view
    }


    private fun onNewWalletButton(view: View?) {
        val walletRecyclerView = view?.findViewById<RecyclerView>(R.id.walletRecyclerView)
        val walletAdapter = walletRecyclerView?.adapter
        walletAdapter
    }
}