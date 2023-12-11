package com.example.barbarmanagement1
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import de.codecrafters.tableview.TableView
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Timer
import java.util.TimerTask


class AdminMainPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_main_page)
        val botonreset = findViewById<Button>(R.id.botonReset)
        val botoncerrar = findViewById<Button>(R.id.botonCerrar)

        val mainHandler = Handler(Looper.getMainLooper())
        val usuario = intent.getStringExtra("usuario")
        val pass = intent.getStringExtra("pass")



        mainHandler.post(object : Runnable {
            override fun run() {

                if (usuario != null) {
                    if (pass != null) {
                        getAllData(usuario,pass)
                    }
                }
                mainHandler.postDelayed(this, 5000)
            }
        })

        botonreset.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Resetear datos?")
                    .setCancelable(false)
                    .setPositiveButton("Si") { dialog, id ->
                        val queue = Volley.newRequestQueue(this)
                        val url = "https://bandoisrael.000webhostapp.com//resetData.php"
                        val stringRequest = StringRequest(
                            Request.Method.GET,
                            url,
                            { response ->

                            },
                            { error ->
                                Toast.makeText(this,"Error no hay conexion a internet", Toast.LENGTH_LONG).show()
                            })
                        queue.add(stringRequest)
                    }
                    .setNegativeButton("No") { dialog, id ->
                        // Dismiss the dialog
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }

        botoncerrar.setOnClickListener {
            finish()
        }


        val timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                updateTimer()
            }
        }, 0, 1000)
    }

    private fun getAllData(usuario: String, pass: String) {
        val queue = Volley.newRequestQueue(this)
        val builder = Uri.parse("https://bandoisrael.000webhostapp.com/getAllData2.php")
            .buildUpon()
            .appendQueryParameter("usuario", usuario)
            .appendQueryParameter("pass", pass)
        val url = builder.build().toString()
        val seqList: MutableList<MutableList<String>> = ArrayList()
        val TABLE_HEADERS = arrayOf("Status","Barber", "Cortes", "Barbas", "Cejas","Total")

        val cortesBarber = findViewById<TextView>(R.id.cortesBarber)
        val cejasBarber = findViewById<TextView>(R.id.cejasBarber)
        val barbasBarber = findViewById<TextView>(R.id.barbasBarber)
        val totalBarber = findViewById<TextView>(R.id.totalBarber)
        var tocrtes = 0
        var tcejas = 0
        var tbarbas = 0
        var ttotal = 0
        val stringRequest = StringRequest(Request.Method.GET,
            url,
            { response ->
                val jsonArray = JSONArray(response)
                jsonArray[0]

                for (i in 0 until jsonArray.length()) {
                    val jo = jsonArray.getJSONObject(i)
                    val alias = jo.get("alias").toString()
                    val cortes = jo.get("cortes").toString()
                    val barbas = jo.get("barbas").toString()
                    val cejas = jo.get("cejas").toString()
                    val total = jo.get("total").toString()
                    val status = jo.get("status").toString()

                    tocrtes = tocrtes + cortes.toInt()
                    tcejas = tcejas + cejas.toInt()
                    tbarbas = tbarbas + barbas.toInt()
                    ttotal = ttotal + total.toInt()

                    cortesBarber.text = tocrtes.toString()
                    cejasBarber.text = tcejas.toString()
                    barbasBarber.text = tbarbas.toString()
                    totalBarber.text = "$"+ ttotal.toString()

                    seqList.add(mutableListOf(status,alias,cortes,barbas,cejas,total))
                }

                val arrayData = seqList.map { it.toTypedArray() }.toTypedArray()

                val tableView = findViewById<View>(R.id.tableView) as TableView<Array<String>>
                tableView.headerAdapter = SimpleTableHeaderAdapter(this, *TABLE_HEADERS)
                tableView.setDataAdapter(SimpleTableDataAdapter(this, arrayData))
            },
            { error ->
                Toast.makeText(this, "Error a conectar al la base de datos, Verifique su connection a internet", Toast.LENGTH_SHORT)
                    .show()
            })
        queue.add(stringRequest)
    }

    private fun updateTimer() {
        val curtime = findViewById<TextView>(R.id.clock_text_view2)

        runOnUiThread {
            curtime.text = SimpleDateFormat("EEEE, MMMM dd, yyyy, hh:mm:ss aaa").format(Date())
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }
}