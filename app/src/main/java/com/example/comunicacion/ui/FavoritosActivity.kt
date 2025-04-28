package com.example.comunicacion.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.comunicacion.adapters.AdaptadorProducto
import com.example.comunicacion.databinding.ActivityFavoritosBinding
import com.example.comunicacion.model.Producto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FavoritosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritosBinding
    private lateinit var adaptador: AdaptadorProducto
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        adaptador = AdaptadorProducto(this)

        binding.recyclerFavoritos.layoutManager = LinearLayoutManager(this)
        binding.recyclerFavoritos.adapter = adaptador

        binding.btnVolver.setOnClickListener {
            finish()
        }

        cargarFavoritos()
    }

    private fun cargarFavoritos() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        firestore.collection("usuarios")
            .document(userId)
            .collection("favoritos")
            .get()
            .addOnSuccessListener { documentos ->
                for (doc in documentos) {
                    val producto = Producto(
                        id = 0, // Opcional si no lo tienes
                        title = doc.getString("titulo") ?: "",
                        description = doc.getString("descripcion") ?: "",
                        thumbnail = doc.getString("imagen") ?: "",
                        category = "Favorito",
                        price = doc.getDouble("precio") ?: 0.0,
                        rating = doc.getDouble("rating") ?: 0.0,
                        genero = doc.getString("genero") ?: "Desconocido"
                    )
                    adaptador.addProducto(producto)
                }
            }
            .addOnFailureListener { e ->
                Log.e("FIREBASE", "❌ Error cargando favoritos", e)
                Toast.makeText(this, "Error cargando favoritos", Toast.LENGTH_SHORT).show()
            }
    }
}
