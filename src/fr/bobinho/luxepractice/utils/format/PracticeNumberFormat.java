package fr.bobinho.luxepractice.utils.format;

import java.text.DecimalFormat;

public class PracticeNumberFormat {

    /**
     * Gets a float rounded to two decimal places
     *
     * @param number the number to format
     * @return the number rounded to two decimal places
     */
    public static float getAsTwoDecimalsFormat(float number) {
        return Float.parseFloat(new DecimalFormat("#.##").format(number));
    }
}
