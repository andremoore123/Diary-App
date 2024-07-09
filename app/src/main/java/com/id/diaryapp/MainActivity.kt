package com.id.diaryapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.id.diaryapp.databinding.ActivityMainBinding
import com.id.diaryapp.ui.adapter.MainFragmentAdapter
import com.id.diaryapp.ui.completed.CompletedFragment
import com.id.diaryapp.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initView()

    }

    private fun initView() {
        val fragments = listOf(
            HomeFragment(), CompletedFragment()
        )
        val fragmentTitles = listOf(
            resources.getString(R.string.text_home),
            resources.getString(R.string.text_completed),
        )

        val fragmentStateAdapter = MainFragmentAdapter(fragments, this)

        with(binding) {
            amViewPager.adapter = fragmentStateAdapter
            TabLayoutMediator(amTabLayout, amViewPager) {tab, position ->
                tab.text = fragmentTitles[position]
            }.attach()
        }
    }
}