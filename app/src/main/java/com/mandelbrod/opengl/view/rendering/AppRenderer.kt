package com.mandelbrod.opengl.view.rendering

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import com.mandelbrod.opengl.config.ColorAlgorithmType
import com.mandelbrod.opengl.config.FractalConfigFactory
import com.mandelbrod.opengl.config.FractalType
import com.mandelbrod.opengl.model.Scale
import com.mandelbrod.opengl.model.Translation
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class AppRenderer : GLSurfaceView.Renderer {

    private lateinit var fractal: Fractal

    private var colorAlgorithmType = ColorAlgorithmType.BLACK_AND_WHITE_SOFT
    private var zoom: Float = 4f
    private var screenAspectRatio = 1f
    private var translationX = -1.5f
    private var translationY = 2f

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        val fractalConfig = FractalConfigFactory.getFractalConfig(
            FractalType.MANDELBROT,
            colorAlgorithmType
        )
        fractal = Fractal(fractalConfig)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        screenAspectRatio = width.toFloat() / height
        fractal.screenSize = floatArrayOf(width.toFloat(), height.toFloat())
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // Model View Projection Matrix
        // Better to use Matrix.translateM, Matrix.scaleM etc.
        val transformationMatrix = floatArrayOf(
            1f * screenAspectRatio * zoom, 0f, 0f, 0f,
            0f, 1f * zoom, 0f, 0f,
            0f, 0f, 1f, 0f,
            translationX, -translationY, 0f, 1f
        )

        fractal.onDraw(transformationMatrix)
    }

    fun onZoom(scale: Scale) {
        zoom += scale.factor * zoom
    }

    fun onTranslation(translation: Translation) {
        translationX += translation.deltaX * zoom * 0.5f
        translationY += translation.deltaY * zoom * 0.5f
    }

    fun onRotation() {
        // TODO do later
    }

    fun changeColorAlgorithm(colorAlgorithmType: ColorAlgorithmType) {
        val config = FractalConfigFactory.getFractalConfig(
            FractalType.MANDELBROT,
            colorAlgorithmType
        )
        fractal.loadConfig(config)
    }
}
