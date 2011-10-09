package ca.vaskor.terry.fractalmusic.lib;

public class ReflectingBrownNoteGenerator extends BrownNoteGenerator {
    public ReflectingBrownNoteGenerator(
            NoteRangeRestrictor nrr, long randomSeed,
            int lowestPitchChange, int highestPitchChange,
            int lowestLengthStep, int highestLengthStep) {
        super(nrr, randomSeed, lowestPitchChange, highestPitchChange, 
                lowestLengthStep, highestLengthStep);
    }
    
    @Override
    protected int calculateChange(
            int startVal, int maxNegChange, int maxPosChange, int maxVal
            ) {
        int minVal = 0;
        int changeAmount = getNextInt(maxPosChange - maxNegChange + 1) + maxNegChange;
        int newVal = startVal + changeAmount;
        if ( (newVal < minVal) || (newVal > maxVal) ) {
            newVal = startVal - changeAmount;
            if ( (newVal < minVal) || (newVal > maxVal) ) {
                newVal = startVal;
            }
        }

        return newVal;
    }	
}