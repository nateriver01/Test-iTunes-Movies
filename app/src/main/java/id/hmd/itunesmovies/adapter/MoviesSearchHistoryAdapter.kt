package id.hmd.itunesmovies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.hmd.itunesmovies.databinding.ItemMoviessearchBinding

class MoviesSearchHistoryAdapter (
    private val mdatas: List<String>, private val itemClick: (String) -> Unit
) : RecyclerView.Adapter<MoviesSearchHistoryAdapter.ViewHolder>(){

    inner class ViewHolder(
        val binding: ItemMoviessearchBinding,
        private val mItemClick: (String) -> Unit): RecyclerView.ViewHolder(binding.root) {
        fun bind(mProduct: String) {
            with(mProduct){
                binding.tvItemsearchTitle.text = mProduct
                binding.clItemsearchParent.setOnClickListener { mItemClick(this) }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val bind = ItemMoviessearchBinding.inflate(inflater,parent,false)
        return ViewHolder(bind, itemClick)
    }

    override fun getItemCount(): Int {
        return mdatas.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mdatas[position])
    }

}