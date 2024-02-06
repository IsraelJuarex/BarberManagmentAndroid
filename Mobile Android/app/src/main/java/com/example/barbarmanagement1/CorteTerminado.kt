package com.example.barbarmanagement1

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class CorteTerminado : AppCompatActivity() {
    val preciosServ : MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.corte_terminado)
        val username = intent.getStringExtra("usuario")

        getPrecios()

        val botonfinalizarcorte = findViewById<Button>(R.id.botonPopupFinalizar)
        botonfinalizarcorte.setOnClickListener {
            val checkCorte = findViewById<CheckBox>(R.id.checkCorte)
            val checkceja = findViewById<CheckBox>(R.id.checkCeja)
            val checkBarba = findViewById<CheckBox>(R.id.checkBarba)
            val loading = findViewById<RelativeLayout>(R.id.loadingPanel)
            val total = findViewById<TextView>(R.id.labelcobrototal)

            if (isNetworkAvailable()) {
                if (!checkBarba.isChecked && !checkceja.isChecked && !checkCorte.isChecked) {
                    Toast.makeText(this, "Por favor ingresa al menos una opción", Toast.LENGTH_SHORT)
                        .show()
                } else {

                    if (checkCorte.isChecked) {
                        if (username != null) {
                            updateServicios(username, "cortes")
                        }
                    }
                    if (checkceja.isChecked) {
                        if (username != null) {
                            updateServicios(username, "cejas")
                        }
                    }
                    if (checkBarba.isChecked) {
                        if (username != null) {
                            updateServicios(username, "barbas")
                        }
                    }
                    if (username != null) {
                        updateTotal(username, total.text)
                    }
                    loading.visibility = View.VISIBLE
                    Handler().postDelayed({
                        val intent2 = Intent(this, MainPage::class.java)
                        intent2.putExtra("usuario",username)
                        startActivity(intent2)
                        finish()
                    },
                        2000)
                }
            }


            else {
                Toast.makeText(this, "No hay conexión a internet", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun getPrecios() {
        val queue = Volley.newRequestQueue(this)
        val url = "https://bandoisrael.000webhostapp.com//getPrecios.php"

        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            { response ->
                val jsonArray = JSONArray(response)
                jsonArray[0]

                for (i in 0.. jsonArray. length() -1){
                    val jo = jsonArray.getJSONObject(i)
                    val servicio = jo.get("Servicio").toString()
                    val precio = jo.get("Precio").toString()
                    preciosServ.add(i,precio)
                }

            },
            { error ->
                Toast.makeText(this,"error", Toast.LENGTH_LONG).show()
            })
        queue.add(stringRequest)
    }


    override fun onBackPressed() {
    }

    private fun updateTotal(username: String, total: CharSequence) {
        val queue = Volley.newRequestQueue(this)
        val url = "https://bandoisrael.000webhostapp.com//updateBarberTotal.php?usuario=$username&total=$total"

        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            { response ->

            },
            { error ->
                Toast.makeText(this,"error", Toast.LENGTH_LONG).show()
            })
        queue.add(stringRequest)

    }

    private fun updateServicios(username: String, servicio: CharSequence) {
        val queue = Volley.newRequestQueue(this)
        val url = "https://bandoisrael.000webhostapp.com//updateBarber.php?usuario=$username&servicio=$servicio"

        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            { response ->

            },
            { error ->
                Toast.makeText(this,"No hay conexion a internet ", Toast.LENGTH_LONG).show()
            })

        queue.add(stringRequest)

    }


    fun onCheckboxClicked(view: View) {
        val labelcobrocorte = findViewById<TextView>(R.id.labelcobrocorte)
        val labelcobroceja = findViewById<TextView>(R.id.labelcobroceja)
        val labelcobrobarba = findViewById<TextView>(R.id.labelcobrobarba)
        val labelcambio = findViewById<TextView>(R.id.labelcobrocambio)
        val labeltotal = findViewById<TextView>(R.id.labelcobrototal)
        val txtpago = findViewById<EditText>(R.id.edittextpago)

        if (view is CheckBox) {
            val checked: Boolean = view.isChecked
            var total = 0
            when (view.id) {

                R.id.checkCorte -> {
                    labelcobrocorte?.let {
                        if (checked && preciosServ.isNotEmpty()) {
                            labelcobrocorte.text =  preciosServ.get(0)

                            total += Integer.parseInt(labelcobrobarba.text.toString()) + Integer.parseInt(labelcobroceja.text.toString()) + Integer.parseInt(labelcobrocorte.text.toString())
                            labeltotal.text=total.toString()
                        } else {
                            labelcobrocorte.text = "0"
                            total += Integer.parseInt(labelcobrobarba.text.toString()) + Integer.parseInt(labelcobroceja.text.toString()) + Integer.parseInt(labelcobrocorte.text.toString())
                            labeltotal.text=total.toString()
                        }

                    }
                }

                R.id.checkCeja -> {
                    labelcobroceja?.let {

                        if (checked&& preciosServ.isNotEmpty()) {
                            labelcobroceja.text =  preciosServ.get(2)
                            total += Integer.parseInt(labelcobrobarba.text.toString()) + Integer.parseInt(labelcobroceja.text.toString()) + Integer.parseInt(labelcobrocorte.text.toString())
                            labeltotal.text=total.toString()
                        } else {
                            labelcobroceja.text = "0"
                            total += Integer.parseInt(labelcobrobarba.text.toString()) + Integer.parseInt(labelcobroceja.text.toString()) + Integer.parseInt(labelcobrocorte.text.toString())
                            labeltotal.text=total.toString()
                        }
                    }
                }

                R.id.checkBarba -> {
                    labelcobrobarba?.let{
                        if (checked && preciosServ.isNotEmpty()) {
                            labelcobrobarba.text =  preciosServ.get(1)
                            total += Integer.parseInt(labelcobrobarba.text.toString()) + Integer.parseInt(labelcobroceja.text.toString()) + Integer.parseInt(labelcobrocorte.text.toString())
                            labeltotal.text=total.toString()
                        } else {
                            labelcobrobarba.text = "0"
                            total += Integer.parseInt(labelcobrobarba.text.toString()) + Integer.parseInt(labelcobroceja.text.toString()) + Integer.parseInt(labelcobrocorte.text.toString())
                            labeltotal.text=total.toString()
                        }
                    }
                }
            }
            // Convierte el ingreso y calcula el cambio cuando se ingresa un valor
            txtpago.doAfterTextChanged {
                val input = txtpago?.text.toString().trim()
                if (!input.isNullOrBlank()) {
                    try {
                        val inputValue = Integer.parseInt(input)
                        labelcambio.text = ((total - inputValue) * -1).toString()
                    } catch (e: NumberFormatException) {
                        // Handle the case where input is not a valid integer
                        labelcambio.text = "0"
                    }
                } else {
                    labelcambio.text = "0"
                }
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }
}