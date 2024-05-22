package com.example.cryptostadisticsms

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Actividad que se muestra al iniciar la aplicacion y que representaria una portada para
 * la aplicacion
 */
class StartupActivity : AppCompatActivity() {

    private lateinit var topAnim: Animation
    private lateinit var bottomAnim: Animation
    private lateinit var ivImage: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvCreators: TextView

    /**
     * Cuando se crea la vista, se le asigna a los elementos unas animacion y un temporizador
     * para iniciar la MainActivity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_startup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Animaciones
        topAnim = AnimationUtils.loadAnimation(this, R.anim.animation_top)
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.animation_bottom)

        //Guardarlos
        ivImage = findViewById(R.id.ivLogo)
        tvTitle = findViewById(R.id.tvTitle)
        tvCreators = findViewById(R.id.tvCreator)

        //Asignarles las animacion a los elementos
        ivImage.animation = topAnim
        tvTitle.animation = bottomAnim
        tvCreators.animation = bottomAnim

        //Pasar a la actividad principal
        Handler().postDelayed(Runnable {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2500)

    }
}