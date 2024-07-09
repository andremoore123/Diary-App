package com.id.diaryapp.ui.completed

import com.id.diaryapp.base.BaseFragment
import com.id.diaryapp.databinding.FragmentCompletedBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CompletedFragment : BaseFragment<FragmentCompletedBinding, CompletedViewModel>(
    FragmentCompletedBinding::inflate
) {
    override val viewModel: CompletedViewModel by viewModel()

}