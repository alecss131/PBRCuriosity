#version 460 core

layout (location = 0) out vec4 color;

layout (location = 0) uniform vec3 lightColor;

void main() {
    color = vec4(lightColor, 1.0f);
}