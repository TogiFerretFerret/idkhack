#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec4 inColor;
layout (location = 2) in vec3 normal;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;

uniform float lineWidth;
uniform vec2 screenSize;
uniform bool scaling;

out vec4 color;

const float VIEW_SHRINK = 1.0 - (1.0 / 256);

const mat4 VIEW_SCALE = mat4(
VIEW_SHRINK, 0.0, 0.0, 0.0,
0.0, VIEW_SHRINK, 0.0, 0.0,
0.0, 0.0, VIEW_SHRINK, 0.0,
0.0, 0.0, 0.0, 1.0
);


vec4 calcPos(vec2 lineScreenDirection, float lengthz, vec4 linePosStart, vec3 ndc1, float width) {
	float scaleWidth = 0;
	if (scaling) {
		scaleWidth = max(width / max(1, lengthz), 0.1);
	} else {
		scaleWidth = width;
	}
	
	vec2 lineOffset = vec2(-lineScreenDirection.y, lineScreenDirection.x) * scaleWidth / screenSize;
	
	if (lineOffset.x < 0.0) {
		lineOffset *= -1.0;
	}
	
	if (gl_VertexID % 2 == 0) {
		return vec4((ndc1 + vec3(lineOffset, 0.0)) * linePosStart.w, linePosStart.w);
	} else {
		return vec4((ndc1 - vec3(lineOffset, 0.0)) * linePosStart.w, linePosStart.w);
	}
}



void main() {
	vec4 linePosStart = projectionMatrix * VIEW_SCALE  * modelViewMatrix * vec4(position, 1.0);
	vec4 linePosEnd =  projectionMatrix * VIEW_SCALE  * modelViewMatrix * vec4(position + normal * 10, 1.0);
	
	vec3 ndc1 = linePosStart.xyz / linePosStart.w;
	vec3 ndc2 = linePosEnd.xyz / linePosEnd.w;
	
	float lengthz =  length((modelViewMatrix * vec4(position, 1.0)).xyz) / 4;
	
	vec2 lineScreenDirection = normalize((ndc2.xy - ndc1.xy) * screenSize);
	
	gl_Position = calcPos(lineScreenDirection, lengthz, linePosStart, ndc1, lineWidth);
	color = inColor;
}