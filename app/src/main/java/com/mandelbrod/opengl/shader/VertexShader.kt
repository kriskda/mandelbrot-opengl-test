package com.mandelbrod.opengl.shader

object VertexShader {

    fun getShader() =
        """
        attribute vec4 vPosition;
        void main() {
          gl_Position = vPosition;
        }
    """.trimIndent()

}