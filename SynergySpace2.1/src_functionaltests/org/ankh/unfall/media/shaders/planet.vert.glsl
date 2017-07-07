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
uniform vec3 fvLightPosition;

varying vec3 vNormal;

varying vec3 vLightDirectionInWorldSpace;
varying vec3 vViewDirectionInWorldSpace;

varying vec3 vLightDirectionInTangentSpace;
varying vec3 vViewDirectionInTangentSpace;

varying vec2 vTexCoords;

vec3 tangentAt(vec3 normal) {
	vec3 c1 = cross(normal, vec3(0.0, 1.0, 0.0));
	vec3 c2 = cross(normal, vec3(0.0, 0.0, 1.0));
	
	if(length(c1)>length(c2))
	{
		return c1;
	}
	
	return c2;
}

void main(void)
{
   //Transformation du vertex par la matrice de projection et la matrice de monde.
   gl_Position = ftransform();

   //Récuperation des coordonnées de textures
   vTexCoords = gl_MultiTexCoord0.xy;

   //Methode 1
   //vNormal 				= gl_NormalMatrix * gl_Normal;
   //vec3 vTangent     	= tangentAt(normalize(vNormal));
   //vec3 vBinormal     	= normalize(cross(normalize(vNormal), vTangent));

   //Methode 2
   //Récuperation des normales, tangent et binormale
   
   vec3 binormal = tangentAt(normalize(gl_Normal));
   vec3 tangent  = cross(normalize(gl_Normal), binormal);
   
   vNormal              = gl_NormalMatrix * normalize(gl_Normal);
   vec3 vBinormal       = gl_NormalMatrix * binormal;
   vec3 vTangent        = gl_NormalMatrix * tangent;

	

	//-------------

   //Récuperation de la position du vertex dans le monde.
   vec3 vertexPosition = ( gl_ModelViewMatrix * gl_Vertex ).xyz;
   
   vec4 tempLightPos;
   tempLightPos.xyz = fvLightPosition;
   tempLightPos.w = 0.0;
   
   //Récuperation des vecteurs direction de la lumiere et de la vue
   vViewDirectionInWorldSpace =  ( -vertexPosition );
   vLightDirectionInWorldSpace = fvLightPosition - vertexPosition;//( (gl_ModelViewMatrix * tempLightPos).xyz - vertexPosition);
   
   //Transformation dans l'espace tangent
   vLightDirectionInTangentSpace.x = dot(vLightDirectionInWorldSpace, vTangent);
   vLightDirectionInTangentSpace.y = dot(vLightDirectionInWorldSpace, vBinormal);
   vLightDirectionInTangentSpace.z = dot(vLightDirectionInWorldSpace, vNormal);
   
   vViewDirectionInTangentSpace.x = dot(vViewDirectionInWorldSpace, vTangent);
   vViewDirectionInTangentSpace.y = dot(vViewDirectionInWorldSpace, vBinormal);
   vViewDirectionInTangentSpace.z = dot(vViewDirectionInWorldSpace, vNormal);
    
   
}