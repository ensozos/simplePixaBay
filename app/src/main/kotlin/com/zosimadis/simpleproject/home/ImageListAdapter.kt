package com.zosimadis.simpleproject.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.zosimadis.database.model.ImageEntity
import com.zosimadis.simpleproject.databinding.ItemImageBinding

class ImageListAdapter(
    private val onItemClick: (ImageEntity) -> Unit,
) : PagingDataAdapter<ImageEntity, ImageViewHolder>(ImageDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return ImageViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}

class ImageViewHolder(
    private val binding: ItemImageBinding,
    private val onItemClick: (ImageEntity) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(image: ImageEntity) {
        with(binding) {
            imageViewThumbnail.load(image.previewUrl) {
                crossfade(true)
                placeholder(android.R.drawable.ic_menu_gallery)
                error(android.R.drawable.ic_menu_report_image)
            }

            textViewUsername.text = image.user
            root.setOnClickListener { onItemClick(image) }
        }
    }
}

private object ImageDiffCallback : DiffUtil.ItemCallback<ImageEntity>() {
    override fun areItemsTheSame(oldItem: ImageEntity, newItem: ImageEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ImageEntity, newItem: ImageEntity): Boolean {
        return oldItem == newItem
    }
}
