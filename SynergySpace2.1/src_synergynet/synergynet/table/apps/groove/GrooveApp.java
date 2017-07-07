/*
 * Copyright (c) 2009 University of Durham, England
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'SynergySpace' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package synergynet.table.apps.groove;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;

import synergynet.contentsystem.ContentSystem;
import synergynet.contentsystem.items.Frame;
import synergynet.table.appregistry.ApplicationInfo;
import synergynet.table.appregistry.menucontrol.HoldBottomLeftExit;
import synergynet.table.apps.DefaultSynergyNetApp;

public class GrooveApp extends DefaultSynergyNetApp{	
	private ContentSystem content;
	private Synthesizer synthesizer;
	private Instrument[] instruments;
	private Instrument currentInstrument;
	private MidiChannel currentChannel;
	private MidiChannel[] allMidiChannels;	
	
	public GrooveApp(ApplicationInfo info) {
		super(info);
	}
	
	@Override
	public void addContent() {
		content = ContentSystem.getContentSystemForSynergyNetApp(this);
		setMenuController(new HoldBottomLeftExit());
		try {
			synthesizer = MidiSystem.getSynthesizer();
			synthesizer.open();
			Soundbank sb = MidiSystem.getSoundbank(GrooveApp.class.getResourceAsStream("soundbank-deluxe.gm"));
			synthesizer.loadAllInstruments(sb);
//			instruments = synthesizer.getDefaultSoundbank().getInstruments();
			instruments = sb.getInstruments();
			currentInstrument = instruments[0];
			synthesizer.loadInstrument(currentInstrument);						
			allMidiChannels = synthesizer.getChannels();
	        currentChannel = allMidiChannels[0];
	        currentChannel.setMono(false);
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Frame f = (Frame) content.createContentItem(Frame.class);
		f.setWidth(480);
		f.setHeight(150);
		f.setRotateTranslateScalable(false);
		f.setScale(1.8f);
		f.centerItem();
		Graphics2D gfx = f.getGraphicsContext();
		gfx.setColor(Color.black);
		gfx.fillRect(0, 0, 680, 150);
		GrooveInstrument pianoInstrument = new GrooveInstrument("Piano", currentChannel);
		Piano p = new Piano(gfx);
		f.addItemListener(new PianoInteraction(p, pianoInstrument));	
		f.flushGraphics();
	}
}
