package com.mandelbrod.opengl.util

import android.util.Log

class FpsCounter {
    private var startTime = System.nanoTime()
    private var frames = 0

    fun logFrame() {
        frames++
        if (System.nanoTime() - startTime >= 1000000000) {
            Log.d("FPSCounter", "fps: $frames")
            frames = 0
            startTime = System.nanoTime()
        }
    }
}