package com.angel.inventogical

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.angel.inventogical.databinding.ItemTasasBinding
import com.angel.inventogical.viewModel.Tasas
import com.angel.inventogical.viewModel.TasasLista

class TasasAdapter(private val tasasLista: TasasLista, private val onClickDelete:(Int) -> Unit) :
    RecyclerView.Adapter<TasasAdapter.TasasHolder>() {

    inner class TasasHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemTasasBinding.bind(view)

        fun render(tasasLista: Tasas, onClickDelete: (Int) -> Unit) {
            binding.tvItemTasa.text = "Tasa: ${tasasLista.tasa}"
            binding.tvItemFechaTasa.text = "Fecha: ${tasasLista.fechaTasa}"
            binding.btnBorraItemTasa.setOnClickListener{onClickDelete(bindingAdapterPosition)}
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasasHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TasasHolder(layoutInflater.inflate(R.layout.item_tasas, parent, false))
    }

    override fun onBindViewHolder(holder: TasasHolder, position: Int) {
        holder.render(tasasLista[position], onClickDelete)
    }

    override fun getItemCount(): Int = tasasLista.size

}