package com.kamilh.findmysong.views.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kamilh.findmysong.R

data class SongViewState(
    val title: String,
    val subtitle: String,
    val smallText: String,
    val imageUrl: String?,
    val isRightCornerImage: Boolean,
    val isDivider: Boolean = true
)

class SongAdapter constructor(
    private val onClick: (Int) -> (Unit)
) : ListAdapter<SongViewState, SongAdapter.SongViewHolder>(
    object : DiffUtil.ItemCallback<SongViewState>() {
        override fun areItemsTheSame(oldItem: SongViewState, newItem: SongViewState): Boolean {
            return oldItem.title == newItem.title && oldItem.subtitle == newItem.subtitle
        }

        override fun areContentsTheSame(oldItem: SongViewState, newItem: SongViewState): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongAdapter.SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return SongAdapter.SongViewHolder(view).apply {
            itemView.setOnClickListener { onClick(adapterPosition) }
        }
    }

    override fun onBindViewHolder(holder: SongAdapter.SongViewHolder, position: Int) {
        val state = getItem(position)
        holder.titleTextView.text = state.title
        holder.subtitleTextView.text = state.subtitle
        holder.smallTextView.text = state.smallText
        holder.rightCornerImageView.isVisible = state.isRightCornerImage
        holder.dividerView.isVisible = state.isDivider
    }

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        var subtitleTextView: TextView = itemView.findViewById(R.id.subtitleTextView)
        var smallTextView: TextView = itemView.findViewById(R.id.smallTextView)
        var rightCornerImageView: ImageView = itemView.findViewById(R.id.rightCornerImageView)
        var dividerView: View = itemView.findViewById(R.id.dividerView)
    }
}
