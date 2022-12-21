package com.angel.inventogical

import android.icu.text.DecimalFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.angel.inventogical.databinding.ItemInventarioBinding
import com.angel.inventogical.viewModel.Articulo
import com.angel.inventogical.viewModel.ListaArticulo
import kotlin.math.ceil
import kotlin.math.roundToInt

class InventarioAdapter(private var articulos: List<Articulo>, val listener: OnItemClickListener, val tasa: Double) :
    RecyclerView.Adapter<InventarioAdapter.InventarioHolder>() {

    inner class InventarioHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnLongClickListener {
        private val binding = ItemInventarioBinding.bind(view)

        fun render(articulo: Articulo, tasa: Double) {
            val dec = DecimalFormat("#,###.00")
            val precioBs = if (articulo.precioPorTasa == 1) ceil(articulo.precioS * tasa * 100) / 100 else articulo.precioBs
            val algo =16.23 * 100

            binding.tvCodigo.text = articulo.codigo.toString()
            binding.tvNombre.text = articulo.nombre
            binding.tvFecha.text = articulo.fechamodifi.toString()
            binding.tvPrecioS.text = dec.format(articulo.precioS)
            binding.tvPrecioBs.text = dec.format(precioBs)
            binding.tvExistencia.text = articulo.existencia.toString()
            binding.tvUnidad.text = articulo.unidad
        }

        init {
            view.setOnLongClickListener(this)
        }

        override fun onLongClick(v: View?): Boolean {
            val position = absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }

            return false
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventarioHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return InventarioHolder(layoutInflater.inflate(R.layout.item_inventario, parent, false))
    }

    override fun onBindViewHolder(holder: InventarioHolder, position: Int) {
        holder.render(articulos[position], tasa)
    }

    override fun getItemCount(): Int = articulos.size

    //Interface que guarda la celda seleccionada y la pasa al fragment Home
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun actualizarArticulos(articulos: List<Articulo>) {
        this.articulos = articulos
        notifyDataSetChanged()
    }

}

