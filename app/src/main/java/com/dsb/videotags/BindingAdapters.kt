package com.dsb.videotags

import android.view.View
import android.webkit.URLUtil
import android.widget.Button
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService




object FetchVideoTagsBindingAdapters {

    @JvmStatic
    @BindingAdapter("enableOnLink")
    fun enableButtonOnValidLink(view: Button, link: MutableLiveData<String>) {
        val value = link.value
        if (value != null) {
            view.isEnabled = checkUrl(value)
        } else {
            view.isEnabled = false
        }
    }

    @JvmStatic
    @BindingAdapter("visibleIfText")
    fun visibleOnlyIfTextEntered(view: Button, link: MutableLiveData<String>) {
        val value = link.value
        if (value != null && value.isNotEmpty()) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.INVISIBLE
        }
    }

    private fun checkUrl(value: String): Boolean {
        return URLUtil.isValidUrl(value)
    }

    @JvmStatic
    @BindingAdapter(value = ["tags", "clipboard"], requireAll = true)
    fun loadTags(chipGroup: ChipGroup, data: MutableLiveData<List<String>>, clipboard: ClipboardManager) {
        val list = data.value
        chipGroup.removeAllViews()
        list?.let { tags ->
            tags.forEachIndexed { index, tag ->
                val chip = Chip(chipGroup.context)
                chip.text = tag
                chip.setOnClickListener {
                    val clip: ClipData = ClipData.newPlainText("Tag", tag)
                    clipboard.primaryClip = clip
                    Toast.makeText(chipGroup.context,"Tag Copied $tag", Toast.LENGTH_SHORT).show()
                }
                chipGroup.addView(chip, index)
            }
        }
    }

}