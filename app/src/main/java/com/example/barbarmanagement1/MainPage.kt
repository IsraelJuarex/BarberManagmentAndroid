package com.example.barbarmanagement1

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Timer
import java.util.TimerTask


class MainPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)


        val username = intent.getStringExtra("usuario")

        if (username != null) {
            getdata(username)
        }

        val botoninicio = findViewById<Button>(R.id.startButton)
        val botonstop = findViewById<Button>(R.id.stopButton)
        val botoncerrarSesion = findViewById<Button>(R.id.cerrarSesionBoton)
        val chronometer = findViewById<Chronometer>(R.id.chronometer)
        val curtime = findViewById<TextView>(R.id.clock_text_view)

        val timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                updateTimer()
            }
        }, 0, 1000)


        //chronometro
        chronometer.onChronometerTickListener = Chronometer.OnChronometerTickListener { cArg ->
            val time = SystemClock.elapsedRealtime() - cArg.base
            val h = (time / 3600000).toInt()
            val m = (time - h * 3600000).toInt() / 60000
            val s = (time - h * 3600000 - m * 60000).toInt() / 1000
            val hh = if (h < 10) "0$h" else h.toString() + ""
            val mm = if (m < 10) "0$m" else m.toString() + ""
            val ss = if (s < 10) "0$s" else s.toString() + ""
            cArg.text = "$hh:$mm:$ss"
        }

        if (isNetworkAvailable()) {

            botoncerrarSesion.setOnClickListener {
                val builder3 = AlertDialog.Builder(this)
                builder3.setMessage("Terminar turno y cerrar programa")
                    .setCancelable(true)

                    //Condicion si termina corte

                    .setPositiveButton("Si") { dialog, id ->
                        if (username != null) {
                            updateStatus(username, "Offline")
                        }
                        finish()

                    }
                    .setNegativeButton("No") { dialog, id ->
                        dialog.dismiss()
                    }
                val alert = builder3.create()
                alert.show()
            }

            //Boton iniciar corte
            botoninicio.setOnClickListener {

                val builder = AlertDialog.Builder(this)
                builder.setMessage("Empezar corte?")
                    .setCancelable(false)

                    .setPositiveButton("Si") { dialog, id ->

                        if (isNetworkAvailable()) {
                            chronometer.base = SystemClock.elapsedRealtime()
                            chronometer.format = "%s"
                            chronometer?.isVisible = true
                            chronometer.start()
                            botoninicio?.isEnabled = false
                            botoninicio.text = "Cortando"
                            botonstop?.isVisible = true
                            botoncerrarSesion.isEnabled = false
                            if (username != null) {
                                updateStatus(username, "Cortando")
                            }
                        }

                        else {
                            Toast.makeText(this, "No hay conexión a internet", Toast.LENGTH_SHORT).show()
                        }




                    }
                    .setNegativeButton("No") { dialog, id ->
                        // Dismiss the dialog
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }


            botonstop.setOnClickListener {

                val builder = AlertDialog.Builder(this)
                builder.setMessage("Terminar corte?")
                    .setCancelable(false)

                    //Condicion si termina corte

                    .setPositiveButton("Si") { dialog, id ->

                        if (isNetworkAvailable()) {
                            if (username != null) {
                                updateStatus(username, "Online")
                            }
                            botoncerrarSesion.isEnabled = true

                            val intent2 = Intent(this, CorteTerminado::class.java)
                            intent2.putExtra("usuario", username)
                            startActivity(intent2)
                            finish()
                            chronometer!!.stop()
                            var text = chronometer.text.toString()
                            botonstop!!.isVisible = false
                            botoninicio!!.isEnabled = true
                            chronometer.base = SystemClock.elapsedRealtime()
                            chronometer.isVisible = false
                            botoninicio.text = "Iniciar Corte"
                        }

                        else {
                            Toast.makeText(this, "No hay conexión a internet", Toast.LENGTH_SHORT).show()
                        }

                    }
                    .setNegativeButton("No") { dialog, id ->
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }

        } else {
            Toast.makeText(this, "No hay conexión a internet", Toast.LENGTH_SHORT).show()
        }
    }



    fun updateStatus(username: String, status: String) {
        val queue = Volley.newRequestQueue(this)
        val url = "https://bandoisrael.000webhostapp.com//updateStatus.php?usuario=$username&status=$status"

        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            { response ->

            },
            { error ->
                Toast.makeText(this,"No hay conexión a internet ", Toast.LENGTH_LONG).show()
            })
        queue.add(stringRequest)

    }
    private fun updateTimer() {
        val curtime = findViewById<TextView>(R.id.clock_text_view)

        runOnUiThread {
            curtime.text = SimpleDateFormat("EEEE, MMMM dd, yyyy, hh:mm:ss aaa").format(Date())
        }
    }

    private fun getdata(username: String) {
        val queue = Volley.newRequestQueue(this)
        val url = "https://bandoisrael.000webhostapp.com//getUser.php?usuario=$username"

        val nameBarber = findViewById<TextView>(R.id.barberName)
        val cortesBarber = findViewById<TextView>(R.id.cortesBarber)
        val cejasBarber = findViewById<TextView>(R.id.cejasBarber)
        val barbasBarber = findViewById<TextView>(R.id.barbasBarber)
        val totalBarber = findViewById<TextView>(R.id.totalBarber)

        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            { response ->
                val jsonArray = JSONArray(response)
                jsonArray[0]

                for (i in 0.. jsonArray. length() -1){
                    val jo = jsonArray.getJSONObject(i)
                    val user = jo.get("usuario").toString()
                    val nombre = jo.get("nombre").toString()
                    val alias = jo.get("alias").toString()
                    val cortes = jo.get("cortes").toString()
                    val barbas = jo.get("barbas").toString()
                    val cejas = jo.get("cejas").toString()
                    val total = jo.get("total").toString()
                    val admin = jo.get("admin").toString()
                    val status = jo.get("status").toString()
                    val estacion = jo.get("estacion").toString()
                    nameBarber.text = "Barber@ " + alias
                    cortesBarber.text = cortes
                    cejasBarber.text = cejas
                    barbasBarber.text = barbas
                    totalBarber.text = "$"+ total

                }
            },
            { error ->
                Toast.makeText(this,"No hay conexión a internet", Toast.LENGTH_LONG).show()
            })
        queue.add(stringRequest)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }


    override fun onBackPressed() {
    }

    }



