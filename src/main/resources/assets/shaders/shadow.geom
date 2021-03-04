#version 460 core\n

in gl_PerVertex {
    vec4 gl_Position;
} gl_in[];

out gl_PerVertex {
    vec4 gl_Position;
};

layout(triangles) in;
layout(triangle_strip, max_vertices = 18) out;

out int gl_Layer;

layout(binding = 1, std140) uniform Matr {
    mat4 shadowMatrices[6];
};

layout (location = 0) out vec4 FragPos;

void main() {
    for (int face = 0; face < 6; ++face) {
        gl_Layer = face;
        for (int i = 0; i < 3; ++i) {
            FragPos = gl_in[i].gl_Position;
            gl_Position = shadowMatrices[face] * FragPos;
            EmitVertex();
        }
        EndPrimitive();
    }
}