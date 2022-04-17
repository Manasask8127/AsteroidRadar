package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.adapter.AsteroidAdapter
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.domain.Asteroid

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this,MainViewModelFactory(requireActivity().application)).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)
        val adapter=AsteroidAdapter(AsteroidAdapter.OnAsteroidClickListener{
            viewModel.displayDetails(it)
        })

        binding.asteroidRecycler.adapter=adapter

        viewModel.asteroidList.observe(viewLifecycleOwner,Observer<List<Asteroid>>{
            it?.let { adapter.submitList(it) }
        })

        viewModel.navigateToDetails.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.displayDetailsFinished()
            }
        })

        viewModel.connectToNetwork.observe(viewLifecycleOwner, Observer {
            if(it==true)
                Toast.makeText(requireContext(),"network connection back", Toast.LENGTH_SHORT).show()
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.show_buy_menu -> viewModel.getAsteroidsByToday()
            R.id.show_rent_menu -> viewModel.getAsteroidsBySave()
            else -> viewModel.getAsteroidsByWeek()
        }
        return true
    }
}
