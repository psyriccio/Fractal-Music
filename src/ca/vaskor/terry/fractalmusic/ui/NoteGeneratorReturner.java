package ca.vaskor.terry.fractalmusic.ui;

import ca.vaskor.terry.fractalmusic.lib.NoteRangeRestrictor;
import ca.vaskor.terry.fractalmusic.lib.NoteGenerator;

/**
 * @author Terry Vaskor
 *
 */
public abstract class NoteGeneratorReturner extends javax.swing.JPanel {
	public abstract NoteGenerator getNoteGenerator(NoteRangeRestrictor nrr, long randomSeed);
}