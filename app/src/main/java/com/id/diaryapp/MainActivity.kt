package com.id.diaryapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.id.diaryapp.databinding.ActivityMainBinding
import com.id.diaryapp.ui.adapter.MainFragmentAdapter
import com.id.diaryapp.ui.adapter.RVHomeAdapter
import com.id.diaryapp.ui.add.AddNoteDialog
import com.id.diaryapp.ui.completed.CompletedFragment
import com.id.diaryapp.ui.home.HomeFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.scope.Scope

class MainActivity : AppCompatActivity(), AndroidScopeComponent {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()
    private lateinit var rvAdapter: RVHomeAdapter
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
        observeData()
    }

    private fun initView() {
        rvAdapter = RVHomeAdapter(
            onCheckClick = {
                viewModel.updateNoteStatus(it)
            },
            onDetailClick = {}
        )
        with(binding) {
            amRvNotes.apply {
                adapter = rvAdapter
                layoutManager = LinearLayoutManager(this@MainActivity)
            }
        }
    }

    private fun observeData() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.listNote.collect {
                rvAdapter.submitData(it)
            }
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