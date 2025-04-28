package com.example.comunicacion.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.comunicacion.adapters.AdaptadorProducto
import com.example.comunicacion.databinding.ActivityMainBinding
import com.example.comunicacion.model.Producto
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adaptadorProducto: AdaptadorProducto
    private val mapaGeneros = mutableMapOf<Int, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adaptadorProducto = AdaptadorProducto(this)
        configurarRecycler()

        obtenerGeneros { cargarPeliculas() }

        binding.textoSaludo.text = "Bienvenido"

        //  Botón para ver pantalla de favoritos
        binding.btnFavoritos.setOnClickListener {
            val intent = Intent(this, FavoritosActivity::class.java)
            startActivity(intent)
        }
    }

    private fun configurarRecycler() {
        binding.recyclerModelos.adapter = adaptadorProducto
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.recyclerModelos.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.recyclerModelos.layoutManager = LinearLayoutManager(this)
        }
    }

    private fun obtenerGeneros(callback: () -> Unit) {
        val apiKey = "de8a8ac518b9684856210092a20e74a9"
        val url = "https://api.themoviedb.org/3/genre/movie/list?api_key=$apiKey&language=es-ES"

        val peticion = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val generos = response.getJSONArray("genres")
                for (i in 0 until generos.length()) {
                    val genero = generos.getJSONObject(i)
                    mapaGeneros[genero.getInt("id")] = genero.getString("name")
                }
                callback()
            },
            { error ->
                Log.e("GENERO_API", "❌ Error al obtener géneros: ${error.message}")
                Snackbar.make(binding.root, "Error cargando géneros", Snackbar.LENGTH_LONG).show()
            })

        Volley.newRequestQueue(this).add(peticion)
    }

    private fun cargarPeliculas() {
        val apiKey = "de8a8ac518b9684856210092a20e74a9"
        val url = "https://api.themoviedb.org/3/movie/popular?api_key=$apiKey&language=es-ES&page=1"

        val peticion = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val peliculas = response.getJSONArray("results")
                Log.d("PELIS_API", "🎥 Recibidas: ${peliculas.length()} películas")

                for (i in 0 until peliculas.length()) {
                    val movieJSON = peliculas.getJSONObject(i)

                    val precioRandom = (5..20).random() + listOf(0.0, 0.49, 0.99).random()
                    val rating = movieJSON.getDouble("vote_average") / 2.0

                    val genreIds = movieJSON.getJSONArray("genre_ids")
                    val primerGenero = if (genreIds.length() > 0) {
                        val idGenero = genreIds.getInt(0)
                        mapaGeneros[idGenero] ?: "Desconocido"
                    } else "Desconocido"

                    val producto = Producto(
                        id = movieJSON.getInt("id"),
                        title = movieJSON.getString("title"),
                        description = movieJSON.getString("overview"),
                        thumbnail = "https://image.tmdb.org/t/p/w500${movieJSON.getString("poster_path")}",
                        category = "Película",
                        price = precioRandom,
                        rating = rating,
                        genero = primerGenero
                    )

                    adaptadorProducto.addProducto(producto)
                    Log.d("PELIS_API", "🛒 Agregado: ${producto.title} - $${producto.price} - ⭐ $rating - 🎭 $primerGenero")
                }
            },
            { error ->
                Log.e("PELIS_API", "❌ Error al cargar películas: ${error.message}")
                Snackbar.make(binding.root, "Error al cargar películas", Snackbar.LENGTH_LONG).show()
            })

        Volley.newRequestQueue(this).add(peticion)
    }
}
