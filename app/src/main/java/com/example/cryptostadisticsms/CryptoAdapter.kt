package com.example.cryptostadisticsms

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.cryptostadisticsms.databinding.CryptoListItemBinding

class CryptoAdapter(private val context: Context, private val arrayList: ArrayList<CryptoItem>):
    BaseAdapter(){
    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(position: Int): Any {
        return arrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = CryptoListItemBinding.inflate(LayoutInflater.from(parent?.context), parent,
            false)
        var convertView = convertView
        convertView = binding.root

        binding.tvName.text = arrayList[position].name
        binding.ivImage.setImageResource(arrayList[position].image)
        binding.rlCrypto.setBackgroundResource(arrayList[position].background)

        return convertView;
    }

}