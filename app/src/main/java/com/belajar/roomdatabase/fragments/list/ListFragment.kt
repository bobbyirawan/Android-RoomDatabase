package com.belajar.roomdatabase.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.belajar.roomdatabase.R
import com.belajar.roomdatabase.adapter.ListFragmentAdapter
import com.belajar.roomdatabase.databinding.FragmentListBinding
import com.belajar.roomdatabase.viewModel.UserViewModel

class ListFragment : Fragment(), View.OnClickListener {


    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var mUserViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ListFragmentAdapter()
        binding.rvListFragment.adapter = adapter
        binding.rvListFragment.layoutManager = LinearLayoutManager(requireContext())

        // connector to viewmodel
        mUserViewModel = ViewModelProvider(this).get(
            UserViewModel::
            class.java
        )
        mUserViewModel.readAllData.observe(viewLifecycleOwner,
            {
                adapter.setData(it)
            })
        binding.floatingActionButton.setOnClickListener(this)

        setHasOptionsMenu(true)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.floatingActionButton -> {
                findNavController().navigate(R.id.action_listFragment_to_addFragment)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete -> {
                deleteAllUser()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllUser() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setNegativeButton("Cancel") { _, _ -> }
        builder.setPositiveButton("Yes") { _, _ ->
            mUserViewModel.deleteAllUser()
            Toast.makeText(requireContext(), "Remove Successfully", Toast.LENGTH_SHORT).show()
        }

        // updateUser from argument navigation controller
        builder.setTitle("Delete")
        builder.setMessage("are you sure want to delete all data?")
        builder.create().show()
    }


}