package com.udacity.asteroidradar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.AsteroidItemBinding
import com.udacity.asteroidradar.domain.Asteroid

class AsteroidAdapter : ListAdapter<Asteroid,AsteroidAdapter.AsteroidViewHolder> (AsteroidDiffCallback()){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AsteroidAdapter.AsteroidViewHolder {
        return AsteroidViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AsteroidAdapter.AsteroidViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AsteroidViewHolder(val binding: AsteroidItemBinding):
            RecyclerView.ViewHolder(binding.root){
                fun bind(item:Asteroid){
                    binding.asteroid=item
                    binding.executePendingBindings()
                }

        companion object{
            fun from(parent: ViewGroup):AsteroidViewHolder{
                val layoutInflater=LayoutInflater.from(parent.context)
                val binding=AsteroidItemBinding.inflate(layoutInflater,parent,false)
                return AsteroidViewHolder(binding)
            }
        }
            }

}

class AsteroidDiffCallback:DiffUtil.ItemCallback<Asteroid>(){
    override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem==newItem
    }
}