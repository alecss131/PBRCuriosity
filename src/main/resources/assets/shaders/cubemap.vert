#version 460 core

out gl_PerVertex  { 
    vec4 gl_Position; 
};

layout (location = 0) in vec3 aPos;

layout (location = 0) out vec3 WorldPos;

void main() {
    WorldPos = aPos;  
    gl_Position =  vec4(WorldPos, 1.0);
}