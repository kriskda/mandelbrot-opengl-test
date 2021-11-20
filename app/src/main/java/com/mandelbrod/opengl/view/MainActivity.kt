package com.mandelbrod.opengl.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mandelbrod.opengl.config.ColorAlgorithmType
import com.mandelbrod.opengl.databinding.ActivityHomeBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initButtonListener()
    }

    override fun onResume() {
        super.onResume()
        binding.appSurfaceView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.appSurfaceView.onPause()
    }

    private fun initButtonListener() {
        binding.apply {
            bwAlgorithm.setOnClickListener {
                appSurfaceView.setColorAlgorithm(ColorAlgorithmType.BLACK_AND_WHITE)
            }
            bwSoftAlgorithm.setOnClickListener {
                appSurfaceView.setColorAlgorithm(ColorAlgorithmType.BLACK_AND_WHITE_SOFT)
            }
            colorAlgorithm.setOnClickListener {
                appSurfaceView.setColorAlgorithm(ColorAlgorithmType.COLORED)
            }
        }
    }
}
