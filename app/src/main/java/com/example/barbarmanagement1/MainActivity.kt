package com.example.barbarmanagement1

import android.content.Intent
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import com.android.volley.Request

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val txtuser = findViewById<EditText>(R.id.txtusuario)
        val txtpass = findViewById<EditText>(R.id.txtpassword)
        val botoninciarsesion = findViewById<Button>(R.id.login)

        val username = txtuser.text
        val password = txtpass.text

        //remove toggle transparent status bar
        //window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        //Run Background video in loop and muted

        backgroundvideo()

        //Botones
        botoninciarsesion.setOnClickListener{
            if (TextUtils.isEmpty(username))
            {
                Toast.makeText(this, "Porfavor ingresar usuario", Toast.LENGTH_SHORT).show()
            }
            else if (TextUtils.isEmpty(password))
            {
                Toast.makeText(this, "Porfavor ingresar Contraseña", Toast.LENGTH_SHORT).show()
            }
            else {
                getValidUser(username,password)
            }
        }
    }

    // Disable back button
    override fun onBackPressed() {
    }



    fun updateStatus(username: String, status: String) {
        val queue = Volley.newRequestQueue(this)
        val url = "https://bandoisrael.000webhostapp.com/updateStatus.php?usuario=$username&status=$status"

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

    // Database connection
    private fun getValidUser(username: Editable , password:Editable) {
        val queue = Volley.newRequestQueue(this)
        val builder = Uri.parse("https://bandoisrael.000webhostapp.com/getUser2.php")
            .buildUpon()
            .appendQueryParameter("usuario", username.toString())
            .appendQueryParameter("pass", password.toString())
        val url = builder.build().toString()

        val stringRequest = StringRequest(Request.Method.GET,
            url,
            { response ->
                val jsonArray = JSONArray(response)
                jsonArray[0]

                for (i in 0 until jsonArray. length()){
                    val jo = jsonArray.getJSONObject(i)
                    val user = jo.get("usuario").toString()
                    val contrasena = jo.get("contrasena").toString()
                    val tipoCuenta = jo.get("admin").toString()
                    //si usuario no es Administrador
                    if (username.contentEquals(user) && password.contentEquals(contrasena) && tipoCuenta == "0"){
                        updateStatus(user,"Online")
                        val intent = Intent(this, MainPage::class.java)
                        intent.putExtra("usuario",user)
                        intent.putExtra("pass",contrasena)
                        startActivity(intent)
                        finish()
                    }
                    //si usuario es Administrador
                    else if (username.contentEquals(user) && password.contentEquals(contrasena) && tipoCuenta == "1"){
                        val intent = Intent(this, AdminMainPage::class.java)
                        intent.putExtra("usuario",user)
                        intent.putExtra("pass",contrasena)

                        startActivity(intent)
                        finish()
                    }


                        else{
                        Toast.makeText(this, "usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            { error ->
                Toast.makeText(this,"No hay conexión a internet", Toast.LENGTH_LONG).show()
            })
        queue.add(stringRequest)

    }


    private fun  backgroundvideo(){
        var simpleVideoView: VideoView?
        simpleVideoView = findViewById<View>(R.id.videoView) as VideoView
        simpleVideoView!!.setVideoURI(
            Uri.parse(
                "android.resource://"
                        + packageName + "/" + R.raw.bg_video3
            )
        )
        simpleVideoView!!.requestFocus()
        // starting the video
        simpleVideoView!!.start()
        // Set Video loop and audio mute
        simpleVideoView.setOnPreparedListener(OnPreparedListener { mp ->
            mp.setVolume(0f, 0f)
            mp.isLooping = true
        })
    }
}