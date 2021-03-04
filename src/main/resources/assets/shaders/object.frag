#version 460 core

layout (location = 0) in vec2 TexCoords;

layout (location = 0) out vec4 FragColor;

layout (location = 0) uniform vec3 camPos;
layout (location = 1) uniform vec3 lightPosition;
layout (location = 2) uniform vec3 lightColor;

layout (binding = 0) uniform samplerCube irradianceMap;
layout (binding = 1) uniform samplerCube prefilterMap;
layout (binding = 2) uniform sampler2D brdfLUT;
layout (binding = 3) uniform sampler2D albedoMap;
layout (binding = 4) uniform sampler2D normalMap;
layout (binding = 5) uniform sampler2D positionsMap;

const float PI = 3.14159265359f;
  
float DistributionGGX(vec3 N, vec3 H, float roughness);
float GeometrySchlickGGX(float NdotV, float roughness);
float GeometrySmith(vec3 N, vec3 V, vec3 L, float roughness);
vec3 fresnelSchlick(float cosTheta, vec3 F0);
vec3 fresnelSchlickRoughness(float cosTheta, vec3 F0, float roughness);

void main() {
    vec4 v1 = texture(albedoMap, TexCoords);
    vec4 v2 = texture(normalMap, TexCoords);
    vec4 v3 = texture(positionsMap, TexCoords);
    // material properties		
    vec3 albedo = v1.rgb;
    float metallic = v1.a;
    float roughness = v2.a;
    float ao = v3.a;
    // input lighting data
    vec3 WorldPos = v3.rgb;
    vec3 N =  v2.rgb;
    vec3 V = normalize(camPos - WorldPos);
    vec3 R = reflect(-V, N);
    // calculate reflectance at normal incidence
    vec3 F0 = mix(vec3(0.04f), albedo, metallic);
    // reflectance equation
    vec3 Lo = vec3(0.0f);
    //for (every light) {
    // calculate per-light radiance
    vec3 L = normalize(lightPosition - WorldPos);
    vec3 H = normalize(V + L);
    float distance = length(lightPosition - WorldPos);
    float attenuation = 1.0f / (distance * distance);
    vec3 radiance = lightColor * attenuation;
    // Cook-Torrance BRDF
    float NDF = DistributionGGX(N, H, roughness);   
    float G = GeometrySmith(N, V, L, roughness);    
    vec3 F = fresnelSchlick(max(dot(H, V), 0.0f), F0); 
    vec3 kD = (vec3(1.0f) - F) * (1.0f - metallic);       
    vec3 nominator = NDF * G * F;
    float denominator = 4 * max(dot(N, V), 0.0f) * max(dot(N, L), 0.0f) + 0.001f;
    vec3 specular = nominator / denominator;
    // scale light by NdotL                
    float NdotL = max(dot(N, L), 0.0f); 
    // add to outgoing radiance Lo       
    Lo += (kD * albedo / PI + specular) * radiance * NdotL;
    //}
    F = fresnelSchlickRoughness(max(dot(N, V), 0.0f), F0, roughness); //vec3
    kD = (1.0f - F) * (1.0f - metallic); //vec3	  
    vec3 irradiance = texture(irradianceMap, N).rgb;
    vec3 diffuse = irradiance * albedo;
    const float MAX_REFLECTION_LOD = 4.0f;
    vec3 prefilteredColor = textureLod(prefilterMap, R,  roughness * MAX_REFLECTION_LOD).rgb;    
    vec2 brdf  = texture(brdfLUT, vec2(max(dot(N, V), 0.0f), roughness)).rg;
    specular = prefilteredColor * (F * brdf.x + brdf.y); //vec3
    vec3 ambient = (kD * diffuse + specular) * ao;
    vec3 color = ambient + Lo;
    color = color / (color + vec3(1.0f)); // HDR tonemapping
    color = pow(color, vec3(1.0f / 2.2f)); // gamma correct
    FragColor = vec4(color, 1.0f);
}   

float DistributionGGX(vec3 N, vec3 H, float roughness) {
    float a = roughness*roughness;
    float a2 = a*a;
    float NdotH = max(dot(N, H), 0.0f);
    float NdotH2 = NdotH*NdotH;
    float num = a2;
    float denom = (NdotH2 * (a2 - 1.0f) + 1.0f);
    denom = PI * denom * denom;
    return num / denom;
}

float GeometrySchlickGGX(float NdotV, float roughness) {
    float r = (roughness + 1.0f);
    float k = (r*r) / 8.0f;
    float num = NdotV;
    float denom = NdotV * (1.0f - k) + k;
    return num / denom;
}

float GeometrySmith(vec3 N, vec3 V, vec3 L, float roughness) {
    float NdotV = max(dot(N, V), 0.0f);
    float NdotL = max(dot(N, L), 0.0f);
    float ggx2 = GeometrySchlickGGX(NdotV, roughness);
    float ggx1 = GeometrySchlickGGX(NdotL, roughness);
    return ggx1 * ggx2;
} 

vec3 fresnelSchlick(float cosTheta, vec3 F0) {
    return F0 + (1.0 - F0) * pow(1.0 - cosTheta, 5.0);
}

vec3 fresnelSchlickRoughness(float cosTheta, vec3 F0, float roughness) {
    return F0 + (max(vec3(1.0f - roughness), F0) - F0) * pow(max(1.0f - cosTheta, 0.0f), 5.0f);
} 