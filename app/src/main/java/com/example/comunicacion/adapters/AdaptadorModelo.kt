package com.example.comunicacion.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.comunicacion.R
import com.example.comunicacion.model.Modelo

class AdaptadorModelo(private var lista: ArrayList<Modelo>, private val context: Context) :
    RecyclerView.Adapter<AdaptadorModelo.MyHolder>() {

    class MyHolder(item: View) : RecyclerView.ViewHolder(item) {
        val imagen: ImageView = item.findViewById(R.id.imagenFila)
        val titulo: TextView = item.findViewById(R.id.tituloFila)
        val subTitulo: TextView = item.findViewById(R.id.subtituloFila)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val vista: View = LayoutInflater.from(context).inflate(
            R.layout.item_modelo_v2,
            parent,
            false
        )
        return MyHolder(vista)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val elemento = lista[position]
        holder.titulo.text = elemento.modelo
        holder.subTitulo.text = elemento.marca

        Glide.with(context)
            .load(elemento.imagen)
            .into(holder.imagen)
    }

    fun cambiarLista(lista: ArrayList<Modelo>) {
        this.lista = lista
        notifyDataSetChanged()
    }
}
