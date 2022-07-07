package com.example.onlineshop.ui.profile

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.onlineshop.R
import com.example.onlineshop.model.Address



class AddressAdapter(
    private var setTextOfAddress2: (String) -> Unit,
    private var showOnMap: (lat:Double,long:Double) -> Unit) :
    ListAdapter<Address, AddressAdapter.ViewHolder>(AddressDiffCallback) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvAddress: TextView = view.findViewById(R.id.tv_address)
        private val btnSetAddress2: Button = view.findViewById(R.id.btn_set_text_of_address)
        private val btnShowOnMap: Button = view.findViewById(R.id.btn_show_on_map)

        @SuppressLint("SetTextI18n")
        fun bind(address:Address, setTextOfAddress2: (String) -> Unit,
                 showOnMap: (lat:Double,long:Double) -> Unit)
        {
            tvAddress.text=address.description
            btnSetAddress2.setOnClickListener { setTextOfAddress2(address.description) }
            btnShowOnMap.setOnClickListener { showOnMap(address.lat,address.long) }
        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.address_layout, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.bind(getItem(position),setTextOfAddress2, showOnMap)

    }


    object AddressDiffCallback : DiffUtil.ItemCallback<Address>() {
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }
    }
}