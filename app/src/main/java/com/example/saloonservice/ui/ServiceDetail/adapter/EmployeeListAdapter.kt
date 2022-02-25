package com.example.saloonservice.ui.ServiceDetail.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.saloonservice.Model.Data
import com.example.saloonservice.Model.ServiceData
import com.example.saloonservice.R
import com.example.saloonservice.Utils.CircularImageView
import com.example.saloonservice.Utils.OpenArrowClick
import com.example.saloonservice.ui.ServiceDetail.adapter.EmployeeListAdapter.*
import com.google.android.material.textview.MaterialTextView
/**
 * Purpose:
 * to display list of employees
 * the selected employee(s) list was send to the activity to store in RoomDB
 * */
class EmployeeListAdapter(private val layoutInflater: LayoutInflater, private val context: Context, private val clickListener:OpenArrowClick<Data>) : RecyclerView.Adapter<ServiceDetailViewHolder>() {

    // Array list to store Employee(s) and Selected Employee(s)
    private var employeeList:ArrayList<Data> = ArrayList()
    var employeeSelectedList:ArrayList<Data> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ServiceDetailViewHolder (
        layoutInflater.inflate(R.layout.layout_row_employee, parent, false))

    @SuppressLint("NotifyDataSetChanged")
    // Method for Set data to adapter
    fun setData(data:ArrayList<Data>){
        employeeList = data
        notifyDataSetChanged()
    }

    // Method to return selected employee(s) list to store in DB
    fun getSelectedEmployee():ArrayList<Data>{
        return employeeSelectedList
    }

    override fun onBindViewHolder(holder: ServiceDetailViewHolder, position: Int) {
        val services = employeeList[position]

        // Employess card click listener to select/Deselect employee
        holder.container.setOnClickListener {
            if(employeeSelectedList.isEmpty() || !employeeSelectedList.contains(services)){
                employeeSelectedList.add(services)
                holder.container.background = context.getDrawable(R.drawable.employee_selected_background)
            }else{
                employeeSelectedList.remove(services)
                holder.container.background = context.getDrawable(R.drawable.employee_de_selected_background)
            }
            // the below method indicate the activity about employee selected
            // based on this the add to cart button will enable
            clickListener.onOpenArrowClick(services)
        }

        // Employee name
        holder.name.text = services.name

        // Image of employee loaded using Glide library
        Glide.with(context)
            .load(services.image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(false)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    @Nullable e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.progress.visibility = View.GONE
                    holder.image.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_supervised_user_circle_24))
                    return false
                }
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable?>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.progress.visibility = View.GONE
                    return false
                }
            })
            .into(holder.image)

    }

    override fun getItemCount(): Int = employeeList.size

    class ServiceDetailViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        // Initialization on UI Components
        var image = itemView.findViewById<CircularImageView>(R.id.imgEmployeeDetail)
        var container = itemView.findViewById<ConstraintLayout>(R.id.lytEmployeeParent)
        var name = itemView.findViewById<MaterialTextView>(R.id.txtEmployeeName)
        var progress = itemView.findViewById<ContentLoadingProgressBar>(R.id.prgImageEmployee)

    }
}