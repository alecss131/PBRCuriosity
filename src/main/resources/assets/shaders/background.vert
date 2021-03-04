#version 460 core

out gl_PerVertex  { 
    vec4 gl_Position; 
};

layout (location = 0) in vec3 aPos;

layout (location = 0) uniform mat4 projection;
layout (location = 1) uniform mat4 view;

layout (location = 0) out vec3 WorldPos;

void main() {
    WorldPos = aPos;
    vec4 clipPos = projection * view * vec4(WorldPos, 1.0f);
    gl_Position = clipPos.xyww;
}