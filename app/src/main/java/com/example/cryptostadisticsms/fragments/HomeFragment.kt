package com.example.cryptostadisticsms.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageView
import android.widget.Toast
import com.example.cryptostadisticsms.CryptoActivity
import com.example.cryptostadisticsms.R
import com.example.cryptostadisticsms.adapters.CryptoAdapter
import com.example.cryptostadisticsms.elements.Crypto

/**
 * Fragment que representa el HOME
 */
class HomeFragment : Fragment() {

    private var lstCryptoItems: ArrayList<Crypto> = ArrayList()
    private lateinit var adapter: CryptoAdapter
    private lateinit var gvCryptos: GridView
    private val criptomonedas = arrayOf(
        "cardano", "binance coin", "xrp", "polkadot", "dogecoin", "avalanche", "chainlink",
        "litecoin", "algorand", "stellar", "vechain", "tezos",
        "cosmos", "filecoin", "theta", "tron", "monero"
    )

    /**
     * Cuando se crea la vista
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    /**
     * Cuando la vista esta creada se inicializa el adapter de las monedas, y se le añade los
     * eventos en question que gestionan la eliminacions, la busqueda y la incializacion de la
     * actividad referente a la moneda.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        gvCryptos = view.findViewById(R.id.gvCryptos)
        //Configurar el adaptador para las GridView de las monedas
        adapter = CryptoAdapter(activity, lstCryptoItems)
        gvCryptos.adapter = adapter

        loadCryptos()
        //Al seleccionar una moneda se enviara al contenid de esa moneda
        gvCryptos.setOnItemClickListener { parent, _, position, _ ->
            val item = parent.getItemAtPosition(position) as Crypto
            val intent = Intent(activity, CryptoActivity::class.java)
            intent.putExtra("Moneda", item)
            startActivity(intent)
        }

        //Al precionar una moneda se hara el evento de si quiere eliminarlo
        gvCryptos.setOnItemLongClickListener { parent, _, position, _ ->
            val item = parent.getItemAtPosition(position) as Crypto
            val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
            builder.setMessage(getString(R.string.del_really_crypto))
                .setTitle(getString(R.string.del_crypto))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    lstCryptoItems.remove(item)
                    adapter.notifyDataSetChanged()
                }
                .setNegativeButton(getString(R.string.no)) { _, _ -> }
            val dialog: AlertDialog = builder.create()
            dialog.show()
            return@setOnItemLongClickListener true
        }

        //El evento para añadir una moneda
        val ivAddCrypro: ImageView? = activity?.findViewById(R.id.ivAddCrypro)
        val etBuscar: EditText? = activity?.findViewById(R.id.etBuscar)
        ivAddCrypro?.setOnClickListener{
            val strSearch = etBuscar?.text.toString()
            if(strSearch.isEmpty()) { //Si esta vacio en el buscador
                Toast.makeText(activity, "No se ha pasado ninguna moneda",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(searchCrypto(strSearch)) { //Si existe la moneda
                lstCryptoItems.add(Crypto(strSearch, 0, generateImage()))
                adapter.notifyDataSetChanged()
                etBuscar?.text?.clear()
            }else{ //en caso contrario
                Toast.makeText(activity, "No se ha encontrado la cryptomoneda",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Genera un fondo para la moneda utilizando un ramdomizador
     */
    private fun generateImage(): Int{
        val num = (1..3).random()
        return when(num){
            1 -> R.drawable.card_1
            2 -> R.drawable.card_2
            else -> R.drawable.card_3
        }
    }

    /**
     * Metodo que se utiliza para la busqueda de la cryptomoneda
     */
    private fun searchCrypto(str: String): Boolean{
        // Recorre el array y comprueba si contiene el nombre buscado
        for (nombre in criptomonedas) {
            if (nombre == str.lowercase()) {
                return true
            }
        }
        return false
    }

    /**
     * Carga las monedas a la lista.
     */
    private fun loadCryptos() {
        //lstCryptos?.add(Crypto("Bitcoin", 1.2))
        lstCryptoItems.add(Crypto("Bitcoin", R.drawable.bitcoin, R.drawable.card_1))
        lstCryptoItems.add(Crypto("Ethereum", R.drawable.ethereum, R.drawable.card_2))
        lstCryptoItems.add(Crypto("Solana", R.drawable.solana, R.drawable.card_3))
    }
}