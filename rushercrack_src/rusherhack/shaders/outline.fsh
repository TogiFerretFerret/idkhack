#version 330 core

in vec2 texCoord;
out vec4 fragColor;

uniform vec2 resolution;
uniform sampler2D image;


uniform bool filled;
uniform float fillOpacity;
uniform int fillMode;
uniform bool outline;
uniform int radius;
uniform bool fade;
uniform bool gradientRainbow;
uniform float rainbowSaturation;
uniform float time;

void main() {
	
	fragColor = vec4(0.0);
	
	//color of current pixel
	vec4 currentColor = texture(image, texCoord);
	vec2 pos = gl_FragCoord.xy;
	
	if (gradientRainbow) {
		vec2 uv = 5.5 * pos / resolution;
		uv += time;
		
		vec3 rainbowColor = sin((length(uv * 1.0)) + vec3(0.0, 0.33, 0.66) * 6.28) * 0.35 + (1.0 - rainbowSaturation / 2.0);
		currentColor = vec4(rainbowColor, currentColor.a);
	}
	
	if (currentColor.a > 0) {
		if (!filled) {
			return;
		}
		
		float opacity = fillOpacity;
		
		if (fillMode != 0) {
			
			//pattern
			switch (fillMode) {
				case 1://dots
				if (mod(pos.x, 6.0) < 1.0 && mod(pos.y, 6.0) < 1.0) {
					opacity = 0.75;
				}
				break;
				case 2://lines
				if (!(mod(pos.x, 10.0) > 1.0 && mod(pos.y, 10.0) > 1.0) && (mod(pos.x + 6.0, 10.0) > 3.0 && mod(pos.y + 6.0, 10.0) > 3.0)) {
					opacity = 0.75;
				}
				break;
			}
		}
		
		//update color with new opacity
		fragColor = vec4(currentColor.rgb, opacity);
		return;
	} else if (outline) {
		
		vec2 onePixel = vec2(1.0, 1.0) / resolution;
		
		int hits = 0;
		vec4 fadeColor = vec4(0.0);
		//find edges
		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {
				
				vec2 offset = vec2(x, y) * onePixel;
				vec4 sampled = texture(image, texCoord.xy + offset);
				
				if (sampled.a > 0) {
					
					fadeColor = gradientRainbow ? vec4(currentColor.rgb, 1.0) : vec4(sampled.rgb, 1.0);
					if (!fade) {
						fragColor = fadeColor;
						return;
					}
					
					hits++;
				}
			}
		}
		
		if (fade && hits > 0) {
			float hitPercent = float(hits) / float((radius * 2 + 1) * (radius * 2 + 1));
			fragColor = vec4(fadeColor.rgb, hitPercent);
			return;
		}
		
		fragColor = fadeColor;
		return;
	}
}