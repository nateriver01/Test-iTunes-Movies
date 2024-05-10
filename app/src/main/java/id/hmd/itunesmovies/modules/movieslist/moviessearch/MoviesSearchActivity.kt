package id.hmd.itunesmovies.modules.movies.moviessearch

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.hmd.itunesmovies.adapter.MoviesSearchAdapter
import id.hmd.itunesmovies.adapter.MoviesSearchHistoryAdapter
import id.hmd.itunesmovies.base.BaseActivity
import id.hmd.itunesmovies.databinding.ActivityMoviesSearchBinding
import id.hmd.itunesmovies.model.response.ResultsItem
import id.hmd.itunesmovies.modules.movies.moviesdetail.MoviesDetailActivity
import id.hmd.itunesmovies.modules.movieslist.moviessearch.MoviesSearchContract
import id.hmd.itunesmovies.utils.AppConstants
import id.hmd.itunesmovies.utils.Helpers
import id.hmd.itunesmovies.utils.changePage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
class MoviesSearchActivity : BaseActivity(), MoviesSearchContract.View, View.OnClickListener {

    override val binding: ActivityMoviesSearchBinding
        get() = ActivityMoviesSearchBinding.inflate(layoutInflater)

    private lateinit var edtSearch: EditText
    private lateinit var tvDeleteAll: TextView
    private lateinit var layoutHistoryList: ConstraintLayout
    private lateinit var layoutProductList: ConstraintLayout
    private lateinit var tvCancel: TextView
    private lateinit var rvHistory: RecyclerView
    private lateinit var rvProduct: RecyclerView
    private lateinit var shimmerProduct: View

    private var listHistoryProduct = mutableListOf<String>()
    private var listProduct = mutableListOf<ResultsItem>()

    private lateinit var historyAdapter: MoviesSearchHistoryAdapter
    private lateinit var productAdapter: MoviesSearchAdapter

    private var minimalChar: Int = 2

    private lateinit var mPresenter: MoviesSearchContract.Presenter

    override fun onBindView() {
        getBundle()
        initView()
        initData()
    }

    private fun getBundle(){

    }

    private fun initView() {
        edtSearch = binding.etMoviessearchSearchTitle
        edtSearch.filters = arrayOf(Helpers.emojiFilter)

        layoutHistoryList = binding.clMoviessearchHistorylist
        layoutProductList = binding.clMoviessearchProductlist
        tvCancel = binding.tvMoviessearchCancel
        tvDeleteAll = binding.tvMoviessearchDeleteall
        rvHistory = binding.rvMoviessearchHistorylist
        rvProduct = binding.rvMoviessearchProductlist

        shimmerProduct = binding.layoutMoviessearchShimmer.root

        toggleHistoryProduct(true)

        rvHistory.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        historyAdapter = MoviesSearchHistoryAdapter(listHistoryProduct) {
//            edtSearch.append(it)
//            getProductData(it)
            setActivityResult(it)
        }
        rvHistory.adapter = historyAdapter

        rvProduct.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        productAdapter = MoviesSearchAdapter(listProduct) {
            mPresenter.saveHistoryData(listHistoryProduct, it.trackName.trim())
            changePage(MoviesDetailActivity::class.java, Bundle().apply {
                this.putString(AppConstants.PRODUCT_CATALOG_ID, it.trackId.toString())
            })
            finish()
        }
        rvProduct.adapter = productAdapter

        edtSearch.requestFocus()

        initListener()
    }

    private fun initData() {
        mPresenter = MoviesSearchPresenter(this)

        mPresenter.getHistoryData()
    }

    private fun initListener() {
        tvCancel.setOnClickListener {
            finish()
        }
        //tvCancel.setOnClickListener(this)
        tvDeleteAll.setOnClickListener(this)
        edtSearch.addTextChangedListener(object : TextWatcher {
            private var searchFor = ""

            @SuppressLint("NotifyDataSetChanged")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().trim()
                if (searchText.length > minimalChar) {
                    toggleHistoryProduct(false)
                    if (searchText == searchFor)
                        return
                    searchFor = searchText
                    GlobalScope.launch {
                        delay(500)  //debounce timeOut
                        if (searchText != searchFor)
                            return@launch
                        // do our magic here
                        runOnUiThread {
                            getProductData(searchText)
                        }
                    }
                } else if (searchText.length == 0) {
                    listProduct.clear()
                    productAdapter.notifyDataSetChanged()
                    /*if(!listHistoryProduct.isNullOrEmpty()){
                        toggleHistoryProduct(false)
                    }*/
                }
            }

            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

        })

        edtSearch.setOnEditorActionListener { view, actionId, keyEvent ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    runOnUiThread {
                        val text = view.text.toString().trim()
                        setActivityResult(text)
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun setActivityResult(text: String) {
        val intent = Intent().apply {
            if (text.isNotEmpty() && text.length > minimalChar) {
                mPresenter.saveHistoryData(listHistoryProduct, text)
                putExtra(AppConstants.FILTER_PRODUCT_TITLE, text)
            }
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun getProductData(searchText: String?) {
        startShimmer(rvProduct, shimmerProduct)
        lifecycleScope.launch(coroutineExcHandler){
            delay(900)
            searchText?.let{

            }
        }


    }

    override fun onClick(v: View?) {
        when (v) {
            tvCancel -> finish()
            tvDeleteAll -> mPresenter.deleteAllHistoryData()
        }
    }

    private fun startShimmer(rv: RecyclerView, shimmer: View) {
        rv.visibility = View.GONE
        shimmer.visibility = View.VISIBLE
    }

    private fun stopShimmer(rv: RecyclerView, shimmer: View) {
        rv.visibility = View.VISIBLE
        shimmer.visibility = View.GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onGetHistoryData(productList: List<String>?) {
        toggleHistoryProduct(true)
        if (!productList.isNullOrEmpty()) {
            listHistoryProduct.clear()
            listHistoryProduct.addAll(productList)
            historyAdapter.notifyDataSetChanged()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onSuccessSearchProduct(moviesList: List<ResultsItem>) {
        stopShimmer(rvProduct, shimmerProduct)
        listProduct.clear()
        listProduct.addAll(moviesList)
        productAdapter.notifyDataSetChanged()
    }

    override fun onFailSearchProduct() {

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onDeleteAllHistory() {
        listHistoryProduct.clear()
        historyAdapter.notifyDataSetChanged()
    }

    private fun toggleHistoryProduct(showHistory: Boolean) {
        if (showHistory) {
            layoutHistoryList.visibility = View.VISIBLE
            layoutProductList.visibility = View.GONE
        } else {
            layoutHistoryList.visibility = View.GONE
            layoutProductList.visibility = View.VISIBLE

        }
    }

}
