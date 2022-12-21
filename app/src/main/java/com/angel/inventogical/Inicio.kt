package com.angel.inventogical

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.angel.inventogical.databinding.FragmentInicioBinding


class Inicio : Fragment() {

    //Creando variables binding para editar la inferfaz de usuario
    private var _binding: FragmentInicioBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        //Inicializacion de la variable binding para usar la interfaz visual
        _binding = FragmentInicioBinding.inflate(inflater, container, false)

        binding.btnInventario.setOnClickListener {
            findNavController().navigate(R.id.action_primerFragment_to_inventarioFragment)
        }

        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_primerFragment_to_addFragment)
        }

        binding.btnTasa.setOnClickListener {
            findNavController().navigate(R.id.action_primerFragment_to_tasaFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Ver. 1"
    }


}