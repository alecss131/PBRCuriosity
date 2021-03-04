#version 460 core

layout (location = 0) in vec3 Normal;
layout (location = 1) in vec3 WorldPos;
layout (location = 2) in vec2 TexCoords;

layout (location = 0) out vec4 gAlbedo;
layout (location = 1) out vec4 gNormal;
layout (location = 2) out vec4 gPosition;

subroutine vec3 albedoSub();
subroutine vec3 normalSub();
subroutine float metallicSub();
subroutine float roughnessSub();
subroutine float aoSub();

struct Material {
    vec4 albedoIn;
    float metallic;
    float roughness;
    float ao;
};

layout (binding = 0, std140) uniform UBO {
    vec3 lightPosition;
    vec3 lightColor;
    Material mat[9];
};

layout (location = 0) uniform int m;

layout (binding = 0) uniform sampler2D albedoMap;
layout (binding = 1) uniform sampler2D normalMap;
layout (binding = 2) uniform sampler2D metallicMap;
layout (binding = 3) uniform sampler2D roughnessMap;
layout (binding = 4) uniform sampler2D aoMap;

vec3 getNormalFromMap();

layout (index = 0) subroutine (albedoSub) vec3 albedofMap() { 
    return pow(texture(albedoMap, TexCoords).rgb, vec3(2.2f));
}; 

layout (index = 1) subroutine (albedoSub) vec3 albedofMat() {
    return mat[m].albedoIn.rgb; 
};

layout (index = 2) subroutine (normalSub) vec3 normalfMap() { 
    return getNormalFromMap();
}; 

layout (index = 3) subroutine (normalSub) vec3 normalfMat() {
    return normalize(Normal); 
};

layout (index = 4) subroutine (metallicSub) float metallicfMap() { 
    return texture(metallicMap, TexCoords).r;
}; 

layout (index = 5) subroutine (metallicSub) float metallicfMat() {
    return mat[m].metallic; 
};

layout (index = 6) subroutine (roughnessSub) float roughnessfMap() { 
    return texture(roughnessMap, TexCoords).r;
}; 

layout (index = 7) subroutine (roughnessSub) float roughnessfMat() {
    return mat[m].roughness; 
};

layout (index = 8) subroutine (aoSub) float aofMap() { 
    return texture(aoMap, TexCoords).r;
}; 

layout (index = 9) subroutine (aoSub) float aofMat() {
    return mat[m].ao; 
};

layout (location = 0) subroutine uniform albedoSub albedoU;
layout (location = 1) subroutine uniform normalSub normalU;
layout (location = 2) subroutine uniform metallicSub metallicU;
layout (location = 3) subroutine uniform roughnessSub roughnessU;
layout (location = 4) subroutine uniform aoSub aoU;

void main() {
    gAlbedo = vec4(albedoU(), metallicU());
    gNormal = vec4(normalU(), roughnessU());
    gPosition = vec4(WorldPos, aoU());
}

vec3 getNormalFromMap() {
    vec3 tangentNormal = texture(normalMap, TexCoords).xyz * 2.0f - 1.0f;
    vec3 Q1  = dFdx(WorldPos);
    vec3 Q2  = dFdy(WorldPos);
    vec2 st1 = dFdx(TexCoords);
    vec2 st2 = dFdy(TexCoords);
    vec3 N   = normalize(Normal);
    vec3 T  = normalize(Q1 * st2.t - Q2 * st1.t);
    vec3 B  = -normalize(cross(N, T));
    mat3 TBN = mat3(T, B, N);
    return normalize(TBN * tangentNormal);
}