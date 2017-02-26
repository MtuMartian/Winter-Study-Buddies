package com.example.miles.winterwonderhack2017.Activities.Data.Misc;

/**
 * Created by miles on 2/25/17.
 */

public class YearParser {
    public static String YearToString(int year)
    {
        switch(year)
        {
            case (1):
                return "Freshman";
            case (2):
                return "Sophomore";
            case (3):
                return "Junior";
            case (4):
                return "Senior";
            default:
                return "Super Senior";
        }
    }
}
