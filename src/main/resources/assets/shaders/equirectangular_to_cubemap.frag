#version 460 core

layout (location = 0) out vec4 FragColor;
layout (location = 0) in vec4 WorldPos;

layout (binding = 0) uniform sampler2D equirectangularMap;

const vec2 invAtan = vec2(0.1591f, 0.3183f);
vec2 SampleSphericalMap(vec3 v);

void main() {		
    vec2 uv = SampleSphericalMap(normalize(WorldPos.xyz));
    vec3 color = texture(equirectangularMap, uv).rgb;
    FragColor = vec4(color, 1.0f);
}

vec2 SampleSphericalMap(vec3 v) {
    vec2 uv = vec2(atan(v.z, v.x), asin(v.y));
    uv *= invAtan;
    uv += 0.5f;
    return uv;
}