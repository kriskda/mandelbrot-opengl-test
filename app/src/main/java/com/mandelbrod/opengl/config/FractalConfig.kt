package com.mandelbrod.opengl.config

import com.mandelbrod.opengl.shader.ColorShader
import com.mandelbrod.opengl.shader.FractalShader

class FractalConfig(
    private val maxIterations: Int = 100,
    private val fractalShader: FractalShader,
    private val colorShader: ColorShader
) {

    fun getMaxIterations() = maxIterations

    fun getFragmentShader() = fractalShader.getFragmentShader(colorShader.algorithm)
}
