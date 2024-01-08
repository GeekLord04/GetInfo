package com.geekster.getinfo.fragmentList

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.geekster.getinfo.NetworkResult
import com.geekster.getinfo.R
import com.geekster.getinfo.databinding.FragmentListBinding
import com.geekster.getinfo.utils.Constants.TAG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment() {

    private var _binding : FragmentListBinding? = null
    private val binding get() = _binding!!

    private val listViewModel by viewModels<ListViewModel>()

    private lateinit var adapter : ItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Toast.makeText(context,"You can swipe to refresh",Toast.LENGTH_SHORT).show()
        super.onViewCreated(view, savedInstanceState)

        bindObserver()

        listViewModel.getList()
        adapter = ItemAdapter(mutableListOf())
        binding.itemList.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.itemList.adapter = adapter
        binding.addItemBtn.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }


        binding.swipeRefresh.setOnRefreshListener {
            listViewModel.getList()
        }



    }

    private fun bindObserver() {
        listViewModel.listLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Success -> {
                    adapter.submitList(it.data)
                    Log.d(TAG, it.data.toString())
                    binding.swipeRefresh.isRefreshing = false;
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(),it.message.toString(), Toast.LENGTH_SHORT).show()
                    binding.swipeRefresh.isRefreshing = false
                }
                is NetworkResult.Loading -> {
                    Log.d("Error",it.message.toString())
                    binding.swipeRefresh.isRefreshing = true
                }

                else -> {}
            }
        })
    }

//    private fun bind(note: ListResponseItem) {
//        Log.d(TAG, "bind: ${note.product_name}")
//    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}