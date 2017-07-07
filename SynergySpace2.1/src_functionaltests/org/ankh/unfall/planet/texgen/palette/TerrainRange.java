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
package org.ankh.unfall.planet.texgen.palette;

import java.awt.Color;

public interface TerrainRange {

	/**
	 * @param x La position x sur la carte
	 * @param y La position y sur la carte
	 * @param temperature La temperature du point
	 * @param slope La pente du point
	 * @param height La hauteur du point
	 * @return Retourne le facteur utilisé pour le mix entre des diverses couleurs.
	 */
	public float getFactor(int x, int y, int height, int slope, float temperature);	
	
	
	/**
	 * @return Retourne la couleur de ce type de terrain
	 */
	public Color getTerrainColor();
	
	/**
	 * @return Retourne la valeur spéculaire de ce type de terrain
	 */
	public Color getTerrainSpecular();
	
}
