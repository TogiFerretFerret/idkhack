#version 330 core

precision mediump float;

layout (location = 0) in vec2 position;

out vec2 texCoord;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

void main() {
    gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 0.0, 1.0);
    texCoord = vec2(gl_Position.x + 1, gl_Position.y + 1) * 0.5;
}