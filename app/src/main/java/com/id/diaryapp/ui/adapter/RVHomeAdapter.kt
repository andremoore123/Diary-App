package com.id.diaryapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import com.id.diaryapp.databinding.RvHomeNoteItemBinding
import com.id.diaryapp.domain.NoteModel

class RVHomeAdapter: Adapter<RVHomeAdapter.RVHomeViewHolder>() {
    private val noteList = mutableListOf<NoteModel>()

    inner class RVHomeViewHolder(val binding: RvHomeNoteItemBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHomeViewHolder {
        val binding = RvHomeNoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RVHomeViewHolder(binding)
    }

    fun submitData(data: List<NoteModel>) {
        noteList.clear()
        noteList.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = noteList.size
    override fun onBindViewHolder(holder: RVHomeViewHolder, position: Int) {
        val item = noteList[position]
        with(holder.binding) {
            rvhTvTitle.text = item.title
            rvhTvDesc.text = item.description
            rvhCbDone.isChecked = item.isDone
        }
    }
}