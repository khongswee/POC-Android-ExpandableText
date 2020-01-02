package com.kho.poc.expandabletext

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatTextView

class ExpandableTextView : AppCompatTextView {

    companion object {
        private const val DEFAULT_SUFFIX_TEXT = "[...more]"
    }


     var onStateChangeListener: (ExpandableTextView.(oldState2: State2, newState2: State2) -> Unit)? =
        null

    private val allText: String by lazy { text.toString() }
    private var suffixInit = ""
    private var maxLinesInit = 0

    // Public properties with protected setters

    /** Current [State2] of this [ExpandableTextView]. Read-only property. [Static] by default. */
    var state = State2.Static
        set(value) {
            if (field != value) {
                Log.d("ExpandableTextView", "$field -> $value")
                onStateChangeListener?.let { it(field, value) }
                field = value
            }
            checkState()

        }

    constructor(context: Context) : super(context) {
        initAttrs()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initAttrs(attrs, defStyleAttr)
    }

    protected fun initAttrs(
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
    ) {
        super.setEllipsize(TextUtils.TruncateAt.END)
        context.obtainStyledAttributes(
            attrs,
            R.styleable.ExpandableTextView,
            defStyleAttr,
            defStyleRes
        ).apply {
            maxLinesInit = maxLines
            suffixInit =
                getString(R.styleable.ExpandableTextView_etv_ellipsize_text) ?: DEFAULT_SUFFIX_TEXT
//            setEllipsizedSuffix(maxLines, suffixInit)
//            checkState()
        }.recycle()
    }

    protected fun setEllipsizedSuffix(maxLines: Int, suffix: String) {
        addOnLayoutChangeListener(object : OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View?,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {

                var newText = allText
                val tvWidth = width
                val textSize = textSize

                if (!textHasEllipsized(newText, tvWidth, textSize, maxLines)) return

                while (textHasEllipsized(newText, tvWidth, textSize, maxLines)) {
                    newText = newText.substring(0, newText.length - 1).trim()
                }

                //now replace the last few chars with the suffix if we can
                val endIndex =
                    newText.length - suffix.length - 1 //minus 1 just to make sure we have enough room
                if (endIndex > 0) {
                    newText = "${newText.substring(0, endIndex).trim()}$suffix"
                }

                text = newText

                setStyleEllipsize(text.toString(), suffix)

                removeOnLayoutChangeListener(this)
            }
        })
    }

    val listenerClick = object : ClickableSpan() {
        override fun onClick(p0: View) {
            Log.d("ExpandableTextView", "click:${state}")
            when (state) {
                State2.Static, State2.Collapsing -> {
                    state = State2.Expanded
                }
                State2.Expanded -> {
                    state = State2.Collapsing
                }
                else -> {

                }
            }
        }
    }

    fun checkState() {
        Log.d("ExpandableTextView", "checkState:${state}")

        when (state) {
            State2.Static, State2.Collapsing -> {
                maxLines = maxLinesInit
                setEllipsizedSuffix(maxLines, suffixInit)
            }
            State2.Expanded -> {
                maxLines = Int.MAX_VALUE
                text = allText
                collap()
            }
            State2.Expanding -> {
                maxLines = Int.MAX_VALUE
                text = allText
                collap()
            }
            else -> {
                maxLines = maxLinesInit
                setEllipsizedSuffix(maxLines, suffixInit)
            }
        }
    }

    private fun collap() {
        val collapText = "See Less"
        val newText = allText + collapText
        val spannable = SpannableString(newText)
        text = null

        Log.d("ExpandableTextView", "collap--${newText}")

        spannable.setSpan(
            listenerClick,
            newText.length - collapText.length,
            newText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        setText(spannable, BufferType.SPANNABLE)
        movementMethod = LinkMovementMethod.getInstance()
        setLinkTextColor(Color.GREEN)

    }

    private fun setStyleEllipsize(textAll: String, suffix: String) {
        val spannable = SpannableString(textAll)

        spannable.setSpan(
            listenerClick,
            textAll.length - suffix.length,
            textAll.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        setLinkTextColor(Color.RED)
        setText(spannable, BufferType.SPANNABLE)

        movementMethod = LinkMovementMethod.getInstance()
    }

    private fun textHasEllipsized(
        text: String,
        tvWidth: Int,
        textSize: Float,
        maxLines: Int
    ): Boolean {
        val paint = Paint()
        paint.textSize = textSize
        val size = paint.measureText(text).toInt()

        return size > tvWidth * maxLines
    }

    enum class State2 {
        /** [State2], in which text is ellipsized and it is possible to expand it, if [expandedLines] is greater than [collapsedLines]. */
        Collapsed,
        /** [State2], in which [ExpandableTextView] is in the progress of [collapsing][collapse]. */
        Collapsing,
        /** [State2], in which [ExpandableTextView] is in the progress of [expanding][expand]. */
        Expanding,
        /** [State2], in which text is expanded, with maximum number of displayed [text lines][getLineCount] equal to [expandedLines]. */
        Expanded,
        /** [State2], in which either text can fit in specified number of [collapsedLines] without ellipsizing
         * or [collapsedLines] equals [expandedLines]. [ExpandableTextView] will not [expand] or [collapse] in this state. */
        Static
    }
}