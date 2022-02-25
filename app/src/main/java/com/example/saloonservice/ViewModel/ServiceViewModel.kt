package com.example.saloonservice.ViewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saloonservice.Model.*
import com.example.saloonservice.RoomDB.CartList
import com.example.saloonservice.ViewModel.Repository.ServiceRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class ServiceViewModel( val context: Context, val repository: ServiceRepository) : ViewModel() {

    val servicelist = MutableLiveData<ServiceList>()
    val employeelist = MutableLiveData<EmployeeList>()
    val cartlist = MutableLiveData<EmployeeList>()

    // Method to Get List Of Services
    fun getServiceList(){
        viewModelScope.launch {
            try {
                val response: Response<ServiceList> = repository.getServiceList()
                servicelist.value =response.body()
            }
            catch (e:Exception){
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Method to Get Employee(s) List
    fun getEmployeeList(){
        viewModelScope.launch {
            try {
                val response: Response<EmployeeList> = repository.getEmployeeList()
                employeelist.value = response.body()
            }
            catch (e:Exception){
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Method to Add Services to cart
    fun addCart(service: CartList){
        repository.addCart(service)
    }

    // Method to Get List Of Services added to cart
    fun getCartList() : List<CartList>{
        return repository.getCartList()
    }


}