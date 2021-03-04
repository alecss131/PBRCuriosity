#version 460 core\n

out gl_PerVertex{
    vec4 gl_Position;
};

layout(location = 0) in vec3 aPos;

layout(location = 0) uniform mat4 model;

void main() {
    gl_Position = model * vec4(aPos, 1.0);
}