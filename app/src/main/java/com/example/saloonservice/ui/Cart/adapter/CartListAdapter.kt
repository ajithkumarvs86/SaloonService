package com.example.saloonservice.ui.Cart.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.saloonservice.R
import com.example.saloonservice.RoomDB.CartList
import com.example.saloonservice.ui.Cart.adapter.CartListAdapter.CartDetailViewHolder
import com.example.saloonservice.Utils.CircularImageView
/**
 * Purpose:
 * This adapter created to display Services added to the cart
 * the list of services displayed were grouped by Employee(s) were selected when adding
 * */

class CartListAdapter(private val layoutInflater: LayoutInflater, private val context: Context) : RecyclerView.Adapter<CartDetailViewHolder>() {

    // Hasp map with Key and Object pair to populate services group by employee(s)
    private var employeeList: Map<String, List<CartList>> = HashMap()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CartDetailViewHolder (
        layoutInflater.inflate(R.layout.layout_cart_row, parent, false))

    @SuppressLint("NotifyDataSetChanged")
    // Method to set data to adapter
    fun setData(data: Map<String, List<CartList>>){
        employeeList = data
        notifyDataSetChanged()
    }



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: CartDetailViewHolder, position: Int) {
        // Get Group ID list from HashMap and store to Keys variable,
        // so we can easily access the list by group ID
        val keys: List<*> = ArrayList(employeeList.keys)
        val key = keys[position]

        //Using Group ID we can get the list of services belong to them and bind then with UI
        val services = employeeList[key]

        // method to bind data to UI
        holder.setBind(services,context)
    }

    override fun getItemCount(): Int = employeeList.size

    class CartDetailViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        // Initializing UI Components
        val lytImage = itemView.findViewById<LinearLayout>(R.id.lytImage)
        val lytService = itemView.findViewById<LinearLayout>(R.id.lytService)
        val lytPrice = itemView.findViewById<LinearLayout>(R.id.lytPrice)

        @RequiresApi(Build.VERSION_CODES.M)
        fun setBind(services: List<CartList>?, context: Context) {

            if (services != null) {

                // Every Index in the group have same Image URL, so we get from Oth index
                val images = services[0].images

                // Image URL are stored to single column in DB as String
                // Group of employee images stored by adding ',' at end and
                // now URL's are separated using delimiter split an added to list
                val elements: List<String> = images.split(",")

                for(i in elements){
                    if (i.isNotEmpty()){
                        // Depends on Image list size Dynamic CircularImageView was created and
                        // URL loaded to View with Glide Library
                        val image = CircularImageView(context)
                        image.layoutParams = LayoutParams(130, 130)
                        image.scaleType = ImageView.ScaleType.CENTER_INSIDE
                        image.setPadding(0,0,5,10)
                        Glide.with(context)
                            .load(i)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .skipMemoryCache(false)
                            .listener(object : RequestListener<Drawable?> {
                                override fun onLoadFailed(
                                    @Nullable e: GlideException?,
                                    model: Any?,
                                    target: com.bumptech.glide.request.target.Target<Drawable?>?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    image.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_supervised_user_circle_24))
                                    return false
                                }
                                override fun onResourceReady(
                                    resource: Drawable?,
                                    model: Any?,
                                    target: com.bumptech.glide.request.target.Target<Drawable?>?,
                                    dataSource: com.bumptech.glide.load.DataSource?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    return false
                                }
                            })
                            .into(image)
                        lytImage.addView(image)
                    }
                }

                for(it in services){
                    // Depends on Service list size Dynamic Textview for Service name and Cost was created
                    val serviceName = TextView(context)
                    serviceName.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                    serviceName.setTextColor(context.getColor(R.color.black))
                    serviceName.setTypeface(null,Typeface.BOLD)
                    serviceName.setTextSize(TypedValue.COMPLEX_UNIT_SP,18F);

                    val price = TextView(context)
                    price.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                    price.setTextColor(context.getColor(R.color.black))
                    price.setTypeface(null,Typeface.BOLD)
                    price.setTextSize(TypedValue.COMPLEX_UNIT_SP,18F);

                    serviceName.text = it.service
                    price.text = "$ "+it.price

                    // Created Textview are added to their respective parent layout
                    lytService.addView(serviceName)
                    lytPrice.addView(price)
                }
            }

        }


    }
}