package com.id.diaryapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.id.diaryapp.databinding.ActivityMainBinding
import com.id.diaryapp.domain.NoteModel
import com.id.diaryapp.ui.adapter.RVHomeAdapter
import com.id.diaryapp.ui.dialog.AddNoteDialog
import com.id.diaryapp.ui.dialog.UpdateNoteDialog
import com.id.diaryapp.util.gone
import com.id.diaryapp.util.visible
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

    private val requiredPermissions = arrayOf(
        Manifest.permission.POST_NOTIFICATIONS,
    )

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val deniedPermissions = permissions.filterValues { !it }.keys
        if (deniedPermissions.isNotEmpty()) {
            Toast.makeText(this, "Permissions denied: $deniedPermissions", Toast.LENGTH_SHORT)
                .show()
        }
    }

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
        if (!arePermissionsGranted()) {
            requestPermissions()
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
            onDetailClick = {
                onItemClick(it)
            },
            handleOnEmptyList = {
                handleEmptyState()
            },
            handleOnSuccess = {
                handleSuccessState()
            }
        )
        with(binding) {
            amRvNotes.apply {
                adapter = rvAdapter
                layoutManager = LinearLayoutManager(this@MainActivity)
            }

            amMtToolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.show_all -> {
                        viewModel.setFilter(Filter.NoFilter)
                        true
                    }

                    R.id.show_unfinished -> {
                        viewModel.setFilter(Filter.UnfinishedFilter)
                        true
                    }

                    R.id.show_finished -> {
                        viewModel.setFilter(Filter.FinishedFilter)
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun observeData() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.listNote.collect { list ->
                viewModel.filter.observe(this@MainActivity) { filter ->
                    handleTitleAndDesc(filter)
                    when (filter) {
                        Filter.NoFilter -> {
                            rvAdapter.submitData(list)
                        }

                        Filter.FinishedFilter -> {
                            rvAdapter.submitData(list.filter {
                                it.isDone
                            })
                        }

                        Filter.UnfinishedFilter -> {
                            rvAdapter.submitData(list.filterNot {
                                it.isDone
                            })
                        }
                    }
                }
            }
        }
    }

    private fun handleTitleAndDesc(filter: Filter) {
        with(binding) {
            amTvTitle.text = resources.getString(filter.title)
            amTvDesc.text = resources.getString(filter.desc)
        }
    }

    private fun initListener() {
        binding.amFaAdd.setOnClickListener {
            onAddButtonClick()
        }
    }

    private fun handleEmptyState() {
        with(binding) {
            amRvNotes.gone()
            amTvDesc.gone()
            amTvEmptyState.visible()
        }
    }

    private fun handleSuccessState() {
        with(binding) {
            amRvNotes.visible()
            amTvDesc.visible()
            amTvEmptyState.gone()
        }
    }

    private fun onItemClick(note: NoteModel) {
        val dialog = UpdateNoteDialog(context = this, supportFragment = supportFragmentManager, note = note,
            handleOnUpdateNote = { s: String, s1: String, l: Long, b: Boolean ->
                viewModel.updateNote(
                    note = note,
                    title = s,
                    desc = s1,
                    date = l,
                    notify = b,
                )
            },
            handleOnDeleteNote = {
                viewModel.deleteNoteByID(it)
            }
        )
        dialog.show()
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

    private fun arePermissionsGranted(): Boolean {
        for (permission in requiredPermissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    private fun requestPermissions() {
        requestPermissionLauncher.launch(requiredPermissions)
    }

    override val scope: Scope by activityScope()
}