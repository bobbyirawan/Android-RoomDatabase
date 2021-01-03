package com.belajar.roomdatabase.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.belajar.roomdatabase.model.User
import com.belajar.roomdatabase.databinding.ItemListFragmentBinding
import com.belajar.roomdatabase.fragments.list.ListFragment
import com.belajar.roomdatabase.fragments.list.ListFragmentDirections


class ListFragmentAdapter: RecyclerView.Adapter<ListFragmentAdapter.MyViewHolder>() {

   private var list = emptyList<User>()

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }


    inner class MyViewHolder(binding: ItemListFragmentBinding) : RecyclerView.ViewHolder(binding.root) {
       val id = binding.tvId
       val name = binding.tvName
       val age = binding.tvAge
       val rowLayout = binding.itemListCustom
   }


    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListFragmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentList = list[position]
        holder.id.text = currentList.id.toString()
        holder.name.text = currentList.firstName + " " + currentList.lastName
        holder.age.text = currentList.age.toString()

        holder.rowLayout.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentList)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(user: List<User>) {
        this.list = user
        notifyDataSetChanged()
    }

}