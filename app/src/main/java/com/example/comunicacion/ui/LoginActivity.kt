package com.example.comunicacion.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import com.example.comunicacion.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authFirebase: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authFirebase = FirebaseAuth.getInstance()

        binding.botonLogin.setOnClickListener(this)
        binding.botonSignUp.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.botonLogin.id -> {
                val email = binding.editCorreo.text.toString().trim()
                val pass = binding.editPass.text.toString()

                if (email.isNotEmpty() && pass.isNotEmpty()) {
                    authFirebase.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val intent = Intent(this, MainActivity::class.java)
                                intent.putExtra("correo", email)
                                intent.putExtra("uid", authFirebase.currentUser?.uid)
                                startActivity(intent)
                                finish()
                            } else {
                                Snackbar.make(binding.root, "Fallo al iniciar sesión", Snackbar.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Snackbar.make(binding.root, "Completa todos los campos", Snackbar.LENGTH_SHORT).show()
                }
            }

            binding.botonSignUp.id -> {
                val intent = Intent(this, SignupActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.example.comunicacion.R.menu.help_menu, menu)
        return true
    }
}
