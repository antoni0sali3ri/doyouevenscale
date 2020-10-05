package de.theopensourceguy.doyouevenscale.ui.main

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import de.theopensourceguy.doyouevenscale.R
import de.theopensourceguy.doyouevenscale.core.model.FretSpacing
import de.theopensourceguy.doyouevenscale.core.model.Instrument

/**
 * TODO: document your custom view class.
 */
class FretboardView : View {

    private var _fretboardColor: Int = Color.rgb(0xfe, 0xde, 0)
    private var _fretColor: Int = Color.rgb(0xcc, 0xcc, 0xcc)
    private var _stringColor: Int = Color.rgb(0xaa, 0xaa, 0xaa)
    private var _noteColor: Int = Color.rgb(0x22, 0x22, 0x22)

    private val crs: Coordinates = Coordinates()
    private val pnt: Paint = Paint()

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.FretboardView, defStyle, 0
        )
        a.recycle()

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        Log.d(TAG, "onSizeChanged($w, $h, $oldw, $oldh")
            crs.setSize(
                w - paddingLeft - paddingRight,
                h - paddingTop - paddingBottom,
                paddingLeft,
                paddingTop
            )
            crs.scaleToSize()
    }

    fun scaleToSize() {
        crs.scaleToSize()
    }

    val TAG = this.javaClass.simpleName

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val noteRadius = 20f

        canvas.apply {
            // Draw the fretboard background
            drawRect(crs.fretboardRect, pnt.fretboard)

            // Draw the frets
            crs.fretPosScaled.forEach {
                drawLine(crs.fretboardRect.left, it, crs.fretboardRect.right, it, pnt.frets)
            }

            // Draw the strings
            crs.stringPosScaled.forEach {
                drawLine(it, crs.fretboardRect.top, it, crs.fretboardRect.bottom, pnt.strings)
            }

            // Draw the fingerings for the current scale
            crs.notesInScale.forEach {
                drawCircle(
                    crs.stringPosScaled[it.x - 1],
                    crs.fretPosScaled[it.y],
                    noteRadius,
                    pnt.notes
                )
            }

            // Fill in the root positions of the current scale
            crs.rootNotes.forEach {
                drawCircle(
                    crs.stringPosScaled[it.x - 1],
                    crs.fretPosScaled[it.y],
                    noteRadius,
                    pnt.roots
                )
            }
        }
    }

    inner class Paint {

        val fretboard = Paint(0).apply {
            color = _fretboardColor
        }

        val strings = Paint(0).apply {
            color = _stringColor
            strokeWidth = 5f
        }

        val frets = Paint(0).apply {
            color = _fretColor
            strokeWidth = 7f
        }

        val notes = Paint(android.graphics.Paint.ANTI_ALIAS_FLAG).apply {
            color = _noteColor
            strokeWidth = 7f
            style = android.graphics.Paint.Style.STROKE
        }

        val roots = Paint(android.graphics.Paint.ANTI_ALIAS_FLAG).apply {
            color = _noteColor
            style = android.graphics.Paint.Style.FILL_AND_STROKE

        }
    }

    fun setStringCount(stringCount: Int) {
        require(stringCount > 0 && stringCount < Instrument.MaxStrings)
        crs.stringCount = stringCount
    }

    fun updateFretboard(fretRange: IntRange, fretSpacing: FretSpacing) {
        updateFretboard(fretRange.last - fretRange.first, fretSpacing)
    }

    fun updateFretboard(fretCount: Int, fretSpacing: FretSpacing) {
        crs.apply {
            fretPos = fretSpacing.getFretPositions(fretCount).map { it.toFloat() }.toTypedArray()
        }
    }

    fun updateScale(fingerings: List<Point>, roots: List<Point>) {
        crs.apply {
            notesInScale = fingerings
            rootNotes = roots
        }
    }

    fun updateStringLabels(tuning: Instrument.Tuning, displayModeSharp: Boolean = true) {

    }

    fun updateFretLabels(fretRange: IntRange) {

    }

    inner class Coordinates {
        lateinit var fretPos: Array<Float>
        var stringCount: Int = 0
        lateinit var notesInScale: List<Point>
        lateinit var rootNotes: List<Point>

        lateinit var stringPosScaled: Array<Float>
        lateinit var fretPosScaled: Array<Float>
        lateinit var notePosScaled: Array<Array<PointF>>
        lateinit var fretboardRect: RectF

        var w : Int = 0
        var h : Int = 0
        var xofs : Int = 0
        var yofs : Int = 0

        init {
            reset()
        }

        fun readyToDraw() = (w > 0 && h > 0) && (stringCount > 0 && fretPos.isNotEmpty())

        fun reset() {
            fretPos = emptyArray()
            stringCount = 0
            stringPosScaled = emptyArray()
            fretPosScaled = emptyArray()
            notePosScaled = emptyArray()
            fretboardRect = RectF()
            resetSize()
            resetScale()
        }

        fun resetSize() {
            w = 0
            h = 0
            xofs = 0
            yofs = 0
        }

        fun resetScale() {
            notesInScale = emptyList()
            rootNotes = emptyList()
        }

        fun setSize(w: Int, h: Int, xofs: Int, yofs: Int) {
            this.w = w
            this.h = h
            this.xofs = xofs
            this.yofs = yofs
        }

        fun scaleToSize() {
            if (!readyToDraw()) return

            fretPosScaled = fretPos.map { yofs + it * h }.toTypedArray()

            val stringSpacing = .7f * (fretPosScaled[1] - fretPosScaled[0])
            val fretboardWidth = stringSpacing * (stringCount - 1)
            val stringOfs = (w - fretboardWidth) / 2f
            stringPosScaled = (0 until stringCount).map {
                xofs + stringOfs + it * stringSpacing
            }.toTypedArray()

            fretboardRect = RectF(
                xofs + stringOfs,
                yofs.toFloat(),
                xofs + stringOfs + fretboardWidth,
                yofs.toFloat() + h
            )

            notePosScaled = stringPosScaled.map { s ->
                fretPosScaled.map { f ->
                    PointF(s, f)
                }.toTypedArray()
            }.toTypedArray()
        }


    }
}

