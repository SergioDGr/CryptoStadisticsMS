package com.example.cryptostadisticsms

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View

import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.GravityCompat

import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform

import com.example.cryptostadisticsms.databinding.ActivityMainBinding
import com.example.cryptostadisticsms.fragments.HomeFragment
import com.example.cryptostadisticsms.fragments.NotificationFragment
import com.google.android.material.navigation.NavigationView

/**
 * Actividad principal donde se gestiona toda la logica
 */
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("MissingPermission", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Conseguir el binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Pedir permisos para la notificacion
        askForNotificationPermission()

        //Inicializa el menu
        initMenu()

        //Inicializa el fragment inicial, HOME
        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction().replace(R.id.flContainer,
                HomeFragment()
            ).commit()
        }
    }

    /**
     * Pedir al usuario el permiso de notificacion
     */
    private fun askForNotificationPermission(){
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ){ isGranted ->
            if(!isGranted){ //El usuario denegó el permiso se cierra la aplicacion
                finish()
            }
        }
        if(checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    /**
     * Inicializa el menu con un configuracion en concreto.
     */
    private fun initMenu(){
        val navView =  binding.navigationView
        navView.bringToFront() //Trae mas a delate el vista de navigacion
        navView.setNavigationItemSelectedListener(this) //Se le añade el evento de seleccion de item
        navView.setCheckedItem(R.id.navHome) //Se marca el item HOME como por defecto

        val drawView = binding.dlMain
        val navBar = binding.ivNavBar
        navBar.setOnClickListener {//Al darle abre o cierra vista de la navegacion
            if(drawView.isDrawerVisible(GravityCompat.START)){
                drawView.closeDrawer(GravityCompat.START)
            }else
                drawView.openDrawer(GravityCompat.START)
        }

    }

    /**
     *  Al selecciona uno de los elementos del menu muentra un contenido en concreto
     *  o termina la actividad.
     */
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId){
            R.id.navHome -> { //Muestra el HomeFragment
                binding.ivAddCrypro.visibility = View.VISIBLE
                binding.etBuscar.visibility = View.VISIBLE
                binding.tvMessageTitle.visibility = View.GONE
                supportFragmentManager.beginTransaction().replace(R.id.flContainer,
                    HomeFragment(), ).commit()
            }
            R.id.navMessages -> { //Muestra el NotificationFragment
                binding.ivAddCrypro.visibility = View.GONE
                binding.etBuscar.visibility = View.GONE
                binding.tvMessageTitle.visibility = View.VISIBLE
                supportFragmentManager.beginTransaction().replace(R.id.flContainer,
                    NotificationFragment(), ).commit()
            }
            R.id.navAboutUS -> { //Dialog de informacion
                val mDialog = Dialog(this)
                mDialog.setContentView(R.layout.information_popup)
                mDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mDialog.show()
            }
            R.id.navExit -> { finish() } //Termina la actividad
        }

        //Cierra el menu
        val drawView = binding.dlMain
        drawView.closeDrawer(GravityCompat.START)
        return true
    }

}