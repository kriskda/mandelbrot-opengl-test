package com.mandelbrod.opengl.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.core.view.GestureDetectorCompat
import com.mandelbrod.opengl.config.ColorAlgorithmType
import com.mandelbrod.opengl.model.Scale
import com.mandelbrod.opengl.model.Translation
import com.mandelbrod.opengl.view.rendering.AppRenderer

class AppSurfaceView(context: Context, attributeSet: AttributeSet) : GLSurfaceView(context, attributeSet) {

    private var appRenderer: AppRenderer
    private val panGestureListener = PanGestureListener()
    private val scaleGestureListener = ScaleListener()
    private val panGestureDetector = GestureDetectorCompat(context, panGestureListener)
    private val scaleGestureDetector = ScaleGestureDetector(context, scaleGestureListener)

    init {
        setEGLContextClientVersion(2)
        appRenderer = AppRenderer()
        setRenderer(appRenderer)
        renderMode = RENDERMODE_CONTINUOUSLY
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        panGestureDetector.onTouchEvent(event)
        scaleGestureDetector.onTouchEvent(event)
        requestRender()
        return true
    }

    fun setColorAlgorithm(colorAlgorithmType: ColorAlgorithmType) {
        appRenderer.changeColorAlgorithm(colorAlgorithmType)
        requestRender()
    }

    private inner class PanGestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            val displayMetrics = Resources.getSystem().displayMetrics
            appRenderer.onTranslation(
                Translation(
                    distanceX / displayMetrics.widthPixels,
                    distanceY / displayMetrics.heightPixels
                )
            )
            return true
        }
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val displayMetrics = Resources.getSystem().displayMetrics
            appRenderer.onZoom(
                Scale(
                    detector.focusX / displayMetrics.widthPixels,
                    detector.focusY / displayMetrics.heightPixels,
                    1f - detector.scaleFactor
                )
            )
            return true
        }
    }
}