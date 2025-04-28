package com.example.comunicacion.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.comunicacion.databinding.ActivitySignupBinding
import com.example.comunicacion.model.Usuario
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var authFirebase: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        instancias()

        binding.botonRegistro.setOnClickListener {
            val nombre = binding.editNombre.text.toString().trim()
            val correo = binding.editCorreo.text.toString().trim()
            val pass = binding.editPass.text.toString().trim()
            val pass2 = binding.editPass2.text.toString().trim()

            if (nombre.isNotEmpty() && correo.isNotEmpty() && pass.isNotEmpty() && pass == pass2) {
                val usuario = Usuario(nombre = nombre, correo = correo, pass = pass)

                authFirebase.createUserWithEmailAndPassword(correo, pass)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            firebaseDatabase.reference.child("usuarios")
                                .child(authFirebase.currentUser!!.uid).setValue(usuario)

                            Snackbar.make(binding.root, "Usuario registrado con éxito", Snackbar.LENGTH_LONG)
                                .setAction("Ir a login") {
                                    startActivity(Intent(this, LoginActivity::class.java))
                                    finish()
                                }.show()
                        } else {
                            Snackbar.make(binding.root, "Fallo en el registro", Snackbar.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Snackbar.make(binding.root, "Rellena todos los campos y confirma la contraseña", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun instancias() {
        authFirebase = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance("https://practicadesarrolloapp-default-rtdb.firebaseio.com/")
    }
}
