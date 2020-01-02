package com.kho.poc.expandabletext.v2

import android.util.Log
import android.view.ViewManager
import android.widget.TextView
import com.kho.poc.expandabletext.BuildConfig

// Public extensions


// Internal extensions

internal inline val TextView.isEllipsized get() = layout.getEllipsisCount(lineCount - 1) > 0

internal fun log(message: String) {
	if (BuildConfig.DEBUG)
		Log.d("ExpandableTextView", message)
}

internal fun doNothing() {}
