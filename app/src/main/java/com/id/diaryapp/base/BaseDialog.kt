package com.id.diaryapp.base

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding

abstract class BaseDialog<B : ViewBinding>(
    private val context: Context,
    private val bindingFactory: (LayoutInflater) -> B
) {
    private var dialog: Dialog? = null
    protected lateinit var binding: B

    fun show() {
        binding = bindingFactory.invoke(LayoutInflater.from(context))
        dialog = Dialog(context)
        dialog?.setContentView(binding.root)
        binding.root.run {
            post {
                dialog?.window?.setLayout(
                    context.resources.displayMetrics.widthPixels,
                    measuredHeight + 100
                )
            }
        }

        initView()
        dialog?.show()
    }

    fun dismiss() {
        dialog?.dismiss()
        dialog = null
    }

    abstract fun initView()

}