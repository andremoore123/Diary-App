package com.id.diaryapp.ui.add

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.id.diaryapp.base.BaseDialog
import com.id.diaryapp.databinding.AddNoteLayoutBinding
import com.id.diaryapp.util.toFormattedDateWithYear

class AddNoteDialog(
    private val context: Context,
    private val supportFragment: FragmentManager,
    private val handleOnAddNote: (String, String, Long, Boolean) -> Unit
) : BaseDialog<AddNoteLayoutBinding>(
    context = context, AddNoteLayoutBinding::inflate
) {
    private val datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select Date")
        .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build()

    private var selectedDate: Long? = null

    override fun initView() {
        with(binding) {
            anBtCancel.setOnClickListener {
                dismiss()
            }

            anBtDone.setOnClickListener {
                handleAddNote()
            }

            anEtDate.setOnClickListener {
                handleDatePicker()
            }
        }
    }

    private fun handleDatePicker() {
        datePicker.show(supportFragment, "TAG")
        selectedDate = datePicker.selection
        binding.anEtDate.setText(datePicker.selection?.toFormattedDateWithYear())
    }

    private fun handleAddNote() {
        with(binding) {
            val title = anEtTitle.text.toString()
            val desc = anEtDesc.text.toString()
            val time = selectedDate
            val isNotify = anCbNotify.isChecked

            if (validator(title, desc, time)) {
                handleOnAddNote.invoke(title, desc, time!!, isNotify)
                dismiss()
            } else {
                Toast.makeText(context, "Please fill all fields correctly", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun validator(title: String, desc: String, time: Long?): Boolean {
        return title.isNotEmpty() && desc.isNotEmpty() && time != null
    }
}