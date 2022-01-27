package fr.bobinho.luxepractice.utils.format;

public class PracticeDurationFormat {

    public static String getAsMinuteSecondFormat(long durationInSecond) {
        int minutes = (int) durationInSecond / 60;
        int seconds = (int) durationInSecond % 60;

        return minutes + " minutes " +  seconds + " seconds";
    }

}
