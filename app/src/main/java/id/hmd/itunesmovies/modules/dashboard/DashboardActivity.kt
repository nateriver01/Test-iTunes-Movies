package id.hmd.itunesmovies.modules.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import id.hmd.itunesmovies.R
import id.hmd.itunesmovies.base.BaseActivity
import id.hmd.itunesmovies.databinding.ActivityDashboardBinding
import id.hmd.itunesmovies.databinding.ItemTabDashboardBinding
import id.hmd.itunesmovies.modules.movies.MoviesFragment
import id.hmd.itunesmovies.modules.myfav.MyFavFragment
import id.hmd.itunesmovies.utils.replace


/**
 * Created by hmdrrhmn on 2019-11-18 at 12:47.
 */
class DashboardActivity : BaseActivity(), DashboardContract.View {

    override val binding: ActivityDashboardBinding
        get() = ActivityDashboardBinding.inflate(layoutInflater)

    lateinit var itemTab:ItemTabDashboardBinding
    lateinit var instance: DashboardActivity
    lateinit var tlDashboard : TabLayout
    private lateinit var fragment:Fragment
    private lateinit var dialogExit:MaterialDialog
    private var tabAt = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true)
        {
            override fun handleOnBackPressed() {
                var tabPosition = tlDashboard.selectedTabPosition
                if(tabPosition!=0){
                    tabPosition = 0
                    getTab(tabPosition)
                }else{
                    dialogExit.show{cornerRadius(8f)}
                }
            }
        })
    }
    override fun onBindView() {
        instance = this
        itemTab = ItemTabDashboardBinding.inflate(layoutInflater)
        tlDashboard = binding.tlDashboard
        //Initialize tab
        tabAt = intent.getIntExtra("getTabAt",0)

        initLayout()
        if(tabAt!=0){
            getTab(tabAt)
        }
    }


    private fun initLayout(){
        //Set first fragment on activity created
        supportFragmentManager.replace(R.id.fl_dashboard, MoviesFragment(),false)

        initTabBar()

        initExitDialog()
    }

    @SuppressLint("InflateParams")
    private fun initTabBar(){

        //Init custom tab bar
        val dashboardTabTitle = arrayOf("Movies", "My Favie")
        //val tlDashboard = binding.tlDashboard
        for (i in dashboardTabTitle.indices) {
            val customTab = itemTab.root
            val tabTitle: String = dashboardTabTitle[i]
            itemTab.tvTabTitle.text = tabTitle

            when (tabTitle){
                "Movies" -> {
                    Glide.with(this).load(R.drawable.ic_home_active)
                        .into(itemTab.ivTabImage)
                    itemTab.tvTabTitle.setTextColor(ContextCompat
                        .getColor(this,R.color.dusk_blue))
                }
                "My Favie"->{
                    Glide.with(this).load(R.drawable.ic_catalog)
                        .into(itemTab.ivTabImage)
                }
            }

            val tab:TabLayout.Tab = tlDashboard.newTab().setCustomView(customTab)
            tlDashboard.addTab(tab)
            tlDashboard.getTabAt(i)?.tag = tabTitle
        }

        //set tab listener
        setTabDashboardListener()
    }

    //set tab listener to change fragment
    private fun setTabDashboardListener(){
        tlDashboard.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {

                when (tab.position) {
                    0 -> {
                        itemTab.tvTabTitle.setTextColor(ContextCompat
                            .getColor(instance,R.color.dusk_blue))
                        Glide.with(instance).load(R.drawable.ic_home_active)
                            .into(itemTab.ivTabImage)
                        fragment = MoviesFragment()

                        supportFragmentManager.replace(R.id.fl_dashboard, fragment,false)
                    }
                    1 -> {
                        //tabView?.tv_tab_title?.setTextColor(resources.getColor(R.color.dusk_blue))
                        itemTab.tvTabTitle.setTextColor(ContextCompat
                            .getColor(instance,R.color.dusk_blue))
                        Glide.with(instance).load(R.drawable.ic_catalog_active)
                            .into(itemTab.ivTabImage)
                        fragment = MyFavFragment()

                        supportFragmentManager.replace(R.id.fl_dashboard,fragment,false)
                    }
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

                when (tab.position) {
                    0 -> {
                        Glide.with(instance).load(R.drawable.ic_home)
                            .into(itemTab.ivTabImage)
                        itemTab.tvTabTitle.setTextColor(ContextCompat
                            .getColor(instance,R.color.brown_grey))
                        //supportFragmentManager.replace(R.id.fl_dashboard, HomeFragment(),false)
                    }
                    1 -> {
                        Glide.with(instance).load(R.drawable.ic_catalog)
                            .into(itemTab.ivTabImage)
                        itemTab.tvTabTitle.setTextColor(ContextCompat
                            .getColor(instance,R.color.brown_grey))
                        //supportFragmentManager.replace(R.id.fl_dashboard, MoviesFragment(),false)
                    }
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    @SuppressLint("SetTextI18n")
    private fun initExitDialog(){

        dialogExit = MaterialDialog(this).customView(R.layout.dialog_general, scrollable = false, noVerticalPadding = true)
        val customView = dialogExit.getCustomView()
        customView.findViewById<TextView>(R.id.tv_dialog_desc).text = "Kamu yakin ingin keluar dari aplikasi?"
        customView.findViewById<Button>(R.id.btn_dialog_negative).setOnClickListener {
            dialogExit.dismiss()
        }
        customView.findViewById<Button>(R.id.btn_dialog_positive).setOnClickListener {
            dialogExit.dismiss()
            this.finishAffinity()
            //super.onBackPressed()
            onBackPressedDispatcher.onBackPressed()
        }
    }


    //get tab selected to response a result
    private fun getTab(position: Int) {
        val tab: TabLayout.Tab? = tlDashboard.getTabAt(position)
        tab?.select()
    }


    override fun setTabLayoutGrey(on: Boolean) {
        tlDashboard.apply {
            if(on) {
                //background = resources.getDrawable(R.color.pure_black)
                background = ResourcesCompat.getDrawable(resources,R.color.pure_black,null)
                alpha = 0.24f
            } else {
                //background = resources.getDrawable(R.color.white)
                background = ResourcesCompat.getDrawable(resources,R.color.white,null)
                alpha = 1f
            }
        }
    }


}