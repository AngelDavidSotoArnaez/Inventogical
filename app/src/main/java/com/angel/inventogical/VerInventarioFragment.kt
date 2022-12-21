package com.angel.inventogical

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.angel.inventogical.adminSQLite.adminSQLiteOpenHelper
import com.angel.inventogical.databinding.FragmentVerInventarioBinding
import com.angel.inventogical.viewModel.Articulo

class VerInventarioFragment : Fragment() {

    private lateinit var binding: FragmentVerInventarioBinding

    private var codigo: Int = 0

    //Creacion de la variable de la conexion con la base de datos
    private lateinit var conSQL: adminSQLiteOpenHelper

    private lateinit var articulo: Articulo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        codigo = arguments?.getInt("codigo")!!
        println(codigo)
        conSQL = adminSQLiteOpenHelper(requireContext())
        articulo = conSQL.selArticuloCodigo(codigo)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentVerInventarioBinding.inflate(inflater, container, false)

        binding.tvCodigo.text = articulo.codigo.toString()
        binding.tvNombre.text = articulo.nombre
        binding.tvFecha.text = articulo.fechamodifi.toString()
        binding.tvPrecioS.text = articulo.precioS.toString()
        binding.tvPrecioBs.text = articulo.precioBs.toString()
        binding.tvExistencia.text = articulo.existencia.toString()
        binding.tvUnidad.text = articulo.unidad

        return binding.root
    }

}