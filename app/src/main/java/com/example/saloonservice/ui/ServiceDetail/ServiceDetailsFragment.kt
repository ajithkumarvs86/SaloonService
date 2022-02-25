package com.example.saloonservice.ui.ServiceDetail

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.saloonservice.ViewModel.ServiceViewModelFactory
import com.example.saloonservice.RoomDB.CartList
import com.example.saloonservice.Model.Data
import com.example.saloonservice.Model.ServiceData
import com.example.saloonservice.R
import com.example.saloonservice.RoomDB.CreateDatabase
import com.example.saloonservice.Utils.OpenArrowClick
import com.example.saloonservice.Utils.SaloonApplication.Companion.CONTEXT
import com.example.saloonservice.databinding.FragmentServiceDetailsBinding
import com.example.saloonservice.ui.ServiceDetail.adapter.EmployeeListAdapter
import com.example.saloonservice.ViewModel.Repository.ServiceRepository
import com.example.saloonservice.ViewModel.ServiceViewModel


class ServiceDetailsFragment : Fragment(),OpenArrowClick<Data> {

    private var _binding: FragmentServiceDetailsBinding? = null
    private val binding get() = _binding!!

    // List to store employee details fet from Server
    private var employeeList:ArrayList<Data> = ArrayList()

    // Local variable to store arguments value passed from Service detail screen
    private var name = ""
    private var image = ""
    private var price = ""

    private lateinit var serviceViewModel: ServiceViewModel
    private lateinit var employeeListAdapter:EmployeeListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentServiceDetailsBinding.inflate(inflater, container, false)

        //getting data passed as argument from service detail screen
        image = arguments?.getString("image").toString()
        name = arguments?.getString("name").toString()
        price = arguments?.getInt("price").toString()

        // Initializing DAO to store services adding cart and passed to repository as argument
        val userdao = CreateDatabase.getAppData(CONTEXT)?.roomDao()

        // Initalization of view model
        serviceViewModel = ViewModelProvider(this,ServiceViewModelFactory(requireContext(),
                            ServiceRepository(userdao)) )[ServiceViewModel::class.java]

        // Assigning title and cost
        _binding!!.txtTitleDetails.text = name
        _binding!!.txtPriceDetails.text = "$ $price"

        // checking for network availability to get employee details from server
        if(isNetworkAvailable(requireContext())){
            serviceViewModel.getEmployeeList()
        }

        //Add services to cart
        binding.btnAddtoCart.setOnClickListener {

            // get service and selected employee(s) list from adapter and sort it by employee id
            val data = employeeListAdapter.getSelectedEmployee().sortedBy { it.id }

            // Group was created by adding Adding employee id to GROUP constant
            // to identify and populating services added to cart based on employee group
            // Eg : "GROUP_1_2
            var group = "GROUP"
            var image = ""

            if (data.isNullOrEmpty()){
                Toast.makeText(requireContext(), "Please select employee(s) ", Toast.LENGTH_SHORT).show()
            }else{
                for (it in data){
                    group = group+"_"+it.id
                    image = image+it.image+","
                }

                // Group ID, Image URL of employee, Service name and cost are added as a Object
                // and Stored to RoomDB
               val employeeGroupList = (CartList(0,group,image,name,price))
                serviceViewModel.addCart(employeeGroupList)

                // after added to DB pop back screen and shows Success toast
                findNavController().popBackStack()
                Toast.makeText(requireContext(),"Service added to cart",Toast.LENGTH_SHORT).show()
            }
        }
        binding.imgBackServiceDetail.setOnClickListener {
            findNavController().popBackStack()
        }

        // To load the service image received from Service detail screen and load using Glide library
        Glide.with(requireContext())
            .load(image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(false)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    @Nullable e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    _binding!!.prgImageDetails.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable?>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    _binding!!.prgImageDetails.visibility = View.GONE
                    return false
                }
            })
            .into(_binding!!.imgServiceDetails)


        // Observing employee data returning from remote server
        serviceViewModel.employeelist.observe(viewLifecycleOwner){
            if(it.responseCode == 200){
                if(it.data.isNotEmpty()){
                    employeeList.clear()
                    employeeList.addAll(it.data)
                    // method to initialize adapter, layout manger and
                    // setting data to adapter to display employee list
                    loadEmployeeDetail()
                }
            }
        }


        val root: View = binding.root
        return root
    }

    // method to initialize adapter, layout manger and
    // setting data to adapter to display employee list
    private fun loadEmployeeDetail() {
        employeeListAdapter = EmployeeListAdapter(requireActivity().layoutInflater,requireContext(),this)
        binding.rclrEmployeeList.layoutManager = LinearLayoutManager(requireContext())
        binding.rclrEmployeeList.adapter = employeeListAdapter
        employeeListAdapter.setData(employeeList)
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

    @RequiresApi(Build.VERSION_CODES.M)
    // method which call while clicking selecting the employee from recycler view
    // and handle enabling and disabling of add to cart button
    override fun onOpenArrowClick(t: Data) {
        val data = employeeListAdapter.getSelectedEmployee()
        _binding!!.btnAddtoCart.isEnabled = data.isNotEmpty()
    }


}