package id.hmd.itunesmovies.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.hmd.itunesmovies.R
import id.hmd.itunesmovies.databinding.ItemMovieslistBinding
import id.hmd.itunesmovies.model.response.ResultsItem
import id.hmd.itunesmovies.utils.Helpers

class MoviesListAdapter(val obj: ArrayList<ResultsItem>) : RecyclerView.Adapter<MoviesListAdapter.ViewHolder>() {

    var interfaceInstance: OnItemClick? = null


    inner class ViewHolder(val binding: ItemMovieslistBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val bind = ItemMovieslistBinding.inflate(inflater,parent,false)

        return ViewHolder(bind)
    }

    override fun getItemCount(): Int {
        return obj.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.llMovieslistItem.setOnClickListener {
            interfaceInstance!!.onListMoviesClick(obj.get(holder.adapterPosition))
        }
        obj.get(position).artworkUrl100?.let {
            Glide.with(holder.itemView.context).load(it)
                .placeholder(Helpers.createCircularProgress(holder.itemView.context))
                .error(R.drawable.img_moviesitem_placeholder)
                .into(holder.binding.ivMovieslistItem)
        }
        holder.binding.tvMovieslistItemName.text=obj.get(position).trackName
        holder.binding.tvMovieslistItemMerchant.text=obj.get(position).primaryGenreName
        holder.binding.tvMovieslistItemPoints.text= obj.get(position).trackPrice.toString() +" ${obj.get(position).currency}"

    }

    interface OnItemClick {
        fun onListMoviesClick(obj:ResultsItem)
    }

}