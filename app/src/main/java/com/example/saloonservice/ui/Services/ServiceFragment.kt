package com.example.saloonservice.ui.Services

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.saloonservice.ViewModel.ServiceViewModelFactory
import com.example.saloonservice.Model.ServiceData
import com.example.saloonservice.RoomDB.CreateDatabase
import com.example.saloonservice.Utils.OpenArrowClick
import com.example.saloonservice.Utils.SaloonApplication.Companion.CONTEXT
import com.example.saloonservice.ViewModel.Repository.ServiceRepository
import com.example.saloonservice.ViewModel.ServiceViewModel
import com.example.saloonservice.databinding.FragmentServicesBinding
import com.example.saloonservice.ui.Services.adapter.ServiceListAdapter


class ServiceFragment : Fragment(),OpenArrowClick<ServiceData> {

    private var _binding: FragmentServicesBinding? = null
    private val binding get() = _binding!!

    private var serviceList:ArrayList<ServiceData> = ArrayList()

    private lateinit var serviceListAdapter: ServiceListAdapter
    private lateinit var serviceViewModel: ServiceViewModel

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        _binding = FragmentServicesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initializing DAO class for RoomDB functional and passed as a argument to repository
        val userdao = CreateDatabase.getAppData(CONTEXT)?.roomDao()

        // Initializing view model
        serviceViewModel = ViewModelProvider(this,ServiceViewModelFactory(requireContext(),
            ServiceRepository(userdao)))[ServiceViewModel::class.java]

        // method to check the network availability to get service detail from remote server
        // if not available a textview will shown to tell check network
        if(isNetworkAvailable(requireContext())){
            binding.txtHint.visibility = GONE
            _binding!!.shimmerViewContainer.visibility = VISIBLE
            _binding!!.shimmerViewContainer.startShimmer()
            serviceViewModel.getServiceList()
        }else{
            binding.txtHint.visibility = VISIBLE
            binding.shimmerViewContainer.visibility = GONE
            binding.rclrServicesList.visibility = GONE
            binding.txtHint.text = "Please Check Connectivity !"
        }

        // Observe service details receiving from remote server
        serviceViewModel.servicelist.observe(viewLifecycleOwner){
            if(it.responseCode == 200){
                binding.txtHint.visibility = GONE
                if(it.data.isNotEmpty()){
                    serviceList.clear()
                    serviceList.addAll(it.data)
                    // method to initialize adapter, layout manger and
                    // setting data to adapter to display employee list
                    loadServiceDetail()
                }else{
                    binding.txtHint.visibility = VISIBLE
                    binding.txtHint.text = "No Services Found"
                }
            }else{
                binding.txtHint.visibility = VISIBLE
                binding.txtHint.text = "Unable to Get Data. Please try again after some time"
            }
        }
        return root
    }

    @SuppressLint("NotifyDataSetChanged")
    // method to initialize adapter, layout manger and
    // setting data to adapter to display employee list
    private fun loadServiceDetail() {
        // Shimmer effect was stopped after data set to the adapter
        _binding!!.shimmerViewContainer.stopShimmer()
        binding.shimmerViewContainer.visibility = GONE

        binding.txtHint.visibility = GONE
        binding.rclrServicesList.visibility = VISIBLE
        serviceListAdapter = ServiceListAdapter(requireActivity().layoutInflater,requireContext(),this)
        binding.rclrServicesList.layoutManager = LinearLayoutManager(requireContext())
        binding.rclrServicesList.adapter = serviceListAdapter
        serviceListAdapter.setData(serviceList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // method to check network availability
    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
    }

    // method which call while clicking service from recycler view
    // and handle opening detail page after checking network availability
    override fun onOpenArrowClick(t: ServiceData) {
        if(isNetworkAvailable(requireContext())){

            // Bundle to share service details to add to cart page
            val bundle = Bundle()
            bundle.putString("name", t.name)
            bundle.putInt("price", t.price)
            bundle.putString("image", t.image)
            findNavController().navigate(com.example.saloonservice.R.id.action_navigation_service_to_navigation_detail,bundle)

        }else{
            Toast.makeText(requireContext(), "Please check connectivity!", Toast.LENGTH_SHORT).show()
        }
    }
}