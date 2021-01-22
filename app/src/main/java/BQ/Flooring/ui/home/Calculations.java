package BQ.Flooring.ui.home;

public class Calculations {

    public static double MetersSquared(double height, double width){
        return height*width;
    }

    public static  double FeetToMeters(double feet){
        return feet*0.3048;
    }

    public static int NumPacks(double size, double packSize){
        return (int) Math.ceil((size/packSize) * 1.1);
    }


}
