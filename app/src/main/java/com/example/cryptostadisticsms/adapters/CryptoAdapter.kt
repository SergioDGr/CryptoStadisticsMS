package com.example.cryptostadisticsms.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.cryptostadisticsms.databinding.CryptoListItemBinding
import com.example.cryptostadisticsms.elements.Crypto

/**
 * Adapter para las cryptomonedas
 */
class CryptoAdapter(private val context: Context?, private val arrayList: ArrayList<Crypto>):
    BaseAdapter(){

    /**
     * Consigue el tamaño del array
     */
    override fun getCount(): Int {
        return arrayList.size
    }

    /**
     * Consigue el elemento del array por la posicion
     */
    override fun getItem(position: Int): Any {
        return arrayList[position]
    }

    /**
     * Consigue el identificador por la posicion
     */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * Consegui la vista con los campos rellenados
     */
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val binding = CryptoListItemBinding.inflate(LayoutInflater.from(parent?.context), parent,
            false)
        var convertView = convertView
        convertView = binding.root

        //Se le asigna el valor de cada campo del elemento, los campos de la vista
        binding.tvName.text = arrayList[position].name
        binding.ivImage.setImageResource(arrayList[position].image)
        binding.rlCrypto.setBackgroundResource(arrayList[position].background)

        return convertView;
    }

}