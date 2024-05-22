package com.example.cryptostadisticsms.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.cryptostadisticsms.databinding.NotificationListItemBinding
import com.example.cryptostadisticsms.elements.Notification

/**
 * Adapter para las notificaciones
 */
class NotificationAdapter(private val context: Context?, private val arrayList: ArrayList<Notification>):
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
        val binding = NotificationListItemBinding.inflate(LayoutInflater.from(parent?.context), parent,
            false)
        var convertView = convertView
        convertView = binding.root

        //Se le asigna el valor de cada campo del elemento, los campos de la vista
        binding.tvMessage.text = arrayList[position].message
        binding.ivCrypoNotification.setImageResource(arrayList[position].image)
        binding.tvPrice.text = arrayList[position].price.toString()

        return convertView;
    }

}