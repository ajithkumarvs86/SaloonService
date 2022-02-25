package com.example.saloonservice.ui.Services.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.saloonservice.Model.ServiceData
import com.example.saloonservice.R
import com.example.saloonservice.Utils.OpenArrowClick
import com.example.saloonservice.ui.Services.adapter.ServiceListAdapter.ServiceViewHolder
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView


/**
 * Purpose:
 * to display list of Service
 * */
class ServiceListAdapter(private val layoutInflater: LayoutInflater, private val context: Context ,private val openArrowClick: OpenArrowClick<ServiceData>) : RecyclerView.Adapter<ServiceViewHolder>() {

    // Array list variable to store list of services get from remove server
    private var serviceList:ArrayList<ServiceData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ServiceViewHolder (
        layoutInflater.inflate(R.layout.layout_row_services, parent, false))

    @SuppressLint("NotifyDataSetChanged")
    // Method to set data to adapter
    fun setData(data:ArrayList<ServiceData>){
        serviceList = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
      holder.bindData(serviceList[position],openArrowClick,context)
    }

    override fun getItemCount(): Int = serviceList.size

    class ServiceViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        // initializing UI Components
        var image = itemView.findViewById<ShapeableImageView>(R.id.imgServiceHome)
        var name = itemView.findViewById<MaterialTextView>(R.id.txtServiceName)
        var prgImageService = itemView.findViewById<ContentLoadingProgressBar>(R.id.prgImageService)
        var cost = itemView.findViewById<MaterialTextView>(R.id.txtServiceCost)

        @SuppressLint("SetTextI18n")
        // method to bind data to UI
        fun bindData(services: ServiceData,openArrowClick: OpenArrowClick<ServiceData>,context: Context){

            // Progress bar for Image loading
            prgImageService.visibility = View.VISIBLE

            // Setting background color to white after stopping shimmer effect
            name.setBackgroundColor(Color.WHITE)
            image.setBackgroundColor(Color.WHITE)
            cost.setBackgroundColor(Color.WHITE)

            name.text = services.name
            cost.text = "$ "+services.price

            // Card click lister which call method in context to launch add to cart screen
            itemView.setOnClickListener {
            openArrowClick.onOpenArrowClick(services)
            }

            // Service images URL were loaded using Glide library
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
                        prgImageService.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable?>?,
                        dataSource: com.bumptech.glide.load.DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        prgImageService.visibility = View.GONE
                        return false
                    }
                })
                .into(image)
        }

    }
}