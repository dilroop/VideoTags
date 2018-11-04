package com.dsb.videotags

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.dsb.videotags.databinding.FragmentVideoTagsBinding
import kotlinx.android.synthetic.main.fragment_video_tags.*

class FetchVideoTagsFragment : Fragment() {

    private lateinit var binding: FragmentVideoTagsBinding

    private val viewModel by lazy {
        ViewModelProviders.of(activity!!).get(FetchVideoTagsViewModel::class.java)
    }

    companion object {
        private const val COPY_TAG_SEPARATOR = ", "
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentVideoTagsBinding.inflate(inflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.run {
            setLifecycleOwner(viewLifecycleOwner)
            viewModel = this@FetchVideoTagsFragment.viewModel
            clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        }

        copy.setOnClickListener {
            val tagsData = viewModel.tags.value
            tagsData?.let { tags ->
                val clipManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip: ClipData = ClipData.newPlainText(
                        "Tags",
                        tags.joinToString(separator = COPY_TAG_SEPARATOR) { tag -> tag }
                )
                clipManager.primaryClip = clip
                Toast.makeText(requireContext(),"Tags Copied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
