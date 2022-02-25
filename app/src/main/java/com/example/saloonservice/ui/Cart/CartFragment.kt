package com.example.saloonservice.ui.Cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.saloonservice.ViewModel.ServiceViewModelFactory
import com.example.saloonservice.RoomDB.CreateDatabase
import com.example.saloonservice.Utils.SaloonApplication.Companion.CONTEXT
import com.example.saloonservice.databinding.FragmentCartBinding
import com.example.saloonservice.ui.Cart.adapter.CartListAdapter
import com.example.saloonservice.ViewModel.Repository.ServiceRepository
import com.example.saloonservice.ViewModel.ServiceViewModel

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var serviceViewModel: ServiceViewModel
    private lateinit var cartListAdapter: CartListAdapter

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initializing DAO class for getting Services added to card from RoomDB
        val userdao = CreateDatabase.getAppData(CONTEXT)?.roomDao()

        //Initializing ViewModel
        serviceViewModel = ViewModelProvider(this,ServiceViewModelFactory(requireContext(),
                            ServiceRepository(userdao)))[ServiceViewModel::class.java]

        // Method to get List of Services added to cart from RoomDB
        val cartList = serviceViewModel.getCartList()


       if(cartList.isNullOrEmpty()){
           // If cart was empty text view will display to notify empty cart
            binding.textNotifications.visibility = View.VISIBLE
        }else{
            binding.textNotifications.visibility = View.GONE

           // the retrieved list of services added to the cart was converted to Map of Key and Object by Group ID
           // Group ID was generated while adding service to cart based on selected employee(s)
            val grouped = cartList.groupBy { it.groupId }

           // Initializing adapter and adding layout manager to recycler view to display cart list
           cartListAdapter = CartListAdapter(requireActivity().layoutInflater,requireContext())
           binding.rclrCartList.layoutManager = LinearLayoutManager(requireContext())
           binding.rclrCartList.adapter = cartListAdapter

           // Mapped date based on Group ID was set to adapter
           cartListAdapter.setData(grouped)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}