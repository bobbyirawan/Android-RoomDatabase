package com.belajar.roomdatabase.fragments.add

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.belajar.roomdatabase.R
import com.belajar.roomdatabase.databinding.FragmentAddBinding
import com.belajar.roomdatabase.model.User
import com.belajar.roomdatabase.viewModel.UserViewModel

class AddFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private lateinit var mUserViewModel: UserViewModel
    private val REQUEST_CODE_SELECT_IMAGE = 2

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
        binding.addImage.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnAdd -> insertDataToDatabase()
            R.id.addImage -> selectImage()
        }
    }

    private fun insertDataToDatabase() {
        val firstName = binding.addFirstname.text.toString()
        val lastName = binding.addLastname.text.toString()
        val age = binding.addAge.text

        if (inputCheck(firstName, lastName, age)) {
            // create user object
            val user = User(0, firstName, lastName, Integer.parseInt(age.toString()))

            // add data to database
            mUserViewModel.addUser(user)
            Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show()

            // navigate back
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "please fill out all fields", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun selectImage() {
        if(requestPermissions()) {
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                startActivityForResult(this, REQUEST_CODE_SELECT_IMAGE)
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            binding.addUserPicture.setImageURI(data?.data)
        }
    }

    private fun inputCheck(firstName: String, lastName: String, age: Editable): Boolean {
        return !(TextUtils.isEmpty(firstName) && TextUtils.isEmpty(lastName) && age.isEmpty())
    }

    private fun requestPermissions(): Boolean {
        val permissionToRequest = mutableListOf<String>()

        if (!hasReadExternalStoragePermission()) {
            permissionToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        return if (permissionToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(), permissionToRequest.toTypedArray(),
                0
            )
            false
        } else {
            true
        }
    }

    private fun hasReadExternalStoragePermission() =
        ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isNotEmpty()) {
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("PermissionsRequest", "${permissions[i]} granted.")
                }
            }
        }
    }

}