package com.example.onlineshop.ui.profile

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.onlineshop.databinding.AddressLayoutBinding
import com.example.onlineshop.model.Address



class AddressAdapter(
    private var setTextOfAddress2: (String) -> Unit,
    private var showOnMap: (lat:Double,long:Double) -> Unit,
    private var deleteAddress: (address:Address) -> Unit) :
    ListAdapter<Address, AddressAdapter.ViewHolder>(AddressDiffCallback) {

    class ViewHolder(private val binding:AddressLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(address:Address, setTextOfAddress2: (String) -> Unit,
                 showOnMap: (lat:Double,long:Double) -> Unit,
                 deleteAddress: (address:Address) -> Unit) =
            binding.apply{
                tvAddress.text=address.description
                btnSetTextOfAddress.setOnClickListener { setTextOfAddress2(address.description) }
                btnShowOnMap.setOnClickListener { showOnMap(address.lat,address.long) }
                ibDeleteAddress.setOnClickListener { deleteAddress(address) }
        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val binding = AddressLayoutBinding.inflate(LayoutInflater.from(viewGroup.context)
        ,viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.bind(getItem(position),setTextOfAddress2, showOnMap,deleteAddress)

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