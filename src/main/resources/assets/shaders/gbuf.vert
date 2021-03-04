#version 460 core

out gl_PerVertex { 
    vec4 gl_Position; 
};

layout (location = 0) in vec3 pos;
layout (location = 1) in vec2 tex;
layout (location = 2) in vec3 normal;

layout (location = 0) out vec3 Normal;
layout (location = 1) out vec3 FragPos; 
layout (location = 2) out vec2 texUV; 

layout (location = 0) uniform mat4 projection;
layout (location = 1) uniform mat4 view;
layout (location = 2) uniform mat4 model;

void main() {
  Normal = vec3(model * vec4(normal, 0.0f));
  texUV = tex;
  FragPos = vec3(model * vec4(pos, 1.0f));
  gl_Position = projection * view * model * vec4(pos, 1.0f);
}