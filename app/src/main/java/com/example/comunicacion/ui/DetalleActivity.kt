package com.example.comunicacion.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.comunicacion.databinding.ActivityDetalleBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DetalleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Recoger datos del intent
        val titulo = intent.getStringExtra("titulo") ?: ""
        val descripcion = intent.getStringExtra("descripcion") ?: ""
        val imagen = intent.getStringExtra("imagen") ?: ""
        val precio = intent.getDoubleExtra("precio", 0.0)
        val rating = intent.getDoubleExtra("rating", 0.0)
        val genero = intent.getStringExtra("genero") ?: ""

        // Mostrar en la UI
        binding.textTitulo.text = titulo
        binding.textDescripcion.text = descripcion
        binding.textPrecio.text = "Precio: $%.2f".format(precio)
        binding.ratingDetalle.rating = (rating / 2).toFloat()
        binding.textGenero.text = "Género: $genero"

        Glide.with(this)
            .load(imagen)
            .into(binding.imageDetalle)

        // Volver atrás
        binding.btnVolver.setOnClickListener {
            finish()
        }

        // Guardar como favorito
        binding.btnFavorito.setOnClickListener {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                val favorito = hashMapOf(
                    "titulo" to titulo,
                    "descripcion" to descripcion,
                    "imagen" to imagen,
                    "precio" to precio,
                    "rating" to rating,
                    "genero" to genero
                )

                firestore.collection("usuarios")
                    .document(userId)
                    .collection("favoritos")
                    .add(favorito)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Añadido a favoritos", Toast.LENGTH_SHORT).show()
                        Log.d("FIREBASE", "✅ Añadido a Firestore correctamente.")
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
                        Log.e("FIREBASE", "❌ Error al guardar en Firestore", e)
                    }
            } else {
                Toast.makeText(this, "No hay usuario autenticado", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
