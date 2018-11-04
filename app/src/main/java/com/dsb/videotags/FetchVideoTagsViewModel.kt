package com.dsb.videotags

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.launch
import java.lang.Exception
import java.net.URL

class FetchVideoTagsViewModel(application: Application) : AndroidViewModel(application) {

    val tags = MutableLiveData<List<String>>()
    val link = MutableLiveData<String>()
    val tagsReady = MutableLiveData<Boolean>()

    companion object {
        private const val FIND_IN_SOURCE = "\"keywords\":\""
        private const val SINGLE_QUOTE = "\""
        private const val SEPARATOR = ","
    }

    init {
        tagsReady.postValue(false)
    }

    fun loadUrl(urlData: MutableLiveData<String>) {
        tagsReady.postValue(false)
        launch {
            val url = urlData.value
            url?.let {
                try {
                    val jsonStr = URL(it).readText()
                    val startOfKeywords = jsonStr.indexOf(FIND_IN_SOURCE, ignoreCase = true)
                    val tagsStartFrom = startOfKeywords + FIND_IN_SOURCE.length
                    val endOfKeywords = jsonStr.indexOf(SINGLE_QUOTE, tagsStartFrom, ignoreCase = true)
                    val videoTags = jsonStr.subSequence(tagsStartFrom, endOfKeywords).toString()

                    val list = videoTags.split(SEPARATOR)
                    if (list.isNotEmpty()) {
                        tags.postValue(list)
                    }
                    tagsReady.postValue(true)
                } catch (exception : Exception) {
                    exception.printStackTrace()
                    tagsReady.postValue(false)
                }
            }
        }
    }

    fun clearLink() {
        link.postValue("")
        tagsReady.postValue(false)
    }

}
