package com.geekster.getinfo.fragmentAdd

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

import com.geekster.getinfo.NetworkResult
import com.geekster.getinfo.databinding.FragmentAddBinding
import com.geekster.getinfo.utils.Constants.TAG
import com.geekster.getinfo.utils.FileUtils
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AddViewModel>()
    private var selectedImageFile: File? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openGallery()
            } else {
                // Handle permission denied
            }
        }

    private val chooseImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    val filePath = FileUtils.getPath(requireContext(), uri)
                    selectedImageFile = File(filePath)
                    binding.imageView.setImageURI(uri)
                }
            }
        }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSelectImage.setOnClickListener {
            checkPermissionAndOpenGallery()
        }

        binding.btnAddProduct.setOnClickListener {
            if (inputValidation()) {
                val productName = binding.etProductName.text.toString()
                val productType = binding.etProductType.text.toString()
                val price = binding.etPrice.text.toString()
                val tax = binding.etTax.text.toString()

                viewModel.addProduct(productName, productType, price, tax, selectedImageFile)
                    .observe(viewLifecycleOwner) { result ->
                        when (result) {
                            is NetworkResult.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE

                            }
                            is NetworkResult.Success -> {
                                binding.progressBar.visibility = View.INVISIBLE
                                val addResponse = result.data
                                Log.d(TAG, "onViewCreated: $addResponse")
                                Toast.makeText(context,"Product Added",Toast.LENGTH_SHORT).show()
                                findNavController().popBackStack()

                            }
                            is NetworkResult.Error -> {
                                val errorMessage = result.message ?: "Unknown error occurred"
                                binding.progressBar.visibility = View.INVISIBLE
                                Log.d(TAG, "onViewCreated: $errorMessage")
                                Toast.makeText(context,"$errorMessage",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
            }

        }
    }


    private fun inputValidation() : Boolean {
        val productName = binding.etProductName.text.toString()
        val productType = binding.etProductType.text.toString()
        val price = binding.etPrice.text.toString()
        val tax = binding.etTax.text.toString()

        if (productName.isEmpty()) {
            showToast("Please enter Product name")
            return false
        }

        if (productType.isEmpty()) {
            showToast("Please enter Product type")
            return false
        }

        if (price.isEmpty()) {
            showToast("Please enter the Price")
            return false
        }

        if (tax.isEmpty()) {
            showToast("Please enter the Tax")
            return false
        }

        return true
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }



    private fun checkPermissionAndOpenGallery() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                openGallery()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        chooseImageLauncher.launch(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}