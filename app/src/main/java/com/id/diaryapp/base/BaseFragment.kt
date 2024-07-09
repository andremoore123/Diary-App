package com.id.diaryapp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<B : ViewBinding, VM : ViewModel>(
    private val bindingFactory: (LayoutInflater, ViewGroup?, Boolean) -> B
) : Fragment() {
    protected lateinit var binding: B
    protected abstract val viewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeData()
        listenData()
        initListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = bindingFactory.invoke(inflater, container, false)
        return binding.root
    }

    open fun initView() {}
    open fun observeData() {}
    open fun initListener() {}
    open fun listenData() {}
    open fun showMessage(q: String) {
        Toast.makeText(requireContext(), q, Toast.LENGTH_SHORT).show()
    }
}