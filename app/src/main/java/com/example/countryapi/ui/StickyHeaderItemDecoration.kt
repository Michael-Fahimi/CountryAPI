package com.example.countryapi.ui

import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.countryapi.R
import com.example.countryapi.data.model.ListItem

class StickyHeaderItemDecoration(private val recyclerView: RecyclerView) : RecyclerView.ItemDecoration() {

    private var currentHeader: View? = null

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(canvas, parent, state)

        val topChild = parent.getChildAt(0) ?: return
        val topChildPosition = parent.getChildAdapterPosition(topChild)
        if (topChildPosition == RecyclerView.NO_POSITION) return

        val adapter = parent.adapter as CountryAdapter
        var headerPos = getHeaderPositionForItem(topChildPosition, adapter)
        if (headerPos == RecyclerView.NO_POSITION) return

        val currentHeader = getHeaderViewForItem(headerPos, parent)
        fixLayoutSize(parent, currentHeader)
        val contactPoint = currentHeader.bottom
        val childInContact = getChildInContact(parent, contactPoint, headerPos)

        if (childInContact != null && isHeader(adapter, parent.getChildAdapterPosition(childInContact))) {
            moveHeader(canvas, currentHeader, childInContact)
            return
        }

        drawHeader(canvas, currentHeader)
    }

    private fun getHeaderPositionForItem(itemPosition: Int, adapter: CountryAdapter): Int {
        var headerPosition = RecyclerView.NO_POSITION
        var currentPosition = itemPosition

        do {
            if (isHeader(adapter, currentPosition)) {
                headerPosition = currentPosition
                break
            }
            currentPosition -= 1
        } while (currentPosition >= 0)

        return headerPosition
    }

    private fun isHeader(adapter: CountryAdapter, position: Int): Boolean {
        if (position < 0 || position >= adapter.itemCount) return false
        return adapter.getItemViewType(position) == 0 // 0 = header view type
    }

    private fun getHeaderViewForItem(headerPosition: Int, parent: RecyclerView): View {
        val adapter = parent.adapter as CountryAdapter
        val headerItem = adapter.currentList[headerPosition] as ListItem.HeaderItem

        if (currentHeader == null) {
            currentHeader = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_header, parent, false)
        }

        val tvHeaderLetter = currentHeader!!.findViewById<TextView>(R.id.tvHeaderLetter)
        tvHeaderLetter.text = headerItem.letter.toString()

        return currentHeader!!
    }

    private fun drawHeader(canvas: Canvas, header: View) {
        canvas.save()
        // Apply padding to position the card with the same margins as in the layout
        canvas.translate(8f, 4f)
        header.draw(canvas)
        canvas.restore()
    }

    private fun moveHeader(canvas: Canvas, currentHeader: View, nextHeader: View) {
        canvas.save()
        // Apply padding to position the card with the same margins as in the layout
        canvas.translate(8f, (nextHeader.top - currentHeader.height).toFloat())
        currentHeader.draw(canvas)
        canvas.restore()
    }

    private fun getChildInContact(parent: RecyclerView, contactPoint: Int, currentHeaderPos: Int): View? {
        var childInContact: View? = null
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)
            if (position == RecyclerView.NO_POSITION || position <= currentHeaderPos) {
                continue
            }
            if (child.top <= contactPoint) {
                childInContact = child
            } else {
                break
            }
        }
        return childInContact
    }

    private fun fixLayoutSize(parent: ViewGroup, view: View) {
        // Account for margins in width calculation
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width - 16, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)

        val childWidthSpec = ViewGroup.getChildMeasureSpec(
            widthSpec,
            parent.paddingLeft + parent.paddingRight,
            view.layoutParams.width
        )
        val childHeightSpec = ViewGroup.getChildMeasureSpec(
            heightSpec,
            parent.paddingTop + parent.paddingBottom,
            view.layoutParams.height
        )

        view.measure(childWidthSpec, childHeightSpec)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }
}