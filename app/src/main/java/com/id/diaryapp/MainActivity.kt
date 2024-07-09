package com.id.diaryapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.id.diaryapp.databinding.ActivityMainBinding
import com.id.diaryapp.ui.adapter.MainFragmentAdapter
import com.id.diaryapp.ui.add.AddNoteDialog
import com.id.diaryapp.ui.completed.CompletedFragment
import com.id.diaryapp.ui.home.HomeFragment
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.scope.Scope

class MainActivity : AppCompatActivity(), AndroidScopeComponent {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()
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
        initListener()
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

    private fun initListener() {
        binding.amFaAdd.setOnClickListener {
            onAddButtonClick()
        }
    }

    private fun onAddButtonClick() {
        val addDialog = AddNoteDialog(
            context = this,
            supportFragment = supportFragmentManager,
            handleOnAddNote = { s: String, s1: String, l: Long, b: Boolean ->
                viewModel.addNote(
                    title = s, desc = s1, date = l, notify = b
                )
            }
        )
        addDialog.show()
    }

    override val scope: Scope by activityScope()
}