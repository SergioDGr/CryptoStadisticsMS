package com.example.cryptostadisticsms

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView

import androidx.recyclerview.widget.RecyclerView

class DrawerAdapter(private val items: List<DrawerItem<*>>) : RecyclerView.Adapter<DrawerAdapter.ViewHolder>() {

    private val drawerAdapter: DrawerAdapter? = null
    private val viewTypes: MutableMap<Class<out DrawerItem<*>>, Int> = mutableMapOf()
    private val holderFactories: SparseArray<DrawerItem<*>> = SparseArray()

    private val listener: OnItemSelectedListener? = null

    init {
        proccessViewTypes()
    }

    private fun proccessViewTypes(){
        var type = 0

        for (item in items){
            if(!viewTypes.containsKey(item::class.java)){
                viewTypes.put(item::class.java, type)!!
                holderFactories.put(type, item)
                type++
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var holder: ViewHolder = holderFactories.get(viewType).createViewHolder(parent)
        holder.drawerAdapter = this
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        items[position].bindViewHolder(holder)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return viewTypes[items[position]::class.java]!!
    }

    fun setSelected(position: Int){
        var newCheked = items[position]
        if(!newCheked.isSelectable()){
            return
        }

        for(i in items.indices){
            var item = items[i]
            if(item.isChecked()){
                item.setChecked(false)
                notifyItemChanged(i)
                break
            }
        }

        newCheked.setChecked(true)
        notifyItemChanged(position)

        listener?.onItemSelected(position)

    }

    interface OnItemSelectedListener{
        fun onItemSelected(position: Int)
    }

    abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var drawerAdapter: DrawerAdapter? = null

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            drawerAdapter?.setSelected(adapterPosition)
        }

    }
}