package fr.bobinho.luxepractice.utils.format;

public class PracticeDurationFormat {

    /**
     * Gets a duration string in minutes - seconds format
     *
     * @param durationInSecond the duration in second to format
     * @return the formatted string in minutes - seconds format
     */
    public static String getAsMinuteSecondFormat(long durationInSecond) {
        return ((int) durationInSecond / 60) + " minutes " +  ((int) durationInSecond % 60) + " seconds";
    }

}
