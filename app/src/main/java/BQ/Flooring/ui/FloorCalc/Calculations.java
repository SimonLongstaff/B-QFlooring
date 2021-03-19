package BQ.Flooring.ui.FloorCalc;

/**
 * Static calculation class called by the Calculation fragment
 */
public class Calculations {

    /**
     * Returns the two values multiplied to get the square meter/feet value
     * @param height
     * @param width
     * @return
     */
    public static double MetersSquared(double height, double width){
        return height*width;
    }

    /**
     * Converts feet to meters
     * @param feet
     * @return
     */
    public static  double FeetToMeters(double feet){
        return feet*0.3048;
    }

    /**
     * Calculatds the number of packs needed to cover an area plus 10%
     * @param size - area sized
     * @param packSize - pack coverage size
     * @return
     */
    public static int NumPacks(double size, double packSize){
        return (int) Math.ceil((size/packSize) * 1.1);
    }


}
