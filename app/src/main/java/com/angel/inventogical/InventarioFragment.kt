package com.angel.inventogical

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.angel.inventogical.adminSQLite.adminSQLiteOpenHelper
import com.angel.inventogical.databinding.FragmentInventarioBinding
import com.angel.inventogical.viewModel.ListaArticulo

class InventarioFragment : Fragment(), InventarioAdapter.OnItemClickListener {

    private lateinit var binding: FragmentInventarioBinding

    private lateinit var adapter: InventarioAdapter

    //Creacion de la variable de la conexion con la base de datos
    private lateinit var conSQL: adminSQLiteOpenHelper

    //private lateinit var vendedor: ListaVendedoresItem
    private var articulos: ListaArticulo = ListaArticulo()

    private var tasa: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        conSQL = adminSQLiteOpenHelper(requireContext())

        val tasas = conSQL.selTasa()

        tasa = tasas.tasa

        // Inflate the layout for this fragment
        binding = FragmentInventarioBinding.inflate(inflater, container, false)

        mostrarInventario()

        binding.etBuscar.addTextChangedListener { filtrarArticulo ->
            val articuloFiltrado =
                articulos.filter { articulo -> articulo.nombre.contains(filtrarArticulo.toString()) }
            adapter.actualizarArticulos(articuloFiltrado)
        }

        //Funcion para eliminar el icono de la lupa
        binding.etBuscar.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.etBuscar.setCompoundDrawables(null, null, null, null)
            } else if (!hasFocus) {
                if (binding.etBuscar.text.isEmpty()) {
                    binding.etBuscar.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_buscar,
                        0,
                        0,
                        0)
                }
            }
        }

        return binding.root
    }

    fun mostrarInventario() {
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!lo copiaste del otro, editalo
        if (conSQL.selArtiulosNum()) {
            binding.etBuscar.visibility = View.VISIBLE
            binding.rvInventario.visibility = View.VISIBLE
            binding.tvInventario.visibility = View.INVISIBLE

            articulos = conSQL.selAllAriculo()

            //Creacion del Recycle View
            binding.rvInventario.layoutManager = LinearLayoutManager(requireContext())
            adapter = InventarioAdapter(articulos, this, tasa)
            binding.rvInventario.adapter = adapter
            binding.rvInventario.setHasFixedSize(true)

        } else {
            binding.etBuscar.visibility = View.INVISIBLE
            binding.rvInventario.visibility = View.INVISIBLE
            binding.tvInventario.visibility = View.VISIBLE
        }
    }

    override fun onItemClick(position: Int) {
        val articulo = articulos[position]
        val codigo = articulo.codigo
        //ADInventarioFragment(codigo!!, adapter, position).show(childFragmentManager, "Alert")

        val alerta = AlertDialog.Builder(requireContext())
        alerta.setTitle("Seleccione una Opción")
        alerta.setMessage("Por favor, Seleccione una de las siguientes opciones")

        alerta.setPositiveButton("Ver Articulo") { dialog, which ->
            //Creacion de un Bundle que servira como contenedor para enviar datos al siguiente fragment
            val datosAEnviar = Bundle()

            //Guardado del codigo de vendedor
            datosAEnviar.putInt("codigo", codigo!!)

            //Navegacion al fagment de Vendedor
            //El fragment vendedor de informacion mas detallada de un mendedor
            findNavController().navigate(R.id.action_inventarioFragment_to_verInventarioFragment,
                datosAEnviar)
        }

        alerta.setNegativeButton("Editar Articulo") { dialog, which ->
            //Creacion de un Bundle que servira como contenedor para enviar datos al siguiente fragment
            val datosAEnviar = Bundle()

            //Guardado del codigo de vendedor
            datosAEnviar.putInt("codigo", codigo!!)

            //Navegacion al fagment de Vendedor
            //El fragment vendedor de informacion mas detallada de un mendedor
            findNavController().navigate(R.id.action_inventarioFragment_to_addFragment,
                datosAEnviar)
        }

        alerta.setNeutralButton("Eliminar Articulo") { dialog, which ->
            Toast.makeText(requireContext(), "Eliminar Articulo", Toast.LENGTH_SHORT).show()

            val alert = AlertDialog.Builder(requireContext())
            alert.setTitle("Seleccione una Opción")
            alert.setMessage("¿Esta seguro de desear borrar este articulo?")

            alert.setPositiveButton("Si") { dialog, which ->
                conSQL.delArticulo(codigo!!)
                adapter.notifyItemRemoved(position)
                mostrarInventario()
            }

            alert.setNegativeButton("No") { dialog, which ->
                alert.setCancelable(true)
            }

            alert.show()

        }

        alerta.show()
    }

}