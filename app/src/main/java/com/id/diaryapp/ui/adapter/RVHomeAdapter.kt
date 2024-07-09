package com.id.diaryapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.id.diaryapp.databinding.RvHomeNoteItemBinding
import com.id.diaryapp.domain.NoteModel
import com.id.diaryapp.util.toFormattedDate

class RVHomeAdapter(
    private val onCheckClick: (NoteModel) -> Unit,
    private val onDetailClick: (NoteModel) -> Unit,
    private val handleOnEmptyList: () -> Unit = {},
    private val handleOnSuccess: () -> Unit = {}
): Adapter<RVHomeAdapter.RVHomeViewHolder>() {
    private val noteList = mutableListOf<NoteModel>()

    inner class RVHomeViewHolder(val binding: RvHomeNoteItemBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHomeViewHolder {
        val binding = RvHomeNoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RVHomeViewHolder(binding)
    }

    fun submitData(data: List<NoteModel>) {
        noteList.clear()
        noteList.addAll(data)
        if (data.isEmpty()) {
            handleOnEmptyList()
        } else {
            handleOnSuccess()
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = noteList.size
    override fun onBindViewHolder(holder: RVHomeViewHolder, position: Int) {
        val item = noteList[position]
        with(holder.binding) {
            rvhTvTitle.text = item.title
            rvhTvDesc.text = item.description
            rvhCbDone.isChecked = item.isDone
            rvhTvDate.text = item.date.toFormattedDate()

            rvhCbDone.setOnClickListener {
                onCheckClick(item)
            }

            root.setOnClickListener {
                onDetailClick(item)
            }
        }
    }
}