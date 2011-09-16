package ca.vaskor.terry.fractalmusic;

public class WhiteNoiseGenerator extends RandomizedNoteGenerator implements NoteGenerator {
    public WhiteNoiseGenerator(NoteRangeRestrictor nrr, long randomSeed) {
        super(nrr, randomSeed);
    }
    
    @Override
    public Note getNextNote() throws OutOfMIDIRangeException {
        int pitchIndex = getNextInt(restrictor.getNumPitches());
        int lengthIndex = getNextInt(restrictor.getNumDurations());
        
        return new Note(restrictor.getPitch(pitchIndex),
                restrictor.getDuration(lengthIndex));
	}
}
