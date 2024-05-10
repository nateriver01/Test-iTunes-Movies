@file:Suppress("PrivatePropertyName")

package id.hmd.itunesmovies.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.hmd.itunesmovies.R
import id.hmd.itunesmovies.databinding.ItemMovieslistloadingBinding
import id.hmd.itunesmovies.databinding.ItemMyfavlistBinding
import id.hmd.itunesmovies.model.response.ResultsItem
import id.hmd.itunesmovies.utils.Helpers

@Suppress("PrivatePropertyName")
class MyFavListAdapter(private val obj: List<ResultsItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPECONTENT = 1
    private val TYPELOADING = 2
    var interfaceInstance: OnItemClick? = null

    inner class LoadingViewHolder(
        mBinding: ItemMovieslistloadingBinding
    ) : RecyclerView.ViewHolder(mBinding.root)

    inner class ContentViewHolder(val binding: ItemMyfavlistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(
            item: ResultsItem,
            mContext: Context
        ) {
            binding.llMyfavlistItem.setOnClickListener {
                interfaceInstance!!.onListMyFavClick(item)
            }

            item.artworkUrl100.let {
                Glide.with(mContext).load(it)
                    .placeholder(Helpers.createCircularProgress(mContext))
                    .error(R.drawable.img_moviesitem_placeholder)
                    .into(binding.ivMyfavlistItem)
            }
            binding.tvMyfavlistItemName.text = item.trackName
            binding.tvMyfavlistItemMerchant.text = item.primaryGenreName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        if (viewType == TYPELOADING){
            val bind = ItemMovieslistloadingBinding.inflate(inflater,parent,false)
            return LoadingViewHolder(bind)
        } else {
            val bind = ItemMyfavlistBinding.inflate(inflater,parent,false)
            return ContentViewHolder(bind)
        }
    }

    override fun getItemCount(): Int {
        return obj.size
    }

    override fun getItemViewType(position: Int): Int {
        @Suppress("LiftReturnOrAssignment")
        if (obj[position].isLoading)
            return TYPELOADING
        else
            return TYPECONTENT
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) != TYPELOADING)
            (holder as ContentViewHolder).bind(obj[position], holder.itemView.context)
    }

    interface OnItemClick {
        fun onListMyFavClick(obj: ResultsItem)
    }

}