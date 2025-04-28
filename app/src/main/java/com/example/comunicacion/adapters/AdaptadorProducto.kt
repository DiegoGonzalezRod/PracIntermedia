package com.example.comunicacion.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.comunicacion.R
import com.example.comunicacion.model.Producto
import com.example.comunicacion.ui.DetalleActivity
import com.google.android.material.chip.Chip

class AdaptadorProducto(private val context: Context) :
    RecyclerView.Adapter<AdaptadorProducto.ProductoViewHolder>() {

    private val listaProductos = ArrayList<Producto>()

    class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagen: ImageView = itemView.findViewById(R.id.imagenFila)
        val titulo: TextView = itemView.findViewById(R.id.tituloFila)
        val subTitulo: TextView = itemView.findViewById(R.id.subtituloFila)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        val chipGenero: Chip = itemView.findViewById(R.id.chipGenero)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val vista = LayoutInflater.from(context).inflate(R.layout.item_modelo_v2, parent, false)
        return ProductoViewHolder(vista)
    }

    override fun getItemCount(): Int = listaProductos.size

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = listaProductos[position]

        holder.titulo.text = "🎞️ ${producto.title}"
        holder.subTitulo.text = "💸 $${String.format("%.2f", producto.price)}"
        holder.ratingBar.rating = (producto.rating / 2).toFloat()
        holder.chipGenero.text = producto.genero

        Glide.with(context)
            .load(producto.thumbnail)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.imagen)


        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetalleActivity::class.java).apply {
                putExtra("id", producto.id)
                putExtra("titulo", producto.title)
                putExtra("descripcion", producto.description)
                putExtra("imagen", producto.thumbnail)
                putExtra("precio", producto.price)
                putExtra("rating", producto.rating)
                putExtra("genero", producto.genero)
            }
            context.startActivity(intent)
        }
    }

    fun addProducto(producto: Producto) {
        listaProductos.add(producto)
        notifyItemInserted(listaProductos.size - 1)
    }

    fun limpiar() {
        listaProductos.clear()
        notifyDataSetChanged()
    }
}
