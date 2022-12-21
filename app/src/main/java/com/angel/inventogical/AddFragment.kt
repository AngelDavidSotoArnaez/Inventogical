package com.angel.inventogical

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.angel.inventogical.adminSQLite.adminSQLiteOpenHelper
import com.angel.inventogical.databinding.FragmentAddBinding
import com.angel.inventogical.viewModel.Articulo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddFragment : Fragment() {

    //Creacion de la variable de la conexion con la base de datos
    private lateinit var conSQL: adminSQLiteOpenHelper

    private lateinit var binding: FragmentAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        conSQL = adminSQLiteOpenHelper(requireContext())

        // Inflate the layout for this fragment
        binding = FragmentAddBinding.inflate(inflater, container, false)

        validarEditOrAdd()

        binding.btnAgregar.setOnClickListener {


            val codigo = null
            val nombre: String = binding.etNombre.editText?.text.toString()
            val unidad: String = binding.etUnidad.editText?.text.toString()
            val existencia: String = binding.etExistencia.editText?.text.toString()
            val precioS: String = binding.etPrecioS.editText?.text.toString()
            val precioBs: String = binding.etPrecioBs.editText?.text.toString()
            val fechamodifi =
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            val precioPorTasa: Int = if (binding.cbTasa.isChecked) 1 else 0

            if (nombre == "" || unidad == "" || existencia == "" || precioS == "" || precioBs == "") {
                Toast.makeText(requireContext(), "No deje espacios en blanco", Toast.LENGTH_SHORT)
                    .show()
            } else {

                val articulo = Articulo(
                    codigo,
                    nombre,
                    unidad,
                    existencia.toDouble(),
                    precioS.toDouble(),
                    precioBs.toDouble(),
                    fechamodifi,
                    precioPorTasa
                )

                if (arguments?.getInt("codigo") == null) {
                    conSQL.articulosAdd(articulo)
                    Toast.makeText(requireContext(),
                        "Articulo: $nombre, fue añadadido con exito",
                        Toast.LENGTH_SHORT).show()
                } else {
                    articulo.codigo = arguments?.getInt("codigo")
                    conSQL.articulosUp(articulo)
                    Toast.makeText(requireContext(),
                        "Articulo: $nombre, fue editado con exito",
                        Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }

                println(articulo)

                blanco()
            }

        }

        return binding.root
    }

    private fun validarEditOrAdd() {
        if (arguments?.getInt("codigo") == null) {
            blanco()
        } else {
            val codigo = arguments?.getInt("codigo")

            val articulo = conSQL.selArticuloCodigo(codigo!!)

            println("ARTICULOS -> $articulo")

            binding.etNombre.editText?.text = creacionEditText(articulo.nombre)
            binding.etUnidad.editText?.text = creacionEditText(articulo.unidad)
            binding.etExistencia.editText?.text = creacionEditText(articulo.existencia.toString())
            binding.etPrecioS.editText?.text = creacionEditText(articulo.precioS.toString())
            binding.etPrecioBs.editText?.text = creacionEditText(articulo.precioBs.toString())
            binding.cbTasa.isChecked = articulo.precioPorTasa == 1
        }
    }

    private fun blanco() {
        val blanco = creacionEditText("")

        binding.etNombre.editText?.text = blanco
        binding.etUnidad.editText?.text = blanco
        binding.etExistencia.editText?.text = blanco
        binding.etPrecioS.editText?.text = blanco
        binding.etPrecioBs.editText?.text = blanco
        binding.cbTasa.isChecked = false
    }

    private fun creacionEditText(text: String): Editable? {
        return Editable.Factory.getInstance().newEditable(text)
    }

}