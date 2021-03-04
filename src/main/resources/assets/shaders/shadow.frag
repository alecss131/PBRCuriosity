#version 460 core\n

layout(location = 0) in vec4 FragPos;

layout(location = 0) uniform vec3 lightPos;
layout(location = 1) uniform float far_plane;

out float gl_FragDepth;

void main() {
    float lightDistance = length(FragPos.xyz - lightPos);
    lightDistance = lightDistance / far_plane;
    gl_FragDepth = lightDistance;
}