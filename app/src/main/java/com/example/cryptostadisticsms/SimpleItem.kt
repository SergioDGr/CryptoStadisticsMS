package com.example.cryptostadisticsms

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class SimpleItem(icon: Drawable, title: String) : DrawerItem<SimpleItem.ViewHolder>() {

    var selectedItemIconTint:Int = -1
    var selectedItemTextTint:Int = -1

    var normalItemIconTint:Int = -1
    var normalItemTextTint:Int = -1



    class ViewHolder(itemView: View) : DrawerAdapter.ViewHolder(itemView) {

        private var icon: ImageView;
        private var title: TextView;

        init {
            icon = itemView.findViewById(R.id.ivIcon)
            title = itemView.findViewById(R.id.tvTitle)
        }

    }

    override fun createViewHolder(parent: ViewGroup): ViewHolder {
        var inflater = LayoutInflater.from(parent.context)
        var v = inflater()
    }

    override fun bindViewHolder(holder: DrawerAdapter.ViewHolder) {
        TODO("Not yet implemented")
    }

}