package ca.vaskor.terry.fractalmusic;

import javax.swing.*;
import java.awt.BorderLayout;
import java.lang.NumberFormatException;

public class MainPanel extends JPanel implements java.awt.event.ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7274657133880441695L;
	public MainPanel() {
		super();
		this.setLayout(new BorderLayout());

		this.add(generateCommand, BorderLayout.SOUTH);

		JPanel theRest = new JPanel();
		this.add(theRest, BorderLayout.CENTER);


		theRest.add(this.ngr);
		theRest.add(this.sharedOpts);

		// Set event listener
		generateCommand.addActionListener(this);
	}


	// The button has been pressed.
	// Either halt the playing of the MIDI music, or generate a new sequence
	// and play it.
	public void actionPerformed(java.awt.event.ActionEvent event) {
		if (this.currentlyPlayingMIDI) {
			msc.haltExecution();
			
			currentlyPlayingMIDI = false;
			generateCommand.setText(START_STRING);
			
			return;
		}
		
		// Read all of the general options.
		MIDIPitch highPitch, lowPitch;
		try {
			lowPitch = sharedOpts.getLowPitch();
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "The low pitch specified is not a valid number.");
			return;
		} catch (OutOfMIDIRangeException e) {
			JOptionPane.showMessageDialog(this, "The low pitch specified is not a valid MIDI pitch.");
			return;
                }
                
		try {
			highPitch = sharedOpts.getHighPitch();
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "The high pitch specified is not a valid number.");
			return;
		} catch (OutOfMIDIRangeException e) {
			JOptionPane.showMessageDialog(this, "The low pitch specified is not a valid MIDI pitch.");
			return;
                }

		Duration longLength = sharedOpts.getLongLength();
		Duration shortLength = sharedOpts.getShortLength();

		long randomSeed;
		try {
			randomSeed = sharedOpts.getRandomSeed();
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Random seed is not a properly formatted long integer");
			return;
		}
		
		// Now create the requested type of NoteGenerator
		NoteGenerator ng;
		try {
			ng = ngr.getNoteGenerator(
					new NoteRangeRestrictor(lowPitch, highPitch, longLength, shortLength),
					randomSeed);
		} catch (Throwable e) {
			JOptionPane.showMessageDialog(this, "Error: " + e.toString() );
			return;
		}
		
		try {
		    msc = new MIDISequenceCreator(ng);
			msc.playSequence();
			
			currentlyPlayingMIDI = true;
			generateCommand.setText(STOP_STRING);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "MIDI Error:\n" + e.toString());
		}
	}
	
	private static String START_STRING = "Generate Music";
	private static String STOP_STRING = "Stop Playing";
	
	private boolean currentlyPlayingMIDI = false;
	private MIDISequenceCreator msc;

	private JButton generateCommand = new JButton(START_STRING);
	private SharedPanel sharedOpts = new SharedPanel();
	private NoteGeneratorReturner ngr = new RadioGeneratorPanel();
        
        

}
