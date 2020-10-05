package de.theopensourceguy.doyouevenscale.ui.main

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import de.theopensourceguy.doyouevenscale.R
import de.theopensourceguy.doyouevenscale.core.model.EqualTemperamentFretSpacing
import de.theopensourceguy.doyouevenscale.core.model.FretSpacing
import de.theopensourceguy.doyouevenscale.core.model.Scale
import de.theopensourceguy.doyouevenscale.core.model.TunedInstrument

/**
 * TODO: document your custom view class.
 */
class FretboardView : View {

    private var _fretboardColor: Int = Color.rgb(0xfe, 0xde, 0)
    private var _fretColor: Int = Color.rgb(0xcc, 0xcc, 0xcc)
    private var _stringColor: Int = Color.rgb(0xaa, 0xaa, 0xaa)
    private var _noteColor: Int = Color.rgb(0x22, 0x22, 0x22)

    private val crs : Coordinates = Coordinates()
    private val pnt : Paint = Paint()

    private val fretSpacing = EqualTemperamentFretSpacing
    var instrument: TunedInstrument? = null
        set(value) {
            Log.d(TAG, "instrument.set($value)")
            if (value == null) {
                crs.reset()
                scale = null
            } else {
                crs.updateInstrument(value.instrument.numStrings, value.instrument.numFrets, fretSpacing)
            }
            field = value
            postInvalidate()
        }

    var scale: Scale? = null
        set(value) {
            instrument?.let {
                Log.d(TAG, "scale.set($value)")
                if (value == null)
                    crs.resetScale()
                else
                    crs.updateScale(it, value)
                field = value
                postInvalidate()
            }
        }


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
        if (instrument != null && w > 0 && h > 0) {
            crs.scaleToSize(
                w - paddingLeft - paddingRight,
                h - paddingTop - paddingBottom,
                paddingLeft,
                paddingTop
            )
        }
    }

    val TAG = this.javaClass.simpleName

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (instrument == null) return

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        /*
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom
        */

        val noteRadius = 20f

        canvas.apply {
            drawRect(crs.fretboardRect, pnt.fretboard)
            crs.fretPosScaled.forEach {
                drawLine(crs.fretboardRect.left, it, crs.fretboardRect.right, it, pnt.frets)
            }
            crs.stringPosScaled.forEach {
                drawLine(it, crs.fretboardRect.top, it, crs.fretboardRect.bottom, pnt.strings)
            }
            scale?.let {
                crs.notesInScale.forEach {
                    drawCircle(crs.stringPosScaled[it.x - 1], crs.fretPosScaled[it.y], noteRadius, pnt.notes)
                }
                crs.rootNotes.forEach {
                    drawCircle(crs.stringPosScaled[it.x - 1], crs.fretPosScaled[it.y], noteRadius, pnt.roots)
                }
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

    inner class Coordinates {
        lateinit var fretPos: Array<Float>
        var stringCount: Int = 0

        lateinit var stringPosScaled: Array<Float>
        lateinit var fretPosScaled: Array<Float>
        lateinit var notePosScaled: Array<Array<PointF>>
        lateinit var fretboardRect: RectF
        lateinit var notesInScale: List<Point>
        lateinit var rootNotes: List<Point>

        init {
            reset()
        }

        fun getNotePosition(stringNo: Int, fretNo: Int) =
            PointF(stringPosScaled[stringNo - 1], fretPosScaled[fretNo])

        fun updateInstrument(numStrings: Int, numFrets: Int, fretSpacing: FretSpacing) {
            fretPos = fretSpacing.getFretPositions(numFrets).map { it.toFloat() }.toTypedArray()
            stringCount = numStrings
        }

        fun reset() {
            fretPos = emptyArray()
            stringCount = 0
            stringPosScaled = emptyArray()
            fretPosScaled = emptyArray()
            notePosScaled = emptyArray()
            fretboardRect = RectF()
            resetScale()
        }

        fun resetScale() {
            notesInScale = emptyList()
            rootNotes = emptyList()

        }

        fun updateScale(instrument: TunedInstrument, scale: Scale) {
            notesInScale = instrument.getFretsForScale(scale)
            rootNotes = instrument.getRoots(scale)
        }

        fun scaleToSize(w: Int, h: Int, xofs: Int, yofs: Int) {
            fretPosScaled = fretPos.map { if (it > 0) it * h else yofs.toFloat() }.toTypedArray()

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

