package com.mandelbrod.opengl.shader

sealed class FractalShader {

    abstract fun getFragmentShader(colorShader: String): String
}

object MandelbrotShader : FractalShader() {

    override fun getFragmentShader(colorShader: String) =
        """               
        precision highp float;     
        uniform mat4 transformationMatrix;
        uniform vec2 screenSize;
        uniform int maxIterations;
  
        int mandelbrot(vec2 c) {
            float zRe = 0.0;
            float zIm = 0.0;
            float zReSquare = 0.0;
            float zImSquare = 0.0;
            float modZ = 0.0;

            int iteration = 0;
            while((zReSquare + zImSquare) <= 4.0 && iteration < maxIterations) { 
                zIm = (zRe + zRe) * zIm + c.y;
                zRe = zReSquare - zImSquare + c.x;
                zReSquare = zRe * zRe;
                zImSquare = zIm * zIm;                           

                iteration += 1;
            }
            return iteration;
        }

        $colorShader

        void main() {
            vec2 pos = gl_FragCoord.xy/screenSize.xy;
            vec2 c = (transformationMatrix * vec4(pos,0,1)).xy;
            int iteration = mandelbrot(c);        
            gl_FragColor = getColor(c, float(iteration), float(maxIterations));  
        }                    
    """.trimIndent()
}