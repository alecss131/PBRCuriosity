#version 460 core

layout (location = 0) out vec4 FragColor;

layout (location = 0) in vec3 WorldPos;

layout (binding = 0) uniform samplerCube environmentMap;

void main() {		
    vec3 envColor = texture(environmentMap, WorldPos).rgb;
    envColor = envColor / (envColor + vec3(1.0f));
    envColor = pow(envColor, vec3(1.0f / 2.2f)); 
    FragColor = vec4(envColor, 1.0f);
}
