package br.itarocha.cartanatal.core.util;

/// <summary>
/// Support for textual and numeric conversions.
/// </summary>
public class Conversions {

    /// <summary>
    /// Convert degree to Radial
    /// </summary>
    /// <param name="x">Degree to be converted</param>
    /// <returns>Input value converted to radials</returns>
    public static double degToRad(double x) {
        return x * Math.PI / 180;
    }


    /// <summary>
    /// limit value x to a value within a given range
    /// pre: upperRange &gt; lowerRange
    /// post: lowerRange &lt;=  x &lt; upperRange
    /// </summary>
    /// <param name="x">the value to be limited to the range</param>
    /// <param name="lowerRange">lower limit of the range (inclusive)</param>
    /// <param name="upperRange">upper limit of the range (exclusive)</param>
    /// <returns>value limited to the defined range</returns>
    public static double toRange(double x, double lowerRange, double upperRange) {
        double rangeSize = upperRange - lowerRange;
        double result = x;
        while (result < lowerRange) result += rangeSize;
        while (result >= upperRange) result -= rangeSize;
        return result;
    }

    /// <summary>
    /// Converts a string in one of the formats ddd.mm.ss, dd.mm.ss to a double value,
    /// the value of direction ('+' or '-') defines the sign of he result.
    /// </summary>
    /// <param name="intext">The string to convert</param>
    /// <param name="direction">The direction, positive for north or east, negative for south or west</param>
    /// <returns>Converted value in degrees. If an error occurred: -999</returns>
    public static double Coordinates2Degrees(String intext, String direction) {
        double x;
        String[] items = intext.split("\\.");
        try {
            double d = Double.parseDouble(items[0]);
            double m = Double.parseDouble(items[1]);
            double s = Double.parseDouble(items[2]);
            x = (double)d + m / 60 + s / 3600;
            if (direction.equals("-")) x = -x;
        }
        catch(Exception e) {
            x = -999;  // TODO better error handling
        }
        return x;
    }

    public static String GrauDecimalParaGrauSexagesimal(double value){
    	String sinal=""; 
    	double valorIni = value;
    	
    	if (value < 0) {
    		value *= -1;
    		sinal = "-";
    	}
    	
        final int deg,min,sec; 
        deg = (int) value; // Round toward 0
        value = (value-deg)*60;
        min = (int) value; // Round toward 0
        value = (value-min)*60; sec = (int) value; // Round toward 0
        value -= sec;          // The remainer (fraction of seconds)

    	String retorno = String.format("%s%03d.%02d.%02d",sinal,deg,min,sec); 
    	System.out.println(String.format("%6f %s",valorIni,retorno));
    	
    	return retorno;
    }
    
    /// <summary>
    /// Converts a text with a time into an array of integers, format of text h:mm:ss
    /// </summary>
    /// <param name="intext">The string to convert</param>
    /// <returns>Converted value as an array of integers</returns>
    public static int[] TimeText2IntArray(String intext) {
        String[] items = intext.split("\\:");
        int[] values = new int[3];
        try {
            for (int i = 0; i < 3; i++) {
                values[i] = Integer.parseInt(items[i]);
            }
        }
        catch(Exception e) {
            for (int i = 0; i < 3; i++) {     // TODO better error handling
                values[i] = -999;
            }
        }
        return values;
    }

    /// <summary>
    /// Converts a text with a time into a double value
    /// </summary>
    /// <param name="intext">The string to convert, it should conform to hh:mm:ss</param>
    /// <returns>Value that represents the string</returns>
    public static double TimeText2Double(String intext) {
        int[] values = TimeText2IntArray(intext);
        return (double)values[0] + (double)values[1] / 60 + (double)values[2] / 3600;
    }
    

}