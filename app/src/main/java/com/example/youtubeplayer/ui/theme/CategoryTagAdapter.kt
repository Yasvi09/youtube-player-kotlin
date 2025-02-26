package com.example.youtubeplayer.ui.theme

import com.example.youtubeplayer.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class CategoryTagAdapter(private val context: Context, private val tagList: List<String>) :
    RecyclerView.Adapter<CategoryTagAdapter.TagViewHolder>() {

    private var selectedPosition = 1 // "All" is selected by default

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.category_tag_item, parent, false)
        return TagViewHolder(view)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val tagName = tagList[position]
        holder.tagText.text = tagName

        if (position == selectedPosition) {
            holder.tagText.background =
                ContextCompat.getDrawable(context, R.drawable.category_tag_selected_background)
            holder.tagText.setTextColor(ContextCompat.getColor(context, R.color.youtube_tag_selected_text))
        } else {
            holder.tagText.background =
                ContextCompat.getDrawable(context, R.drawable.category_tag_background)
            holder.tagText.setTextColor(ContextCompat.getColor(context, R.color.white))
        }

        holder.itemView.setOnClickListener {
            val previousSelected = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(previousSelected)
            notifyItemChanged(selectedPosition)
        }
    }

    override fun getItemCount(): Int {
        return tagList.size
    }

    class TagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tagText: TextView = itemView.findViewById(R.id.tag_text)
    }
}
