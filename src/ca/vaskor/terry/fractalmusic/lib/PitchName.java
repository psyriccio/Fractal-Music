/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.vaskor.terry.fractalmusic.lib;


/**
 * A representation of the pitches within the chromatic scale.
 * 
 * @author Terry Vaskor
 */

public enum PitchName {
    C, C_SHARP, D, D_SHARP, E, F, 
    F_SHARP, G, G_SHARP, A, A_SHARP, B;
    
    public final static String SHARP = "\u266F";
    public final static String FLAT = "\u266D";
    
    /**
     * Provides a convenient and short human-readable representation of
     * the pitch in question, using the Unicode characters for sharp and
     * flat where required.
     * 
     * @return The string representation of the pitch.
     */
    @Override
    public String toString() {
        if (this == PitchName.C_SHARP) return "C" + SHARP;
        if (this == PitchName.D_SHARP) return "E" + FLAT;
        if (this == PitchName.F_SHARP) return "F" + SHARP;
        if (this == PitchName.G_SHARP) return "A" + FLAT;
        if (this == PitchName.A_SHARP) return "B" + FLAT; 
        return super.toString();
    }
}