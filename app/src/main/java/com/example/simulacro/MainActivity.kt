package com.example.simulacro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simulacro.databinding.ActivityMainBinding
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ElAdapter
    private var listaResultados = listOf<Usuario>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.pbDownloading.visibility = View.VISIBLE
        hacerLlamada()

        binding.bChicas.setOnClickListener {
            adapter.actualizarListaUsuarios(listaResultados.filter { it.gender == "female" })
        }
        binding.bChicos.setOnClickListener {
            adapter.actualizarListaUsuarios(listaResultados.filter { it.gender == "male" })
        }
        binding.bTodos.setOnClickListener {
            adapter.actualizarListaUsuarios(listaResultados)
        }
    }

    fun hacerLlamada() {
        val client = OkHttpClient()
        val request = Request.Builder()
        request.url("https://randomuser.me/api/?results=100")

        val call = client.newCall(request.build())
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println(e.toString())
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(this@MainActivity, "Algo ha ido mal", Toast.LENGTH_SHORT).show()
                    binding.pbDownloading.visibility = View.GONE
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { responseBody ->
                    val body = responseBody.string()
                    val gson = Gson()
                    val results = gson.fromJson(body, Results::class.java)

                    CoroutineScope(Dispatchers.Main).launch {
                        binding.pbDownloading.visibility = View.GONE
                        listaResultados = results.results
                        adapter = ElAdapter(listaResultados)
                        binding.recyclerview.adapter = adapter
                    }
                }
            }
        })
    }

}