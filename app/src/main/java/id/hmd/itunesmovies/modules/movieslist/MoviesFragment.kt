package id.hmd.itunesmovies.modules.movieslist

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.marginRight
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.github.ybq.android.spinkit.SpinKitView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import id.hmd.itunesmovies.R
import id.hmd.itunesmovies.adapter.MoviesListAdapter
import id.hmd.itunesmovies.base.BaseFragment
import id.hmd.itunesmovies.databinding.FragmentMoviesListBinding
import id.hmd.itunesmovies.model.response.ResultsItem
import id.hmd.itunesmovies.modules.movies.MoviesContract
import id.hmd.itunesmovies.modules.movies.MoviesPresenter
import id.hmd.itunesmovies.modules.movies.moviesdetail.MoviesDetailActivity
import id.hmd.itunesmovies.modules.movies.moviessearch.MoviesSearchActivity
import id.hmd.itunesmovies.utils.AppConstants
import id.hmd.itunesmovies.utils.changePage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MoviesFragment : BaseFragment(), MoviesContract.View, View.OnClickListener,
    MoviesListAdapter.OnItemClick {

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var binding: FragmentMoviesListBinding

    private val mPresenter: MoviesContract.Presenter = MoviesPresenter(this)

    //load more on scrollin param
    private var visibleItemCount: Int? = null
    private var totalItemCount: Int? = null
    private var pastVisiblesItems: Int? = null

    private lateinit var mMoviesListAdapter: MoviesListAdapter

    private var isLoadMore: Boolean = false
    private var isLoading: Boolean = false

    private val mArrayOfListProduct = ArrayList<ResultsItem>()

    private var selectedProductCategory = ArrayList<String>()
    private var selectedMerchant = ArrayList<String>()

    private var isLoadFromCategory: Boolean = false

    private var page: Int = 0
    private var itemLimit: Int = 10

    private lateinit var srlMovies: SwipeRefreshLayout
    private lateinit var nsvMovies: NestedScrollView
    private lateinit var rvMoviesList: RecyclerView
    private lateinit var shimmerMoviesList: View
    private lateinit var layoutMoviesEmpty: View
    private lateinit var btnLayoutMoviesEmpty: Button
    private lateinit var etSearch: TextInputEditText
    private lateinit var appbarScrollParams: AppBarLayout.LayoutParams
    private lateinit var tvHeaderTitle: TextView
    private lateinit var clHeader: ConstraintLayout
    private lateinit var ctlHeader: CollapsingToolbarLayout
    private lateinit var ablHeader: AppBarLayout
    private lateinit var tilSearchTitle: TextInputLayout
    private lateinit var skLoading : SpinKitView

    private var searchTitle: String = ""


    private lateinit var mContext : Context



    override fun getViewBind(container: ViewGroup?): ViewBinding {
        binding = FragmentMoviesListBinding.inflate(layoutInflater,container,false)
        return binding
    }

    override fun onBindView() {

        mContext = requireContext()

        resultLauncher = registerForActivityResult(
            ActivityResultContracts
            .StartActivityForResult()){
            if (it.resultCode == Activity.RESULT_OK){
                val data:Intent? = it.data
                if (data != null) {
                    if (!data.getStringExtra(AppConstants.FILTER_PRODUCT_TITLE).isNullOrEmpty()) {
                        searchTitle = data.getStringExtra(AppConstants.FILTER_PRODUCT_TITLE) ?: ""

                        binding.etMovieslistSearchTitle.setText(searchTitle)
                        startShimmer(rvMoviesList, shimmerMoviesList).apply {
                            layoutMoviesEmpty.visibility = View.GONE
                        }
                        mArrayOfListProduct.removeAll(mArrayOfListProduct.toSet())

                        isLoadMore = false
                        addDataToList()
                    }
                }
            }
        }

        initLayout()
        initData()
        initListMoviesAdapter()

    }

    private fun initData() {
        addDataToList()
    }

    @SuppressLint("SetTextI18n")
    private fun initLayout() {
        tvHeaderTitle = binding.tvMovieslistHeadertitle
        ablHeader = binding.ablMovieslistHeader
        ctlHeader = binding.ctlMovieslistHeader
        layoutMoviesEmpty = binding.layoutNotfoundMovieslist.root
        btnLayoutMoviesEmpty = binding.layoutNotfoundMovieslist.btnEmptysearchReset
        clHeader = binding.clMovieslistHeader
        skLoading = binding.spinKitMovieslist

        appbarScrollParams = ctlHeader.layoutParams as AppBarLayout.LayoutParams

        //init shimmer for movies list
        rvMoviesList = binding.rvMovieslistList
        shimmerMoviesList = binding.layoutShimmerMovieslist.root
        startShimmer(rvMoviesList, shimmerMoviesList).apply {
            layoutMoviesEmpty.visibility = View.GONE
        }

        etSearch = binding.etMovieslistSearchTitle
        etSearch.setOnClickListener(this)
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                p0?.let {
                    if (it.toString().isEmpty()) {
                        searchTitle = ""
                        isLoadFromCategory = false
                        startShimmer(rvMoviesList, shimmerMoviesList).apply {
                            layoutMoviesEmpty.visibility = View.GONE
                        }
                        isLoadMore = false
                        addDataToList()
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        //Load more list
        nsvMovies = binding.nsvMovieslistItem
        nsvMovies.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, oldScrollY ->
            if (v.getChildAt(v.childCount - 1) != null) {
                if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight &&
                    scrollY > oldScrollY
                ) {
                    visibleItemCount = rvMoviesList.childCount
                    totalItemCount = rvMoviesList.layoutManager?.itemCount
                    pastVisiblesItems =
                        (rvMoviesList.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                    if (!isLoading) {
                        isLoading = true
                        if (isLoadMore) {
                            if (visibleItemCount!! + pastVisiblesItems!! >= totalItemCount!!) {
                                page += 10
                                skLoading.visibility = View.VISIBLE

                                addDataToList()
                            }
                        } else {
                            page = 0
//                            rvMoviesList.visibility = View.VISIBLE.apply {
//                                layoutMoviesEmpty.visibility = View.GONE
//                            }
                            if(rvMoviesList.visibility==View.VISIBLE){
                                layoutMoviesEmpty.visibility=View.GONE
                            }
                        }
                    }
                }
            }
        })

        //Refresh list from start
        srlMovies = binding.srlMovieslist
        srlMovies.setProgressViewEndTarget(false, 0)
        srlMovies.setOnRefreshListener {
            isLoadMore = false
            startShimmer(rvMoviesList, shimmerMoviesList).apply {
                layoutMoviesEmpty.visibility = View.GONE
            }
            addDataToList()
        }



        btnLayoutMoviesEmpty.setOnClickListener {

            selectedMerchant = ArrayList()

            if (searchTitle.isEmpty()) {
                startShimmer(rvMoviesList, shimmerMoviesList).apply {
                    layoutMoviesEmpty.visibility = View.GONE
                }
                addDataToList()
            } else {
                searchTitle = ""
                etSearch.setText("")
            }


        }

        ///////////////////
        //custom collapsing
        var titleWidth = 0
        var titleMarginRight = 0
        var desiredMargin = 0
        var searchMaxHeight = 0
        var searchMinHeight = 0
        var spareHeight = 0

        val scale = mContext.resources.displayMetrics.density
        val pixels4 = (4 * scale + 0.5f)
        val pixels46 = (46 * scale + 0.5f)

        //need to post for get actual size
        tilSearchTitle = binding.tilMovieslistSearchTitle
        tvHeaderTitle.post {
            titleMarginRight = tvHeaderTitle.marginRight
            titleWidth = tvHeaderTitle.width + pixels4.toInt()
            //number that we get is not in dp, thays why need to get margin too
            desiredMargin = tvHeaderTitle.width + titleMarginRight + pixels4.toInt()
            searchMaxHeight = pixels46.toInt()
            searchMinHeight = tilSearchTitle.minimumHeight
            spareHeight = searchMaxHeight - searchMinHeight
        }


        ablHeader.addOnOffsetChangedListener { appBarLayout, y ->
            ablHeader.post {
                val offsetPercentage: Float =
                    (ablHeader.totalScrollRange + appBarLayout.y) / ablHeader.totalScrollRange
                val constraintSet = ConstraintSet()
                constraintSet.clone(clHeader)
                val biasHorPercentage: Float
                val biasVerPercentage: Float
                val searchMargin: Int
                val searchHeight: Int

                if (y == 0) {
                    biasHorPercentage = 0.5f
                    biasVerPercentage = 0.0f
                    searchMargin = titleMarginRight
                    searchHeight = searchMaxHeight
                } else if (ablHeader.totalScrollRange + y == 0) {
                    biasHorPercentage = 0.0f
                    biasVerPercentage = 1.0f
                    searchMargin = desiredMargin
                    searchHeight = searchMinHeight
                } else {
                    biasHorPercentage = 0.5f - ((1.0f - offsetPercentage) / 2)
                    biasVerPercentage = 1.0f - offsetPercentage
                    searchMargin =
                        (titleWidth - (titleWidth * offsetPercentage) + titleMarginRight).toInt()
                    searchHeight =
                        searchMaxHeight - (spareHeight - (spareHeight * offsetPercentage)).toInt()
                }

                constraintSet.setHorizontalBias(R.id.tv_movieslist_headertitle, biasHorPercentage)
                constraintSet.setVerticalBias(R.id.tv_movieslist_headertitle, biasVerPercentage)
                constraintSet.setMargin(
                    R.id.til_movieslist_search_title,
                    ConstraintSet.START,
                    searchMargin
                )
                constraintSet.constrainHeight(R.id.til_movieslist_search_title, searchHeight)
                constraintSet.applyTo(clHeader)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            btnLayoutMoviesEmpty -> {
                isLoadFromCategory = false

                selectedMerchant = ArrayList()
                selectedProductCategory = ArrayList()

                if (searchTitle.isEmpty()) {
                    startShimmer(rvMoviesList, shimmerMoviesList).apply {
                        layoutMoviesEmpty.visibility = View.GONE
                    }
                    addDataToList()
                } else {
                    searchTitle = ""
                    etSearch.setText("")
                }

            }

            etSearch -> {
                val intent = Intent(activity, MoviesSearchActivity::class.java)
                //startActivityForResult(intent, AppConstants.CATALOG_SEARCH)
                resultLauncher.launch(intent)
            }
        }
    }

    private fun startShimmer(rv: RecyclerView, shimmer: View) {
        rv.visibility = View.GONE.apply {
            shimmer.visibility = View.VISIBLE
        }
    }

    private fun stopShimmer(rv: RecyclerView, shimmer: View) {
        if(rv.visibility==View.VISIBLE){
            shimmer.visibility = View.GONE
        }
        srlMovies.isRefreshing = false
    }

    private fun initListMoviesAdapter() {
        mMoviesListAdapter = MoviesListAdapter(mArrayOfListProduct)
        mMoviesListAdapter.interfaceInstance = this
        rvMoviesList.adapter = mMoviesListAdapter

    }

    private fun addDataToList() {
        //layoutMoviesEmpty.visibility = View.GONE
        if (!isLoadMore) {
            page = 0
        }
        lifecycleScope.launch(coroutineExcHandler){
            delay(900)
            mPresenter.getMoviesList()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onSuccessgetMoviesList(moviesList: List<ResultsItem>?) {
        if (layoutMoviesEmpty.visibility == View.VISIBLE) {
            layoutMoviesEmpty.visibility = View.GONE
        }
        isLoading = false
        if (!isLoadMore) {
            mArrayOfListProduct.clear().apply {
                moviesList?.let {
                    mArrayOfListProduct.addAll(it).apply {
                        val layoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
                        rvMoviesList.layoutManager = layoutManager
                        mMoviesListAdapter = MoviesListAdapter(mArrayOfListProduct)
                        mMoviesListAdapter.notifyDataSetChanged()
                        stopShimmer(rvMoviesList, shimmerMoviesList)
                    }
                }

            }

        } else {
            //page+=10
            moviesList?.let { mArrayOfListProduct.addAll(it) }
            val dist = mArrayOfListProduct.distinctBy { i -> i.trackId }.toList()
            if(dist.size < mArrayOfListProduct.size) {
                mArrayOfListProduct.clear()
                mArrayOfListProduct.addAll(dist)
            }
            val layoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
            rvMoviesList.layoutManager = layoutManager
            mMoviesListAdapter = MoviesListAdapter(mArrayOfListProduct)
            mMoviesListAdapter.notifyItemRangeChanged(
                (mArrayOfListProduct.size - 1) - itemLimit,
                mArrayOfListProduct.size
            )
            skLoading.visibility = View.GONE
        }

        setupScrollParams(true)
        isLoadMore = true
    }

    private fun setupScrollParams(isSuccess: Boolean) {
        if (isSuccess) {
            if (mArrayOfListProduct.size <= 2) {
                appbarScrollParams.scrollFlags = 0  // clear all scroll flags
            } else {
                appbarScrollParams.scrollFlags =
                    SCROLL_FLAG_SCROLL or SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
            }
        } else {
            appbarScrollParams.scrollFlags = 0  // clear all scroll flags
        }
    }

    override fun onFailGetListDataMovies() {

        isLoading = false
        stopShimmer(rvMoviesList, shimmerMoviesList).apply {
            if (!isLoadMore) {
                if(layoutMoviesEmpty.visibility==View.VISIBLE){
                    rvMoviesList.visibility = View.GONE
                    isLoadMore = false
                }
                setupScrollParams(false)
            } else {
                if (rvMoviesList.visibility == View.VISIBLE) {
                    layoutMoviesEmpty.visibility = View.GONE
                    skLoading.visibility = View.GONE
                }
            }
        }
        //
        page = 0

    }

    override fun onListMoviesClick(obj: ResultsItem) {
        activity?.changePage(MoviesDetailActivity::class.java, Bundle().apply {
            this.putString(AppConstants.PRODUCT_CATALOG_ID, obj.trackId.toString())
        })
    }


    override fun onRetryClicked() {
        println("onRetryClicked Fragment")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rvMoviesList.adapter = null
    }

}