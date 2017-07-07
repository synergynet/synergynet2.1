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
package org.ankh.unfall.test;

import java.util.concurrent.Callable;

import javax.swing.JOptionPane;

import org.ankh.unfall.planet.Planet;
import org.ankh.unfall.planet.PlanetInformations;
import org.ankh.unfall.planet.texgen.ContinentalGenerator;
import org.ankh.unfall.planet.texgen.PlanetGenerator;
import org.ankh.unfall.planet.texgen.palette.EarthPalette;
import org.ankh.unfall.planet.texgen.palette.MarsPalette;
import org.ankh.unfall.planet.texgen.palette.TerrainPalette;

import com.jme.input.MouseInput;
import com.jmex.editors.swing.settings.GameSettingsPanel;
import com.jmex.game.StandardGame;
import com.jmex.game.state.DebugGameState;
import com.jmex.game.state.GameStateManager;

public class PlanetTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		final StandardGame game = new StandardGame("Test Planete");
		
		
		if (GameSettingsPanel.prompt(game.getSettings())) {
			game.start();
					
			game.executeInGL(new Callable<Integer>() {
				public Integer call() {
					
					DebugGameState state = new DebugGameState();

					//Enable mouse input:
					MouseInput.get().setCursorVisible(true);

					PlanetInformations infos = new PlanetInformations();
					infos.setDaytime(360);
					infos.setEquatorTemperature(45);
					infos.setPoleTemperature(-20);
					infos.setRadius(10f);
					infos.setWaterInPercent(0.7f);
					infos.setHeightFactor(0.2f);
					infos.setSeed((int)System.currentTimeMillis());
					infos.setHumidity(1.0f);
					
					/* Affichage de la scene de menu */
					int choice = JOptionPane.showOptionDialog(null, 
						       "Choice palette type", 
						       "Palette type", 
						       JOptionPane.YES_NO_OPTION,
						       JOptionPane.QUESTION_MESSAGE,
						       null,
						       new String[]{"Earth",
			                   "Mars"}, null);
					
					TerrainPalette palette = null;
					
					if(choice==JOptionPane.YES_OPTION) {
						palette = new EarthPalette(infos);
						infos.setHasCloud(true);
						infos.setAtmosphereDensity(1.0f);
					} else if (choice==JOptionPane.NO_OPTION) {
						palette = new MarsPalette(infos);
						infos.setHasCloud(false);
						infos.setWaterInPercent(0);
						infos.setAtmosphereDensity(0.5f);
						infos.setHeightFactor(0.2f);
					} else {
						System.exit(0);
					}
					
					PlanetGenerator generator = new ContinentalGenerator(1024, 1024, infos, palette );

					long time = System.currentTimeMillis();
					
					Planet p = new Planet(infos, generator, game.getDisplay().getRenderer());
					
					time = System.currentTimeMillis() - time;
					System.out.println(time);
					
					p.setLocalTranslation(0, 0, -10);
					
					p.updateGeometricState(-1, true);
										
					//Disable mouse input:
					MouseInput.get().setCursorVisible(false);

					state.getRootNode().attachChild(p);
					
					state.getRootNode().updateRenderState();

					// Add it to the manager
					GameStateManager.getInstance().attachChild(state);

					// Activate the game state
					state.setActive(true);
					
					return 0;
				}
			});
		}
		
	}

}
