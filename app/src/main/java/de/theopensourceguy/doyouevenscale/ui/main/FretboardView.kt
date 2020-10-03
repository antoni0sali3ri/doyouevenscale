package de.theopensourceguy.doyouevenscale.ui.main

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import de.theopensourceguy.doyouevenscale.R
import de.theopensourceguy.doyouevenscale.core.model.EqualTemperamentFretSpacing
import de.theopensourceguy.doyouevenscale.core.model.Instrument
import de.theopensourceguy.doyouevenscale.core.model.Scale

/**
 * TODO: document your custom view class.
 */
class FretboardView : View {

    private var _exampleColor: Int = Color.RED // TODO: use a default from R.color...
    private var _fretboardColor: Int = Color.rgb(0xfe,0xde, 0)
    private var _fretColor: Int = Color.rgb(0xcc, 0xcc, 0xcc)
    private var _stringColor: Int = Color.rgb(0xaa, 0xaa, 0xaa)
    private var _noteColor: Int = Color.rgb(0x22, 0x22, 0x22)

    private var stringLength: Float = 0f
    private var fretPositions: Array<Float> = emptyArray()
    private var stringSpacing: Float = 0f
    private var stringPositions: Array<Float> = emptyArray()
    private var fretboardWidth: Float = 0f
    private var fretboardRect: RectF = RectF()
    private var notePositions: Array<PointF> = emptyArray()
    private var rootPositions: Array<PointF> = emptyArray()

    private val fretSpacing = EqualTemperamentFretSpacing
    var instrument: Instrument? = null
        set(value) {
            Log.d(TAG, "instrument.set($value)")
            field = value
            updateMeasurements(width, height)
            invalidate()
        }

    var scale: Scale? = null
        set(value) {
            if (instrument != null) {
                Log.d(TAG, "scale.set($value)")
                field = value
                updateScale()
                invalidate()
            }
        }

    /**
     * The font color
     */
    var exampleColor: Int
        get() = _exampleColor
        set(value) {
            _exampleColor = value
        }

    private val fretboardPaint = Paint(0).apply {
        color = _fretboardColor
    }

    private val stringPaint = Paint(0).apply {
        color = _stringColor
        strokeWidth = 5f
    }

    private val fretPaint = Paint(0).apply {
        color = _fretColor
        strokeWidth = 7f
    }

    private val notePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = _noteColor
        strokeWidth = 7f
        style = Paint.Style.STROKE
    }

    private val rootPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = _noteColor
        style = Paint.Style.FILL_AND_STROKE

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
        R.styleable.FretboardView_fretColor



        _exampleColor = a.getColor(
            R.styleable.FretboardView_exampleColor,
            exampleColor
        )

        a.recycle()

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        Log.d(TAG, "onSizeChanged($w, $h, $oldw, $oldh")
        if (w > 0 && h > 0) {
            updateMeasurements(w, h)
            updateScale()
        }
    }

    val TAG = this.javaClass.simpleName

    private fun updateScale() {
        instrument?.let {
            val positions = mutableListOf<PointF>()
            val roots = mutableListOf<PointF>()

            for (s in 0 until it.numStrings) {
                val frets = it.getFretsForScale(it.numStrings - s, scale!!)
                val rootFrets = it.getRoots(it.numStrings - s, scale!!)
                for (f in frets) {
                    Log.d(TAG, "PointF(${stringPositions[s]}, ${fretPositions[f]})")
                    positions.add(PointF(stringPositions[s], fretPositions[f]))
                }
                for (f in rootFrets) {
                    roots.add(PointF(stringPositions[s], fretPositions[f]))
                }
            }

            rootPositions = roots.toTypedArray()
            notePositions = positions.toTypedArray()
        }

    }

    private fun updateMeasurements(w: Int, h: Int) {
        Log.d(TAG, "updateMeasurements($w, $h) instrument = $instrument")
        val height = (h - paddingTop - paddingBottom).toFloat()
        fretboardWidth = (w - paddingLeft - paddingRight).toFloat()
        stringLength = height

        instrument?.let {
            val fretOfs = 0.02f * height
            fretPositions = fretSpacing.getFretPositions(it).map {
                paddingTop + (it * (height - 2* fretOfs) + fretOfs).toFloat()
            }.toTypedArray()
            stringSpacing = 0.9f * (fretPositions.get(1) - fretPositions.get(0))
            val stringOfs = 0.25f * stringSpacing
            stringPositions = (0 until it.numStrings).map {
                paddingLeft + it * stringSpacing + stringOfs
            }.toTypedArray()
            fretboardWidth = stringOfs * 2 + stringSpacing * (it.numStrings - 1)
            fretboardRect = RectF(paddingLeft.toFloat(), paddingTop.toFloat(),
                    paddingLeft + fretboardWidth, paddingTop + stringLength)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(fretboardWidth.toInt(), stringLength.toInt() )
    }

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
            drawRect(fretboardRect, fretboardPaint)
            fretPositions.forEach {
                drawLine(paddingLeft.toFloat(), it, paddingLeft + fretboardWidth, it, fretPaint)
            }
            stringPositions.forEach {
                drawLine(it, paddingTop.toFloat(), it, paddingTop + stringLength, stringPaint)
            }
            scale?.let {
                notePositions.forEach {
                    drawCircle(it.x, it.y, noteRadius, notePaint)
                }
                rootPositions.forEach {
                    drawCircle(it.x, it.y, noteRadius, rootPaint)
                }
            }
        }
    }
}

