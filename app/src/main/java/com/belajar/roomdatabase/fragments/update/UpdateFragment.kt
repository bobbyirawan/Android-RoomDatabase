package com.belajar.roomdatabase.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.belajar.roomdatabase.R
import com.belajar.roomdatabase.databinding.FragmentUpdateBinding
import com.belajar.roomdatabase.model.User
import com.belajar.roomdatabase.viewModel.UserViewModel

class UpdateFragment : Fragment(), View.OnClickListener {

    private val args by navArgs<UpdateFragmentArgs>()
    private lateinit var userViewModel: UserViewModel
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        // updateUser from argument navigation controller
        binding.updateFirstname.setText(args.updateUser.firstName)
        binding.updateLastname.setText(args.updateUser.lastName)
        binding.updateAge.setText(args.updateUser.age.toString())

        binding.btnUpdate.setOnClickListener(this)

        // add menu
        setHasOptionsMenu(true)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnUpdate -> updateUser()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete -> {
                deleteUser()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteUser() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setNegativeButton("Cancel") { _, _ -> }
        builder.setPositiveButton("Yes") { _, _ ->
            userViewModel.deleteUser(args.updateUser)
            showMessage("Successfully remove: ${args.updateUser.firstName}")
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }

        // updateUser from argument navigation controller
        builder.setTitle(args.updateUser.firstName)
        builder.setMessage("are you sure want to delete ${args.updateUser.firstName}?")
        builder.create().show()
    }

    private fun updateUser() {
        val firstName = binding.updateFirstname.text.toString()
        val lastName = binding.updateLastname.text.toString()
        val age = binding.updateAge.text

        if (inputCheck(firstName, lastName, age)) {
            // create user object
            val updateUser =
                // updateUser from argument navigation controller
                User(args.updateUser.id, firstName, lastName, Integer.parseInt(age.toString()))

            // add data to database
            userViewModel.changeUser(updateUser)
            showMessage("Update Successfully")

            // navigate back
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            showMessage("please fill out all fields")
        }
    }

    private fun inputCheck(firstName: String, lastName: String, age: Editable): Boolean {
        return !(TextUtils.isEmpty(firstName) && TextUtils.isEmpty(lastName) && age.isEmpty())
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


}