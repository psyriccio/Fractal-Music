package ca.vaskor.terry.fractalmusic.ui;

import javax.swing.*;
import java.awt.BorderLayout;

import ca.vaskor.terry.fractalmusic.lib.NoteGenerator;
import ca.vaskor.terry.fractalmusic.lib.MIDISequenceCreator;

/**
 * This is the main panel for displaying the GUI.
 * 
 * It contains the {@link RadioGeneratorPanel}
 * that allows selection of the type of music to generate
 * and appropriate parameters for that particular type of music, the {@link SharedPanel}
 * with options that apply to all types of music, and buttons to create a new music
 * generating window and to start playing music.
 * 
 * @author Terry Vaskor
 */
public class MainPanel extends RecursiveEnableJPanel  {
    
    private static final long serialVersionUID = 7274657133880441695L;
    private static final int PANEL_PADDING = 5;
    
    /**
     * Create a new MainPanel, optionally with data to clone from another
     * {@link MainPanel}.
     * 
     * @param sDat Data to copy from another {@link SharedPanel}; null if no data supplied
     * @param gDat Data to copy from another {@link GeneratorPanel}; null if no data supplied
     * 
     */
    public MainPanel(SharedPanelData sDat, GeneratorPanelData gDat) {
        super();
        sharedOpts = new SharedPanel(sDat);
        ngr = new RadioGeneratorPanel(gDat);
        forkCommand = new ForkWindowButton(sharedOpts, sharedOpts, ngr);
        
        this.setLayout(new BorderLayout());
        
        this.add(buttonContainer, BorderLayout.SOUTH);
        this.add(disableMaster, BorderLayout.CENTER);    
        
        layoutMainPanel();
        layoutButtonPanel();
        generateCommand.addActionListener(new ButtonListener());
    }
    
    private void layoutButtonPanel() {
        buttonContainer.setLayout(
                new java.awt.GridLayout(0, 1, PANEL_PADDING, PANEL_PADDING)
                );
        buttonContainer.add(forkCommand);
        buttonContainer.add(generateCommand);
    }
        
    private void layoutMainPanel() {
        SpringLayout layout = new SpringLayout();
        disableMaster.setLayout(layout);
        
        disableMaster.add(this.ngr);
        disableMaster.add(this.sharedOpts);
        
        
        
        // Put the NoteGeneratorReturner at the top of the panel,
        // centred horizontally.
        layout.putConstraint(SpringLayout.NORTH, ngr,
                PANEL_PADDING,
                SpringLayout.NORTH, disableMaster);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, ngr,
                0,
                SpringLayout.HORIZONTAL_CENTER, disableMaster);
        
        // Put the SharedPanel directly below the NoteGeneratorReturner,
        // also centred horizontally.
        layout.putConstraint(SpringLayout.NORTH, sharedOpts,
                PANEL_PADDING,
                SpringLayout.SOUTH, ngr);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, sharedOpts,
                0,
                SpringLayout.HORIZONTAL_CENTER, disableMaster);
        
        // Enforce a minimum panel height by putting a constraint on the
        // south side of it.
        layout.putConstraint(SpringLayout.SOUTH, disableMaster,
                Spring.constant(PANEL_PADDING, PANEL_PADDING, PANEL_PADDING * 1000),
                SpringLayout.SOUTH, sharedOpts);
        
        
        // Make sure the components do not stretch vertically or horizontally
        // by setting their maximum, preferred and minimum heights/widths to the
        // same heights/widths.
        java.awt.Component[] comps = { ngr, sharedOpts };
        String[] attributes = { SpringLayout.HEIGHT, SpringLayout.WIDTH };
        
        for (java.awt.Component component : comps) {
            for (String attribute: attributes) {
                int setValue = layout.getConstraint(attribute, component).getPreferredValue();
                Spring newSpring = Spring.constant(setValue, setValue, setValue);
                layout.getConstraints(component).setConstraint(attribute, newSpring);
            }
        }
        
        // Ensure a minimum panel width too, by ensuring the minimum width is
        // at least as wide as any contained components.
        int maxwidth = Math.max(
                layout.getConstraint(SpringLayout.WIDTH, ngr).getMinimumValue(),
                layout.getConstraint(SpringLayout.WIDTH, sharedOpts).getMinimumValue()
                ) 
                + (PANEL_PADDING * 2);
        layout.getConstraints(disableMaster).setConstraint(
                SpringLayout.WIDTH,
                Spring.constant(maxwidth, maxwidth, maxwidth)
                );
    }


    // The button has been pressed.
    // Either halt the playing of the MIDI music, or generate a new sequence
    // and play it.
    private class ButtonListener implements java.awt.event.ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent event) {
            if (currentlyPlayingMIDI) {
                // Stop execution of the music
                msc.haltExecution();

                // Restore the UI to a state where it accepts user input.
                currentlyPlayingMIDI = false;
                disableMaster.setEnabled(true);
                
                generateCommand.toggle();

                return;
            }

            // Read all of the general options.
            Long randomSeed;
            try {
                randomSeed = sharedOpts.getRandomSeed();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(sharedOpts, "Random seed must be a properly formatted long integer");
                return;
            }
            
            java.util.Random randGen = new java.util.Random();
            if (randomSeed != null) {
                randGen.setSeed(randomSeed);
            }

            NoteGenerator ng = ngr.getNoteGenerator(
                    sharedOpts.getNoteRangeRestrictor(),
                    randGen
                    );

            SharedPanelData dat = sharedOpts.getData();
            
            try {
                // Create and play a new MIDI sequence based on the NoteGenerator
                // created from the user's specifications
                msc = new MIDISequenceCreator(ng, dat.instrument, dat.volume);
                msc.playSequence();

                // Modify the user interface so the user can no longer modify anything
                // and only has the option to stop the music.
                currentlyPlayingMIDI = true;
                disableMaster.setEnabled(false);
                generateCommand.toggle();
            } catch (javax.sound.midi.InvalidMidiDataException e) {
                JOptionPane.showMessageDialog(disableMaster, "MIDI Error:\n" + e.toString());
            } catch (javax.sound.midi.MidiUnavailableException e) {
                JOptionPane.showMessageDialog(disableMaster, "MIDI is not available:\n" + e.toString());
            }
        }
    }
    
    /*package*/ MIDISequenceCreator getMIDISequenceCreator() {
        if (currentlyPlayingMIDI) {
            return msc;
        }
        return null;
    }

    private boolean currentlyPlayingMIDI = false;
    private MIDISequenceCreator msc;
    
    private JPanel disableMaster = new RecursiveEnableJPanel();
    private SharedPanel sharedOpts;
    private GeneratorPanel ngr;
    
    private JPanel buttonContainer = new JPanel();
    private MusicToggleButton generateCommand = new MusicToggleButton();
    private ForkWindowButton forkCommand;
}
