package com.id.diaryapp.ui.dialog

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.id.diaryapp.base.BaseDialog
import com.id.diaryapp.databinding.UpdateNoteLayoutBinding
import com.id.diaryapp.domain.NoteModel
import com.id.diaryapp.util.toFormattedDateWithYear

class UpdateNoteDialog(
    private val context: Context,
    private val supportFragment: FragmentManager,
    private val note: NoteModel,
    private val handleOnUpdateNote: (String, String, Long, Boolean) -> Unit,
    private val handleOnDeleteNote: (Int) -> Unit
) : BaseDialog<UpdateNoteLayoutBinding>(
    context = context, UpdateNoteLayoutBinding::inflate
) {
    private val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText("Select Date")
        .setSelection(note.date)
        .build()

    private var selectedDate: Long? = null

    override fun initView() {
        with(binding) {
            unEtTitle.setText(note.title)
            unEtDesc.setText(note.description)
            unEtDate.setText(note.date.toFormattedDateWithYear())
            unCbNotify.isChecked = note.isNotify

            unBtDelete.setOnClickListener {
                handleOnDeleteNote(note.id ?: 0)
                dismiss()
            }
            unBtDone.setOnClickListener {
                handleUpdateNote()
            }
            unBtCancel.setOnClickListener {
                dismiss()
            }
            unEtDate.setOnClickListener {
                handleDatePicker()
            }
        }
    }

    private fun handleUpdateNote() {
        with(binding) {
            val title = unEtTitle.text.toString()
            val desc = unEtDesc.text.toString()
            val time = selectedDate
            val isNotify = unCbNotify.isChecked

            if (validator(title, desc, time)) {
                handleOnUpdateNote.invoke(title, desc, time!!, isNotify)
                dismiss()
            } else {
                Toast.makeText(context, "Please fill all fields correctly", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun handleDatePicker() {
        datePicker.show(supportFragment, "TAG")
        selectedDate = datePicker.selection
        binding.unEtDate.setText(datePicker.selection?.toFormattedDateWithYear())
    }

    private fun validator(title: String, desc: String, time: Long?): Boolean {
        return title.isNotEmpty() && desc.isNotEmpty() && time != null
    }
}