#version 460 core

out gl_PerVertex  { 
    vec4 gl_Position; 
};

layout (location = 0) in vec3 pos; 

layout (location = 0) uniform mat4 projection;
layout (location = 1) uniform mat4 view;
layout (location = 2) uniform mat4 model;

void main() {
  gl_Position = projection * view * model * vec4(pos, 1.0f);
}