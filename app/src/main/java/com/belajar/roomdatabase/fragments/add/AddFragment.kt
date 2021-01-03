package com.belajar.roomdatabase.fragments.add

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.belajar.roomdatabase.R
import com.belajar.roomdatabase.model.User
import com.belajar.roomdatabase.viewModel.UserViewModel
import com.belajar.roomdatabase.databinding.FragmentAddBinding

class AddFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private lateinit var mUserViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        binding.btnAdd.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnAdd -> {
                insertDataToDatabase()
            }
        }
    }

    private fun insertDataToDatabase() {
        val firstName = binding.addFirstname.text.toString()
        val lastName = binding.addLastname.text.toString()
        val age = binding.addAge.text

        if(inputCheck(firstName, lastName, age)) {
            // create user object
            val user = User(0, firstName, lastName, Integer.parseInt(age.toString()))

            // add data to database
            mUserViewModel.addUser(user)
            Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show()

            // navigate back
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "please fill out all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun inputCheck(firstName: String, lastName: String, age: Editable): Boolean {
        return !(TextUtils.isEmpty(firstName) && TextUtils.isEmpty(lastName) && age.isEmpty())
    }


}