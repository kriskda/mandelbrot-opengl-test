package com.mandelbrod.opengl.shader

sealed class ColorShader {

    abstract val algorithm: String

    object BWColorShader : ColorShader() {
        override val algorithm =
            """
            vec4 getColor(vec2 c, float iteration, float maxIterations) {
                if (iteration < maxIterations) {
                    return vec4(1.0, 1.0, 1.0, 1.0);
                } else {
                    return vec4(0.0, 0.0, 0.0, 1.0);
                }              
            }
        """.trimIndent()
    }

    object BWSoftColorShader : ColorShader() {
        override val algorithm =
            """
            vec4 getColor(vec2 c, float iteration, float maxIterations) {       
                float quotient = iteration / maxIterations;
                float fraction = mod(quotient, 1.0) * (maxIterations / 100.0);
                return vec4(fraction, fraction, fraction, 1.0);                        
            }
        """.trimIndent()
    }

    object ColoredShader : ColorShader() {
        override val algorithm =
            """
                vec3 hsv2rgb(vec3 c) {
                    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
                    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
                    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
                }
                
                vec4 getColor(vec2 c, float iteration, float maxIterations) {
                    float hue = iteration / maxIterations;
                    float saturation = 1.0;
                    float value = 0.0;
                    
                    if (iteration < maxIterations) {
                        value = 1.0;
                    }            
                    
                    vec3 rgb = hsv2rgb(vec3(hue, saturation, value));
                    
                    return vec4(rgb.x, rgb.y, rgb.z, 1.0);     
                }                                
            """.trimIndent()
    }
}