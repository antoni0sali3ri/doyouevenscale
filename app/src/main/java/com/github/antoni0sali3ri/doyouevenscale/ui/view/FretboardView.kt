package com.github.antoni0sali3ri.doyouevenscale.ui.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.github.antoni0sali3ri.doyouevenscale.R
import com.github.antoni0sali3ri.doyouevenscale.core.model.FretSpacing
import com.github.antoni0sali3ri.doyouevenscale.core.model.Instrument
import com.github.antoni0sali3ri.doyouevenscale.core.model.Note
import kotlin.math.min

/**
 * TODO: document your custom view class.
 */
class FretboardView : View {

    private var _fretboardColor: Int = Color.WHITE
    var fretboardColor: Int
        get() = _fretboardColor
        set(value) {
            _fretboardColor = value
            pnt.fretboard.color = value
            invalidate()
            requestLayout()
        }

    private var _inlayColor: Int = Color.GRAY
    var inlayColor: Int
        get() = _inlayColor
        set(value) {
            _inlayColor = value
            pnt.inlays.color = value
            invalidate()
            requestLayout()
        }

    private var _fretColor: Int = Color.BLACK
    var fretColor: Int
        get() = _fretColor
        set(value) {
            _fretColor = value
            pnt.frets.color = value
            invalidate()
            requestLayout()
        }

    private var _stringColor: Int = Color.BLACK
    var stringColor: Int
        get() = _stringColor
        set(value) {
            _stringColor = value
            pnt.strings.color = value
            invalidate()
            requestLayout()
        }

    private var _noteStrokeColor: Int = Color.BLACK
    var noteStrokeColor: Int
        get() = _noteStrokeColor
        set(value) {
            _noteStrokeColor = value
            pnt.noteStroke.color = value
            invalidate()
            requestLayout()
        }

    private var _noteColor: Int = Color.WHITE
    var noteColor: Int
        get() = _noteColor
        set(value) {
            _noteColor = value
            pnt.notes.color = value
            invalidate()
            requestLayout()
        }

    private var _highlightColor: Int = Color.BLACK
    var highlightColor: Int
        get() = _highlightColor
        set(value) {
            _highlightColor = value
            pnt.roots.color = value
            invalidate()
            requestLayout()
        }

    private var _labelColor: Int = Color.BLACK
    var labelColor: Int
        get() = _labelColor
        set(value) {
            _labelColor = value
            pnt.stringLabels.color = value
            pnt.fretLabels.color = value
            invalidate()
            requestLayout()
        }

    private val crs: Coordinates = Coordinates()
    private val pnt: Paints

    constructor(context: Context) : super(context) {
        init(null, 0)
        pnt = Paints()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
        pnt = Paints()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
        pnt = Paints()
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        context.obtainStyledAttributes(
            attrs, R.styleable.FretboardView, defStyle, 0
        ).apply {
            try {
                _fretboardColor = getInt(R.styleable.FretboardView_backgroundColor, _fretboardColor)
                _inlayColor = getInt(R.styleable.FretboardView_inlayColor, _inlayColor)
                _fretColor = getInt(R.styleable.FretboardView_fretColor, _fretColor)
                _stringColor = getInt(R.styleable.FretboardView_stringColor, _stringColor)
                _noteStrokeColor = getInt(R.styleable.FretboardView_noteStrokeColor, _noteStrokeColor)
                _noteColor = getInt(R.styleable.FretboardView_noteColor, _noteColor)
                _highlightColor = getInt(R.styleable.FretboardView_highlightColor, _highlightColor)
                _labelColor = getInt(R.styleable.FretboardView_labelColor, _labelColor)
            } finally {
                recycle()
            }
        }
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

        val fretLabelSize = pnt.fretLabels.textSize
        val stringLabelSize = pnt.stringLabels.textSize

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
                    crs.inlayRadius,
                    pnt.inlays
                ) else drawCircle(
                    it,
                    inlayCoord,
                    crs.inlayRadius,
                    pnt.inlays
                )
            }

            // Draw the string labels
            val stringLabelCoord = if (crs.isVertical) {
                crs.fretboardRect.top - 2.5f * crs.noteRadius
            } else {
                crs.fretboardRect.left - 2.5f * crs.noteRadius
            }
            for (i in 0 until crs.stringCount) {
                if (crs.isVertical) {
                    drawText(
                        crs.stringLabels[i],
                        crs.stringPosScaled[i],
                        stringLabelCoord,
                        pnt.stringLabels
                    )
                } else {
                    drawText(
                        crs.stringLabels[crs.stringCount - i - 1],
                        stringLabelCoord,
                        crs.stringPosScaled[i] + 0.35f * stringLabelSize,
                        pnt.stringLabels
                    )
                }
            }

            // Draw the fret numbers
            val fretLabelCoord = if (crs.isVertical) {
                // x coordinate
                crs.fretboardRect.left - crs.noteRadius * 2
            } else {
                // y coordinate
                crs.fretboardRect.bottom + crs.noteRadius * 2 + pnt.fretLabels.textSize
            }
            val count = crs.fretLabels.size
            for (i in 0 until count) {
                if (crs.isVertical) {
                    drawText(
                        crs.fretLabels[i].toString(),
                        fretLabelCoord,
                        crs.fretLabelsPosScaled[i] + fretLabelSize * .35f,
                        pnt.fretLabels
                    )
                } else {
                    drawText(
                        crs.fretLabels[i].toString(),
                        crs.fretLabelsPosScaled[i],
                        fretLabelCoord,
                        pnt.fretLabels
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
                if (crs.isVertical) drawNote(
                    this,
                    crs.stringPosScaled[it.x - 1],
                    crs.fretPosScaled[it.y],
                    crs.noteRadius,
                    pnt.notes
                ) else drawNote(
                    this,
                    crs.fretPosScaled[it.y],
                    crs.stringPosScaled[crs.stringCount - it.x],
                    crs.noteRadius,
                    pnt.notes
                )
            }

            // Fill in the root positions of the current scale
            crs.rootNotes.forEach {
                if (crs.isVertical) drawNote(
                    this,
                    crs.stringPosScaled[it.x - 1],
                    crs.fretPosScaled[it.y],
                    crs.noteRadius,
                    pnt.roots
                ) else drawNote(
                    this,
                    crs.fretPosScaled[it.y],
                    crs.stringPosScaled[crs.stringCount - it.x],
                    crs.noteRadius,
                    pnt.roots
                )
            }
        }
    }

    fun drawNote(canvas: Canvas, x: Float, y: Float, radius: Float, paint: Paint) {
        canvas.drawCircle(x, y, radius, paint)
        canvas.drawCircle(x, y, radius, pnt.noteStroke)
    }

    inner class Paints {

        val fretboard = Paint(0).apply {
            color = fretboardColor
        }

        val inlays = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = inlayColor
            style = Paint.Style.FILL_AND_STROKE
        }

        val strings = Paint(0).apply {
            color = stringColor
            strokeWidth = 5f
        }

        val frets = Paint(0).apply {
            color = fretColor
            strokeWidth = 7f
        }

        val stringLabels = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = labelColor
            textAlign = Paint.Align.CENTER
            textSize = 50f
        }

        val fretLabels = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = labelColor
            textAlign = Paint.Align.RIGHT
            textSize = 50f
        }

        val noteStroke = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = noteStrokeColor
            strokeWidth = 7f
            style = Paint.Style.STROKE
        }

        val notes = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = noteColor
            style = Paint.Style.FILL

        }

        val roots = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = highlightColor
            style = Paint.Style.FILL
        }
    }

    fun setStringCount(stringCount: Int) {
        require(stringCount > 0 && stringCount < Instrument.MaxStrings)
        crs.stringCount = stringCount
    }

    fun updateFretboard(fretRange: IntRange, fretSpacing: FretSpacing) {
        crs.apply {
            firstFret = fretRange.first
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
        var noteRadius: Float = 25f
        var inlayRadius: Float = 10f
        var stringSpacing: Float = 0f
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

            setTextAlignment()

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

        private fun calculateRadii() {
            val last = fretPosScaled.size - 1
            val size = min(
                fretPosScaled[last] - fretPosScaled[last-1],
                stringSpacing
            ) / 2
            noteRadius = size * .45f
            inlayRadius = size * .25f
            pnt.noteStroke.strokeWidth = size * .15f
        }

        private fun setTextAlignment() {
            if (isVertical) {
                pnt.fretLabels.textAlign = Paint.Align.RIGHT
                pnt.stringLabels.textAlign = Paint.Align.CENTER
            } else {
                pnt.fretLabels.textAlign = Paint.Align.CENTER
                pnt.stringLabels.textAlign = Paint.Align.RIGHT
            }
        }

        private fun scaleToSizeVert() {
            val w = width
            val h = height - 1.5f * stringLabelSize
            val xofs = xOffset
            val yofs = yOffset + 1.5f * stringLabelSize

            fretPosScaled = fretPos.map { yofs + it * h }.toTypedArray()

            calculateInlays()
            calculateFretLabels()

            stringLabelSize
            val stringGaps = stringCount - 1
            stringSpacing =
                Math.min(.7f * (fretPosScaled[1] - fretPosScaled[0]), (.7f * w) / stringGaps)
            val fretboardWidth = stringSpacing * stringGaps
            val stringOfs = (w - fretboardWidth) / 2f
            stringPosScaled = (0 until stringCount).map {
                xofs + stringOfs + it * stringSpacing
            }.toTypedArray()

            calculateRadii()

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
            val w = width - 1.5f * stringLabelSize
            val h = height
            val xofs = xOffset + 1.5f * stringLabelSize
            val yofs = yOffset

            fretPosScaled = fretPos.map { xofs + it * w }.toTypedArray()

            calculateInlays()
            calculateFretLabels()

            val stringGaps = stringCount - 1
            stringSpacing =
                Math.min(.7f * (fretPosScaled[1] - fretPosScaled[0]), (.7f * h) / stringGaps)
            val fretboardWidth = stringSpacing * stringGaps
            val stringOfs = (h - fretboardWidth) / 2f
            stringPosScaled = (0 until stringCount).map {
                yofs + stringOfs + it * stringSpacing
            }.toTypedArray()

            calculateRadii()

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