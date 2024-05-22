package com.example.cryptostadisticsms.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.example.cryptostadisticsms.R
import com.example.cryptostadisticsms.adapters.NotificationAdapter
import com.example.cryptostadisticsms.elements.Notification

/**
 * Fragment que representa NOTIFICACION
 */
class NotificationFragment : Fragment() {

    private var lstNotificationItems: ArrayList<Notification> = ArrayList()
    private lateinit var adapter: NotificationAdapter
    private lateinit var lvNotifications: ListView

    /**
     * Crea la vista del Fragment
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    /**
     * Cuando se crea la vista inicializamos el adaptar y la lista de las notifiaciones
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lvNotifications = view.findViewById(R.id.lvNotifications)
        adapter = NotificationAdapter(activity, lstNotificationItems)
        lvNotifications.adapter = adapter

        loadNotifications()
    }

    /**
     * Carga la lista con las notificaciones
     */
    private fun loadNotifications(){

    }

}