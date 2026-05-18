#version 330 core
out vec4 RusherHack_FragColor;

#ifdef GL_ES
precision mediump float;
#endif

uniform float time;
uniform vec2 mouse;
uniform vec2 resolution;

void main(void) {
    float camera_yaw = 0.5;
    float camera_pitch = 1.0;
    float angle = camera_yaw;

    vec2 pos = (gl_FragCoord.xy / resolution.xy) - vec2(camera_yaw, camera_pitch);
    float horizon = 0.0;
    float fov = 0.5;

    float scaling = 0.1;
    float yoffset = - time * 0.3;

    vec3 p = vec3(pos.x, fov, pos.y - horizon);
    vec2 s = vec2(p.x/p.z, p.y/p.z) * scaling;

    //texture

    float nx = p.x;
    float ny = p.y;

    //Rotate
    nx = nx * cos(angle) - ny * sin(angle);
    ny = nx * sin(angle) + ny * cos(angle);

    //Proyect
    nx = scaling * nx/p.z;
    ny = scaling * ny/p.z;

    //Transform
    ny = ny + yoffset;

    float color = sign((mod(nx, scaling) - 0.05) * (mod(ny, scaling) - 0.05));
    //fading
    color *= p.z*p.z*10.0;
    RusherHack_FragColor = vec4(vec3(color, p.z * 64.0, 40), 1.0);

}