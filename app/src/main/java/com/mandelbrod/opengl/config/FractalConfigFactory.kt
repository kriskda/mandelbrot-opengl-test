package com.mandelbrod.opengl.config

import com.mandelbrod.opengl.shader.ColorShader
import com.mandelbrod.opengl.shader.MandelbrotShader

enum class FractalType {
    MANDELBROT,
}

enum class ColorAlgorithmType {
    BLACK_AND_WHITE,
    BLACK_AND_WHITE_SOFT,
    COLORED,
}

object FractalConfigFactory {

    fun getFractalConfig(
        fractalType: FractalType,
        colorAlgorithmType: ColorAlgorithmType = ColorAlgorithmType.BLACK_AND_WHITE,
        maxIterations: Int = 100
    ): FractalConfig =
        when (fractalType) {
            FractalType.MANDELBROT -> FractalConfig(
                maxIterations,
                MandelbrotShader,
                colorAlgorithmType.toColorAlgorithm()
            )
        }

    private fun ColorAlgorithmType.toColorAlgorithm() =
        when (this) {
            ColorAlgorithmType.BLACK_AND_WHITE -> ColorShader.BWColorShader
            ColorAlgorithmType.BLACK_AND_WHITE_SOFT -> ColorShader.BWSoftColorShader
            ColorAlgorithmType.COLORED -> ColorShader.ColoredShader
        }
}