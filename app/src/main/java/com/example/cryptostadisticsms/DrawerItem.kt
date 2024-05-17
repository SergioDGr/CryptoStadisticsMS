package com.example.cryptostadisticsms

import android.view.ViewGroup

abstract class DrawerItem<T : DrawerAdapter.ViewHolder> {
    protected var isChecked: Boolean = false

    abstract fun createViewHolder(parent: ViewGroup): T

    abstract fun bindViewHolder(holder: DrawerAdapter.ViewHolder)

    fun setChecked(isChecked: Boolean): DrawerItem<T> {
        this.isChecked = isChecked
        return this
    }

    fun isChecked(): Boolean {
        return isChecked;
    }

    fun isSelectable(): Boolean {
        return true
    }
}

