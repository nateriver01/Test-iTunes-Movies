package id.hmd.itunesmovies.modules.myfav

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import id.hmd.itunesmovies.adapter.MyFavListAdapter
import id.hmd.itunesmovies.base.BaseFragment
import id.hmd.itunesmovies.databinding.FragmentMyfavBinding
import id.hmd.itunesmovies.model.response.ResultsItem
import id.hmd.itunesmovies.modules.movies.moviesdetail.MoviesDetailActivity
import id.hmd.itunesmovies.utils.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber


/**
 * Created by hmdrrhmn on 2019-11-18 at 12:47.
 */
class MyFavFragment : BaseFragment(), MyFavContract.View, MyFavListAdapter.OnItemClick,
    View.OnClickListener {

    private lateinit var myfavListAdapter: MyFavListAdapter
    private lateinit var binding: FragmentMyfavBinding

    private lateinit var srlMyFavList: SwipeRefreshLayout
    private lateinit var rvMyFavList: RecyclerView
    private lateinit var ivBtnHistory: ImageView
    private lateinit var ivReddot: ImageView
    private lateinit var layoutMyFavEmpty: View
    private lateinit var btnMyFavEmpty: Button
    private lateinit var shimmerMyFav: View

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private var isLoadMore: Boolean = false

    private var skip: Int = 0
    private var itemLimit: Int = 10

    private var listMyFav = ArrayList<ResultsItem>()
    private var loadingItem = ResultsItem()


    override fun getViewBind(container: ViewGroup?): ViewBinding {
        binding = FragmentMyfavBinding.inflate(layoutInflater,container,false)
        return binding
    }

    override fun onBindView() {
        initLayout()
        initAdapter()
        initData()
        initListener()
    }

    private fun initLayout() {
        srlMyFavList = binding.srlMyfavlist
        rvMyFavList = binding.rvMyfavlistList
        layoutMyFavEmpty = binding.layoutNotfoundMyfavlist.root
        btnMyFavEmpty = binding.layoutNotfoundMyfavlist.btnEmptymyfavExchange
        shimmerMyFav = binding.layoutShimmerMyfavlist.root

        srlMyFavList.setProgressViewEndTarget(false, 0)
    }

    private fun initAdapter() {
        myfavListAdapter = MyFavListAdapter(listMyFav)
        linearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rvMyFavList.layoutManager = linearLayoutManager
        myfavListAdapter.interfaceInstance = this
        rvMyFavList.adapter = myfavListAdapter
    }

    private fun initListener() {
        ivBtnHistory.setOnClickListener(this)
        btnMyFavEmpty.setOnClickListener(this)

        scrollListener = object : EndlessRecyclerViewScrollListener(2, linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                Timber.tag("TEST").d("onLoadMore $page - $totalItemsCount")
                insertLoadingItem()
                hitApi(false, page)
            }
        }
        rvMyFavList.addOnScrollListener(scrollListener)

        srlMyFavList.setOnRefreshListener {
            hitApi(true)
        }
    }

    private fun initData() {
        //getOrderReadCount()
        hitApi(true)
    }

    private fun hitApi(isInitial: Boolean, page: Int = 0) {
        layoutMyFavEmpty.visibility = View.GONE
        if (isInitial) {
            skip = 0
            isLoadMore = false
            startShimmer(rvMyFavList, shimmerMyFav)
        } else {
            skip = itemLimit * page
            isLoadMore = true
        }

        lifecycleScope.launch(coroutineExcHandler){
            delay(900)
        }

    }

    override fun onListMyFavClick(obj: ResultsItem) {
        activity?.changePage(MoviesDetailActivity::class.java, Bundle().apply {
            this.putString(AppConstants.PRODUCT_CATALOG_ID, obj.trackId.toString())
        })
    }

    override fun onSuccessGetMyFavList(myfavList: List<ResultsItem>) {
        if (!isLoadMore) {
            if (!listMyFav.isEmpty()) listMyFav.clear()
            listMyFav.addAll(myfavList)
            myfavListAdapter.notifyDataSetChanged()
            stopShimmer(rvMyFavList, shimmerMyFav)
        } else {
            removeLoadingItem()

            listMyFav.addAll(myfavList)
            myfavListAdapter.notifyItemRangeChanged(
                (listMyFav.size - 1) - itemLimit,
                myfavList.size
            )
        }
        isLoadMore = true
    }

    override fun onFailedGetMyFavList() {
        stopShimmer(rvMyFavList, shimmerMyFav)
        if (!isLoadMore) {
            rvMyFavList.visibility = View.GONE
            layoutMyFavEmpty.visibility = View.VISIBLE
        } else {
            removeLoadingItem()
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

    private fun insertLoadingItem() {
        loadingItem.isLoading = true
        listMyFav.add(loadingItem)
        myfavListAdapter.notifyItemInserted(listMyFav.size - 1)
    }

    private fun removeLoadingItem() {
        listMyFav.removeAt(listMyFav.lastIndex)
        val scrollPosition: Int = listMyFav.size
        myfavListAdapter.notifyItemRemoved(scrollPosition)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        rvMyFavList.adapter = null
    }

    override fun onSuccessOrderCount(count: Int) {
        if(count > 0){
            ivReddot.visibility = View.VISIBLE
        }else {
            ivReddot.visibility = View.INVISIBLE
        }
    }

    override fun onFailOrderCount() {
        ivReddot.visibility = View.INVISIBLE
    }



    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

}