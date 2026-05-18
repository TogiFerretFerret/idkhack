#version 330 core

precision mediump float;

in vec2 texCoord;
out vec4 fragColor;

uniform sampler2D texture2d;

void main() {
    fragColor = texture(texture2d, texCoord);
}