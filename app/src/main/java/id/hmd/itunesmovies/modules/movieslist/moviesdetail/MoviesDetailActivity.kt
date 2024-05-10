package id.hmd.itunesmovies.modules.movies.moviesdetail

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetDialog
import id.hmd.itunesmovies.R
import id.hmd.itunesmovies.base.BaseActivity
import id.hmd.itunesmovies.databinding.ActivityMoviesdetailBinding
import id.hmd.itunesmovies.databinding.SkeletonMoviesdetailBinding
import id.hmd.itunesmovies.model.response.ResultsItem
import id.hmd.itunesmovies.utils.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MoviesDetailActivity : BaseActivity(), MoviesDetailContract.View {

//    private val localPreferences = LocalPreferences.getInstance(iTunesMoviesApp.instance)

    override val binding: ActivityMoviesdetailBinding
        get() = ActivityMoviesdetailBinding.inflate(layoutInflater)
    //private val mPresenter: MoviesDetailContract.Presenter = MoviesDetailPresenter(this)
    private var productId: String = ""
    //private var product:ResultsItem= ResultsItem()
    private lateinit var ivBanner:ImageView
    private lateinit var toolbar: Toolbar
    private lateinit var toolbarTitle: TextView
    private lateinit var tvUserPoints: TextView

    private lateinit var shimmerDetail: SkeletonMoviesdetailBinding
    private lateinit var nsvMoviesDetail: NestedScrollView


    private var userPoints = "0"
    private var productPrice = "0"
    private var productName = ""

    private lateinit var mBottomSheetnotSufficient: BottomSheetDialog

    private var isExpand: BooleanArray = booleanArrayOf(true,false,false)


    override fun onBindView() {

        productId=intent.getStringExtra(AppConstants.PRODUCT_CATALOG_ID) ?: ""

        initLayout()
        initData()
        initClickListener()
    }

    private fun initData(){
        if(productId.isNotEmpty()){
            lifecycleScope.launch(coroutineExcHandler){
                delay(900)
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun initLayout(){
        ivBanner = binding.ivMoviesdetailBanner
        binding.ivMoviesdetailDropdownDesc.rotation=-180f
        toolbar = binding.tbrMoviesdetail
        toolbarTitle = binding.tvMoviesdetailTitle
        shimmerDetail = binding.shimmerMoviesdetail
        nsvMoviesDetail = binding.nscvMoviesdetail
        shimmerLoad(true)

        initDialog()

        setSupportActionBar(toolbar)

        tvUserPoints.text = userPoints+" Points"

        binding.ablMoviesdetailBanner.addOnOffsetChangedListener { appBarLayout, _ ->
            val offsetAlpha: Float = appBarLayout.y / binding.ablMoviesdetailBanner.totalScrollRange
            binding.tvMoviesdetailToolbartitle.alpha = 0 + offsetAlpha * -1
            binding.view5.alpha = 0 + offsetAlpha * -1
        }

    }

    private fun initClickListener(){

        binding.cvMoviesdetailBack.setOnClickListener {
            //onBackPressed()
            onBackPressedDispatcher.onBackPressed()
        }

        binding.clMoviesdetailDescHeader.setOnClickListener{

            binding.tvMoviesdetailDeskripsiContent.toggleVisibility()
            binding.ivMoviesdetailDropdownDesc.toggleRotation(isExpand[0])
            isExpand[0] = !isExpand[0]
        }

        binding.btnMoviesdetailSubmit.setOnClickListener {
            onLoadingButton(true)
        }


    }

    @SuppressLint("SetTextI18n")
    override fun onSuccessGetProduct(product: ResultsItem) {
        productPrice = product.trackPrice.toString()
        productName = product.trackName

        binding.tvMoviesdetailToolbartitle.text = productName
        binding.tvMoviesdetailTitle.text = productName
        binding.tvMoviesdetailItemPoints.text = productPrice+" ${product.currency}"

        val imageItem = product.artworkUrl100

        if(!imageItem.isNullOrEmpty()){
            //Add image to banner
            Glide.with(this)
                .load(imageItem)
                .placeholder(Helpers.createCircularProgress(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.img_moviesitem_placeholder)
                .into(ivBanner)
        }else{
            Glide.with(this)
                .load(R.drawable.img_moviesitem_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivBanner)
        }

        shimmerLoad(false)


    }

    @SuppressLint("InflateParams")
    private fun initDialog(){

    }

    override fun onFailRedeem() {
        onLoadingButton(false)
        mBottomSheetnotSufficient.show()
    }

    override fun onFailGetProduct() {
        onLoadingButton(false)
    }

    private fun onLoadingButton(isLoading:Boolean){
        if(isLoading){
            binding.btnMoviesdetailSubmit.visibility = View.INVISIBLE
            binding.spinKit.visibility = View.VISIBLE
        }else{
            binding.btnMoviesdetailSubmit.visibility = View.VISIBLE
            binding.spinKit.visibility = View.INVISIBLE
        }
    }

    override fun showInetError() {
        super.showInetError()
        onLoadingButton(false)
    }

    private fun shimmerLoad(isLoading:Boolean){
        if(isLoading){
            nsvMoviesDetail.visibility = View.INVISIBLE
            shimmerDetail.root.visibility = View.VISIBLE
        }else{
            nsvMoviesDetail.visibility = View.VISIBLE
            shimmerDetail.root.visibility = View.INVISIBLE
        }
    }

}