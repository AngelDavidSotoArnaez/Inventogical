package com.angel.inventogical

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.angel.inventogical.adminSQLite.adminSQLiteOpenHelper
import com.angel.inventogical.databinding.FragmentTasaBinding
import com.angel.inventogical.viewModel.TasasLista
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TasaFragment : Fragment() {

    private lateinit var binding: FragmentTasaBinding

    private lateinit var conSQL: adminSQLiteOpenHelper

    private lateinit var tasasLista: TasasLista

    private lateinit var adapter: TasasAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        conSQL = adminSQLiteOpenHelper(requireContext())

        binding = FragmentTasaBinding.inflate(inflater, container, false)

        mostrarTasas()

        colocarFecha()

        binding.btnAgregarTasa.setOnClickListener { guardarTasa() }

        return binding.root
    }

    private fun mostrarTasas() {
        if (conSQL.selTasasNum()) {
            binding.rvTasa.visibility = View.VISIBLE
            binding.tvTasas.visibility = View.INVISIBLE

            tasasLista = conSQL.selAllTasas()

            //Creacion del Recycle View
            binding.rvTasa.layoutManager = LinearLayoutManager(requireContext())
            adapter = TasasAdapter(tasasLista = tasasLista,
                onClickDelete = { position -> borrarFila(position) })
            binding.rvTasa.adapter = adapter
            binding.rvTasa.setHasFixedSize(true)

        } else {
            binding.rvTasa.visibility = View.INVISIBLE
            binding.tvTasas.visibility = View.VISIBLE
        }
    }

    private fun guardarTasa() {
        if (binding.etTasa.editText?.text.toString() != "" || binding.etFechaTasa.editText?.text.toString() != "") {
            val tasa: Double = binding.etTasa.editText?.text.toString().toDouble()
            val fechaTasa: String = binding.etFechaTasa.editText?.text.toString()

            conSQL.addTasa(tasa, fechaTasa)

            colocarFecha()
            binding.etTasa.editText?.text = Editable.Factory.getInstance().newEditable("")

            val tasas = conSQL.selTasa()

            tasasLista.add(tasasLista.size, tasas)
            println(tasasLista)
            adapter.notifyItemInserted(tasasLista.size - 1)

        } else {
            Toast.makeText(requireContext(), "Rellene los campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun borrarFila(position: Int) {
        conSQL.delTasa(tasasLista[position].codigo)
        tasasLista.removeAt(position)
        adapter.notifyItemRemoved(position)
    }

    private fun colocarFecha() {

        binding.etFechaTasa.editText?.text = Editable.Factory.getInstance()
            .newEditable(LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
    }


}