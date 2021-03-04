#version 460 core

out gl_PerVertex { 
    vec4 gl_Position; 
};

layout (location = 0) in vec3 pos;
layout (location = 1) in vec2 tex;
 
layout (location = 0) out vec2 texUV; 

void main() {
  texUV = tex;
  gl_Position = vec4(pos, 1.0f);
}