package Main;

import Math.Mat3d;
import Math.Vect3d;
import java.text.DecimalFormat;

public class Logger {

    private static int decimalPlaces = 5;

    public static void setDecimalPlaces(int places){
        decimalPlaces = places;
    }

    private static String format(double n){
        if(decimalPlaces < 0)
            return "" + n;
        String format = "0.";
        for(int i = 0; i < decimalPlaces; i++){
            format += "0";
        }

        DecimalFormat df = new DecimalFormat(format);
        String str = df.format(n);
        
        if(n < 0)
            return str;
      
        return " " + str;

    }

    public static void printVect(Vect3d v, String name){
        if(!Main.Logging())
            return;

        System.out.println(name + ".x : " + format(v.x));
		System.out.println(name + ".y : " + format(v.y));
		System.out.println(name + ".z : " + format(v.z));
		System.out.println();
    }

    public static void printMatrix(Mat3d m, String name){
        if(!Main.Logging())
            return;

        System.out.print(name + ".xx : " + format(m.c1.x)); System.out.print(" " + name + ".yx : " + format(m.c2.x)); System.out.println(" " + name + ".zx : " + format(m.c3.x));
		System.out.print(name + ".xy : " + format(m.c1.y)); System.out.print(" " + name + ".yy : " + format(m.c2.y)); System.out.println(" " + name + ".zy : " + format(m.c3.y));
		System.out.print(name + ".xz : " + format(m.c1.z)); System.out.print(" " + name + ".yz : " + format(m.c2.z)); System.out.println(" " + name + ".zz : " + format(m.c3.z));
		System.out.println();
    }

    //TODO: figure out how to trace where the print is coming from.
}
