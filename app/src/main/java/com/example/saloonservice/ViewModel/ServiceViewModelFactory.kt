package com.example.saloonservice.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.saloonservice.ViewModel.Repository.ServiceRepository

class ServiceViewModelFactory(val context: Context, val repository : ServiceRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       return  ServiceViewModel(context,repository) as T
    }
}