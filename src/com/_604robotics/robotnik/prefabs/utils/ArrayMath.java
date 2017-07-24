package com._604robotics.robotnik.prefabs.utils;

public class ArrayMath
{
    private ArrayMath() {}
    
    public static double ArrayMax(double[] array)
    {
        double max=Double.NEGATIVE_INFINITY;
        for (double element:array)
        {
            if (element>max)
            {
                max=element;
            }
        }
        return max;
    }
    
    public static double ArrayMin(double[] array)
    {
        double max=Double.POSITIVE_INFINITY;
        for (double element:array)
        {
            if (element<max)
            {
                max=element;
            }
        }
        return max;
    }
    
    public static double ArrayAverage(double[] array) {
        double sum = 0;
        for (double element:array) {
            sum+=element;
        }
        return sum/(double)array.length;
    }
}
