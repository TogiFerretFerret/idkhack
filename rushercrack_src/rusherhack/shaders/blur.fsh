#version 330 core

precision highp float;

in vec2 texCoord;

out vec4 fragColor;

uniform vec2 resolution;
uniform sampler2D image;
uniform vec2 direction;

bool flip = false;

// credit: https://github.com/Experience-Monks/glsl-fast-gaussian-blur/
void main() {
    vec2 uv = vec2(texCoord.xy);

    vec4 color = vec4(0.0);
    vec2 off1 = vec2(1.3846153846) * direction;
    vec2 off2 = vec2(3.2307692308) * direction;
    color += texture(image, uv) * 0.2270270270;
    color += texture(image, uv + (off1 / resolution)) * 0.3162162162;
    color += texture(image, uv - (off1 / resolution)) * 0.3162162162;
    color += texture(image, uv + (off2 / resolution)) * 0.0702702703;
    color += texture(image, uv - (off2 / resolution)) * 0.0702702703;

    fragColor = color;
}