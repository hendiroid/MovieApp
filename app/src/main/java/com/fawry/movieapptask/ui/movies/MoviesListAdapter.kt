package com.fawry.movieapptask.ui.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fawry.movieapptask.R
import com.fawry.movieapptask.business.models.Item
import com.fawry.movieapptask.databinding.ItemMovieBinding
import com.fawry.movieapptask.util.printLogD
import javax.inject.Inject

class MoviesListAdapter
@Inject constructor(
    private val interaction: Interaction? = null,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val itemCallback = object : DiffUtil.ItemCallback<Item>() {

        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, itemCallback)
    private lateinit var binding: ItemMovieBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.item_movie, parent,
            false
        )
        val width = parent.context.resources.displayMetrics?.widthPixels

        val params =
            RecyclerView.LayoutParams(
                (width!! * 0.8).toInt(),
                RecyclerView.LayoutParams.MATCH_PARENT
            )
        params.setMargins(30, 30, 30, 30)
        binding.root.layoutParams = params
        return MoviesViewHolder(interaction, binding)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MoviesViewHolder -> {
                binding.model = differ.currentList[position]
                binding.rate = differ.currentList[position].vote_average / 2
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Item>) {
        val commitCallback = Runnable {
            // if process died must restore list position
            interaction?.restoreListPosition()
        }
        printLogD("listadapter", "size: ${list.size}")
        differ.submitList(list, commitCallback)
    }

    class MoviesViewHolder
    @Inject
    constructor(
        private val interaction: Interaction?,
        binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) = with(itemView) {
            setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Item)
        fun restoreListPosition()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}