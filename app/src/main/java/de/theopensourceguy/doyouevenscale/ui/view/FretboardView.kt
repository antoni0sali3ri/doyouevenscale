package de.theopensourceguy.doyouevenscale.ui.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import de.theopensourceguy.doyouevenscale.R
import de.theopensourceguy.doyouevenscale.core.model.FretSpacing
import de.theopensourceguy.doyouevenscale.core.model.Instrument
import de.theopensourceguy.doyouevenscale.core.model.Note

/**
 * TODO: document your custom view class.
 */
class FretboardView : View {

    private var _fretboardColor: Int = Color.rgb(0xfe, 0xde, 0)
    private var _inlayColor: Int = Color.rgb(0xde, 0xde, 0xde)
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
        val width = w - paddingLeft - paddingRight
        val height = h - paddingTop - paddingBottom
        val xofs = paddingLeft
        val yofs = paddingRight
        crs.setSize(width, height, xofs, yofs)
        crs.scaleToSize()
    }

    fun scaleToSize() {
        crs.scaleToSize()
    }

    val TAG = this.javaClass.simpleName

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val noteRadius = 20f
        val inlayRadius = 10f
        val labelTextSize = 50f

        canvas.apply {
            // Draw the fretboard background
            drawRect(crs.fretboardRect, pnt.fretboard)

            // Draw the fretboard inlays
            val inlayCoord =
                if (crs.isVertical) crs.fretboardRect.centerX() else crs.fretboardRect.centerY()
            crs.inlayPosScaled.forEach {
                if (crs.isVertical) drawCircle(
                    inlayCoord,
                    it,
                    inlayRadius,
                    pnt.inlays
                ) else drawCircle(
                    it,
                    inlayCoord,
                    inlayRadius,
                    pnt.inlays
                )
            }

            // Draw the string labels
            val stringLabelCoord = if (crs.isVertical) {
                crs.fretboardRect.top - 1.2f * crs.stringLabelSize
            } else {
                crs.fretboardRect.left - 1.5f * crs.stringLabelSize
            }
            for (i in 0 until crs.stringCount) {
                if (crs.isVertical) {
                    drawText(
                        crs.stringLabels[i],
                        crs.stringPosScaled[i] - 0.2f * crs.stringLabelSize,
                        stringLabelCoord,
                        pnt.labels
                    )
                } else {
                    drawText(
                        crs.stringLabels[crs.stringCount - i - 1],
                        stringLabelCoord,
                        crs.stringPosScaled[i] + 0.4f * crs.stringLabelSize,
                        pnt.labels
                    )
                }
            }

            // Draw the fret numbers
            val fretLabelCoord = if (crs.isVertical) {
                crs.fretboardRect.left - 2 * labelTextSize
            } else {
                crs.fretboardRect.bottom + 2 * labelTextSize
            }
            val count = crs.fretLabels.size
            for (i in 0 until count) {
                if (crs.isVertical) {
                    drawText(
                        crs.fretLabels[i].toString(),
                        fretLabelCoord,
                        crs.fretLabelsPosScaled[i] + labelTextSize * .4f,
                        pnt.labels
                    )
                } else {
                    drawText(
                        crs.fretLabels[i].toString(),
                        crs.fretLabelsPosScaled[i] - labelTextSize * .4f,
                        fretLabelCoord,
                        pnt.labels
                    )
                }
            }

            // Draw the frets
            crs.fretPosScaled.forEach {
                if (crs.isVertical)
                    drawLine(crs.fretboardRect.left, it, crs.fretboardRect.right, it, pnt.frets)
                else
                    drawLine(it, crs.fretboardRect.top, it, crs.fretboardRect.bottom, pnt.frets)
            }

            // Draw the strings
            crs.stringPosScaled.forEach {
                if (crs.isVertical)
                    drawLine(it, crs.fretboardRect.top, it, crs.fretboardRect.bottom, pnt.strings)
                else
                    drawLine(crs.fretboardRect.left, it, crs.fretboardRect.right, it, pnt.strings)
            }

            // Draw the fingerings for the current scale
            crs.notesInScale.forEach {
                if (crs.isVertical) drawCircle(
                    crs.stringPosScaled[it.x - 1],
                    crs.fretPosScaled[it.y],
                    noteRadius,
                    pnt.notes
                ) else drawCircle(
                    crs.fretPosScaled[it.y],
                    crs.stringPosScaled[crs.stringCount - it.x],
                    noteRadius,
                    pnt.notes
                )
            }

            // Fill in the root positions of the current scale
            crs.rootNotes.forEach {
                if (crs.isVertical) drawCircle(
                    crs.stringPosScaled[it.x - 1],
                    crs.fretPosScaled[it.y],
                    noteRadius,
                    pnt.roots
                ) else drawCircle(
                    crs.fretPosScaled[it.y],
                    crs.stringPosScaled[crs.stringCount - it.x],
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

        val inlays = Paint(android.graphics.Paint.ANTI_ALIAS_FLAG).apply {
            color = _inlayColor
            style = android.graphics.Paint.Style.FILL_AND_STROKE
        }

        val strings = Paint(0).apply {
            color = _stringColor
            strokeWidth = 5f
        }

        val frets = Paint(0).apply {
            color = _fretColor
            strokeWidth = 7f
        }

        val labels = Paint(android.graphics.Paint.ANTI_ALIAS_FLAG).apply {
            color = _noteColor
            textSize = 50f
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
        crs.apply {
            fretPos =
                fretSpacing.getFretPositions(fretRange.last - fretRange.first)
                    .map { it.toFloat() }
                    .toTypedArray()
            inlayPos = InlayPositions
                .filter { fretRange.contains(it) }
                .map { it - fretRange.first }
                .filter { it > 0 }
                .toTypedArray()
            fretLabels = InlayPositions
                .filter { fretRange.contains(it) }
                .toTypedArray()

        }
    }

    fun updateScale(fingerings: List<Point>, roots: List<Point>) {
        crs.apply {
            notesInScale = fingerings
            rootNotes = roots
        }
    }

    fun updateStringLabels(tuning: Instrument.Tuning, displayMode: Note.Display) {
        crs.apply {
            stringLabels = tuning.stringPitches.map { it.getName(displayMode) }.toTypedArray()
        }
    }

    inner class Coordinates {

        // Orientation-independent properties
        lateinit var fretPos: Array<Float>
        lateinit var inlayPos: Array<Int>
        lateinit var fretLabels: Array<Int>
        lateinit var stringLabels: Array<String>

        var firstFret: Int = 0
        var stringCount: Int = 0
        var stringLabelSize: Float = 50f
        var isVertical: Boolean = true
        lateinit var notesInScale: List<Point>
        lateinit var rootNotes: List<Point>

        // Derivative properties
        lateinit var stringPosScaled: Array<Float>
        lateinit var fretPosScaled: Array<Float>
        lateinit var notePosScaled: Array<Array<PointF>>
        lateinit var fretboardRect: RectF
        lateinit var inlayPosScaled: Array<Float>
        lateinit var fretLabelsPosScaled: Array<Float>

        var width: Int = 0
        var height: Int = 0
        var xOffset: Int = 0
        var yOffset: Int = 0

        init {
            reset()
        }

        fun readyToDraw() =
            (width > 0 && height > 0) && (stringCount > 0 && fretPos.isNotEmpty() && stringLabels.isNotEmpty())

        fun reset() {
            fretPos = emptyArray()
            inlayPos = emptyArray()
            fretLabels = emptyArray()
            stringLabels = emptyArray()
            stringCount = 0
            firstFret = 0
            isVertical = true

            stringPosScaled = emptyArray()
            fretPosScaled = emptyArray()
            notePosScaled = emptyArray()
            fretboardRect = RectF()
            inlayPosScaled = emptyArray()
            fretLabelsPosScaled = emptyArray()
            resetSize()
            resetScale()
        }

        private fun resetSize() {
            width = 0
            height = 0
            xOffset = 0
            yOffset = 0
        }

        private fun resetScale() {
            notesInScale = emptyList()
            rootNotes = emptyList()
        }

        fun setSize(w: Int, h: Int, xofs: Int, yofs: Int) {
            isVertical = h > w
            this.width = w
            this.height = h
            this.xOffset = xofs
            this.yOffset = yofs
        }

        fun scaleToSize() {
            if (!readyToDraw()) return

            if (isVertical)
                scaleToSizeVert()
            else
                scaleToSizeHoriz()
        }

        private fun calculateInlays() {
            val inlays: MutableList<Float> = mutableListOf()
            for (i in inlayPos) {
                inlays.add((fretPosScaled[i] + fretPosScaled[i - 1]) / 2f)
            }
            inlayPosScaled = inlays.toTypedArray()
        }

        private fun calculateFretLabels() {
            val labels = mutableListOf<Float>()
            for (i in fretLabels) {
                val pos = i - firstFret
                labels.add(fretPosScaled[pos])
            }
            fretLabelsPosScaled = labels.toTypedArray()
        }

        private fun scaleToSizeVert() {
            val w = width
            val h = height - 2 * stringLabelSize
            val xofs = xOffset
            val yofs = yOffset + 2 * stringLabelSize

            fretPosScaled = fretPos.map { yofs + it * h }.toTypedArray()

            calculateInlays()
            calculateFretLabels()

            val stringGaps = stringCount - 1
            val stringSpacing =
                Math.min(.7f * (fretPosScaled[1] - fretPosScaled[0]), (.7f * w) / stringGaps)
            val fretboardWidth = stringSpacing * stringGaps
            val stringOfs = (w - fretboardWidth) / 2f
            stringPosScaled = (0 until stringCount).map {
                xofs + stringOfs + it * stringSpacing
            }.toTypedArray()

            fretboardRect = RectF(
                xofs + stringOfs,
                yofs,
                xofs + stringOfs + fretboardWidth,
                yofs + h
            )

            notePosScaled = stringPosScaled.map { s ->
                fretPosScaled.map { f ->
                    PointF(s, f)
                }.toTypedArray()
            }.toTypedArray()
        }

        private fun scaleToSizeHoriz() {
            val w = width - 2 * stringLabelSize
            val h = height
            val xofs = xOffset + 2 * stringLabelSize
            val yofs = yOffset

            fretPosScaled = fretPos.map { xofs + it * w }.toTypedArray()

            calculateInlays()
            calculateFretLabels()

            val stringGaps = stringCount - 1
            val stringSpacing =
                Math.min(.7f * (fretPosScaled[1] - fretPosScaled[0]), (.7f * h) / stringGaps)
            val fretboardWidth = stringSpacing * stringGaps
            val stringOfs = (h - fretboardWidth) / 2f
            stringPosScaled = (0 until stringCount).map {
                yofs + stringOfs + it * stringSpacing
            }.toTypedArray()

            fretboardRect = RectF(
                xofs,
                yofs + stringOfs,
                xofs + w,
                yofs + stringOfs + fretboardWidth,
            )

            notePosScaled = stringPosScaled.map { s ->
                fretPosScaled.map { f ->
                    PointF(f, s)
                }.toTypedArray()
            }.toTypedArray()
        }
    }

    companion object {
        val InlayPositions = listOf(
            0,
            3, 5, 7, 9, 12,
            15, 17, 19, 21, 24,
            27, 29
        )
    }
}