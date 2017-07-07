/*    
 	This file is part of jME Planet Demo.

    jME Planet Demo is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation.

    jME Planet Demo is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with jME Planet Demo.  If not, see <http://www.gnu.org/licenses/>.
*/
varying vec3 vNormal;

varying vec3 vLightDirectionInWorldSpace;
varying vec3 vViewDirectionInWorldSpace;

varying vec3 vLightDirectionInTangentSpace;
varying vec3 vViewDirectionInTangentSpace;

varying vec2 vTexCoords;

uniform sampler2D baseMap;
uniform sampler2D specMap;
uniform sampler2D normalMap;
uniform sampler2D cloudsMap;

uniform float fSpecularPower;
uniform float fCloudHeight;
uniform float fCloudRotation; //Les nuages se deplace lentement

uniform vec4 fvDiffuse; //Couleur diffuse du soleil
uniform vec4 fvSpecular; //Couleur speculaire du soleil

void main(void)
{
   //Récuperation de la normale depuis la normal map
   vec3 normal           = normalize( ( texture2D( normalMap, vTexCoords ).xyz * 2.0 ) - 1.0 );
   
   //Récuperation de la normale sans relief pour l'ajout de nuages!
   vec3 simpleNormal     = normalize(vNormal);
   
   vec3 lightDirection   = normalize(vLightDirectionInTangentSpace);
   vec3 viewDirection    = normalize(vViewDirectionInTangentSpace);
   
   vec4 diffuseColor = texture2D(baseMap, vTexCoords); 
   
   //Angle entre la lumiere et la normale
   float NdotL = dot(normal, lightDirection);
   
   //De meme, avec la simple normale, mais celle-ci étant dans 
   //  l'espace absolu, on doit utilise les vecteurs de cet espace
   vec3 lightDirectionWorld = normalize(vLightDirectionInWorldSpace);
   vec3 viewDirectionWorld = normalize(vViewDirectionInWorldSpace);
   
   float NdotLCloud = dot(simpleNormal, lightDirectionWorld);
   
   vec3 RCloud = reflect(-lightDirectionWorld, simpleNormal);
   //Les nuages ne reflechisse pas des masses! On divise par 5 le specular max
   float SpecularCloud = pow(max(0.0, dot(viewDirectionWorld, RCloud)), fSpecularPower) * 0.80;
   
   //Récuperation du rayon reflechi pour le calcul du specular
   vec3 R = reflect(-lightDirection, normal);
   float NdotR = max(0.0, dot(viewDirection, R));
   
   //Calcul du facteur speculaire
   float specularFactor = pow(NdotR, fSpecularPower);
   
   vec4 specularColor = texture2D(specMap, vTexCoords);
   
   vec4 specular = specularFactor * specularColor;
   
   vec4 diffuseFinal = diffuseColor * NdotL * fvDiffuse;
   
   vec2 cloudTexCoord = vTexCoords;
   cloudTexCoord.x += fCloudRotation;
   
   vec4 cloudColor = texture2D(cloudsMap, cloudTexCoord);
   
   //Calcul de l'ombre des nuages: Egale à l'inverse de l'opacité du nuage & deplacement en fonction de la lumiere.
   float zFactor = min(0.01, abs(fCloudHeight/lightDirection.z));
   vec2 displacement = -zFactor * lightDirection.yx;
   
   float shadowModule = 1.0 - texture2D(cloudsMap, cloudTexCoord + displacement).w;
   
   //La couleur juste avant l'ajout des nuages
   vec4 beforeCloud = diffuseFinal + specular * fvSpecular;
   
   //Multiplication de la couleur du sol par la modulation de l'ombre du nuage
   beforeCloud *= shadowModule;
   
   vec4 finalCloudColor;
   finalCloudColor.xyz = cloudColor.xyz * fvDiffuse.xyz * 
   												NdotLCloud + fvSpecular.xyz*SpecularCloud;
   finalCloudColor.w = cloudColor.w;
   //Mix selon le canal alpha de la couche nuageuse
   //gl_FragColor.xyz = -viewDirection.xyz;
   
   //gl_FragColor = vec4(NdotLCloud);
   //gl_FragColor = beforeCloud;
   gl_FragColor = mix(beforeCloud, finalCloudColor, finalCloudColor.w);
   //vec3 light = normalize(vLightDirectionInWorldSpace);
   //gl_FragColor = vec4(light.x, light.y, light.z, 1.0);
}