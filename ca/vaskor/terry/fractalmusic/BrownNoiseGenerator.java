package ca.vaskor.terry.fractalmusic;

public abstract class BrownNoiseGenerator extends RandomizedNoteGenerator implements NoteGenerator {
	public BrownNoiseGenerator(NoteRangeRestrictor overallRange, long randomSeed, int lowPitchChange, int highPitchChange, int lowLengthStep, int highLengthStep) throws OutOfMIDIRangeException {
		super(overallRange, randomSeed);
		lowPC = lowPitchChange;
		highPC = highPitchChange;
		lowLC = lowLengthStep;
		highLC = highLengthStep;

		// Set first note to midpoint of ranges
		nextPitchIndex = (int) ( restrictor.getNumPitches() / 2 );
		nextLengthIndex = (int) ( restrictor.getNumDurations() / 2 );

//		nextNote = new Note(restrictor.getPitch(pitchIndex), 
//                        restrictor.getDuration(lengthIndex));
	}

    @Override
	public Note getNextNote() throws OutOfMIDIRangeException {
		int currentPitchIndex = nextPitchIndex;
                int currentLengthIndex = nextLengthIndex;
		nextPitchIndex = calculateChange(
                        currentPitchIndex, lowPC, highPC, restrictor.getNumPitches()
                        );
		nextLengthIndex = calculateChange(
                        currentLengthIndex, lowLC, highLC, restrictor.getNumDurations()
                        );
		return new Note(restrictor.getPitch(currentPitchIndex), 
                        restrictor.getDuration(currentLengthIndex));
	}
/*
	protected int getLowestPitchChange() { return lowPC; }
	protected int getHighestPitchChange() { return highPC; }
	protected int getLowestConsecutiveLengthChange() { return lowLC; }
	protected int getHighestConsecutiveLengthChange() { return highLC; }
     */

	protected abstract int calculateChange(
                int baseIndex, int maxNegChange,
                int maxPosChange, int maxIndex);

        private int nextPitchIndex, nextLengthIndex;

	private int lowPC;
	private int highPC;
	private int lowLC;
	private int highLC;
}
