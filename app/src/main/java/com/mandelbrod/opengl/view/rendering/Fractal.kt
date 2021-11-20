package com.mandelbrod.opengl.view.rendering

import android.opengl.GLES20
import com.mandelbrod.opengl.config.FractalConfig
import com.mandelbrod.opengl.shader.VertexShader
import com.mandelbrod.opengl.util.FpsCounter
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class Fractal(private var fractalConfig: FractalConfig) {

    companion object {
        // We draw on square plane
        private val SQUARE_COORDS = floatArrayOf(
            -1.0f, 1.0f, 0.0f,   // top left
            -1.0f, -1.0f, 0.0f,   // bottom left
            1.0f, -1.0f, 0.0f,   // bottom right
            1.0f, 1.0f, 0.0f // top right
        )
        private val DRAW_ORDER = shortArrayOf(0, 1, 2, 0, 2, 3) // order to draw vertices
        private const val COORDS_PER_VERTEX = 3;
        private const val VERTEX_STRIDE = COORDS_PER_VERTEX * 4
    }

    var screenSize = FloatArray(2)

    private val fpsCounter = FpsCounter()
    private val program = GLES20.glCreateProgram()
    private var isNewConfig = false

    private val vertexBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(SQUARE_COORDS.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(SQUARE_COORDS)
                position(0)
            }
        }

    private val drawListBuffer: ShortBuffer =
        ByteBuffer.allocateDirect(DRAW_ORDER.size * 2).run {
            order(ByteOrder.nativeOrder())
            asShortBuffer().apply {
                put(DRAW_ORDER)
                position(0)
            }
        }

    init {
        compileAndAttachShader(GLES20.GL_VERTEX_SHADER, VertexShader.getShader())
        compileAndLoadFragmentShader(fractalConfig.getFragmentShader())
    }

    fun loadConfig(fractalConfig: FractalConfig) {
        this.fractalConfig = fractalConfig
        isNewConfig = true
    }

    fun onDraw(transformationMatrix: FloatArray) {
        fpsCounter.logFrame()

        // Due to onDraw runs on other thread
        if (isNewConfig) {
            compileAndLoadFragmentShader(fractalConfig.getFragmentShader())
            isNewConfig = false
        }

        GLES20.glUseProgram(program)

        GLES20.glGetAttribLocation(program, "vPosition").apply {
            GLES20.glEnableVertexAttribArray(this)
            GLES20.glVertexAttribPointer(this, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, vertexBuffer)
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, DRAW_ORDER.size, GLES20.GL_UNSIGNED_SHORT, drawListBuffer)
            GLES20.glDisableVertexAttribArray(this)
        }

        GLES20.glGetUniformLocation(program, "transformationMatrix").apply {
            GLES20.glUniformMatrix4fv(this, 1, false, transformationMatrix, 0)
        }

        GLES20.glGetUniformLocation(program, "screenSize").apply {
            GLES20.glUniform2fv(this, 1, screenSize, 0)
        }

        GLES20.glGetUniformLocation(program, "maxIterations").apply {
            GLES20.glUniform1i(this, fractalConfig.getMaxIterations())
        }
    }

    private fun compileAndLoadFragmentShader(shaderCode: String) {
        val fragmentShader = compileAndAttachShader(GLES20.GL_FRAGMENT_SHADER, shaderCode)
        GLES20.glLinkProgram(program)
        detachAndDeleteShader(fragmentShader)
    }

    private fun compileAndAttachShader(type: Int, shaderCode: String): Int {
        val shader = GLES20.glCreateShader(type)
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)
        GLES20.glAttachShader(program, shader)
        return shader
    }

    private fun detachAndDeleteShader(shader: Int) {
        GLES20.glDetachShader(program, shader)
        GLES20.glDeleteShader(shader)
    }
}