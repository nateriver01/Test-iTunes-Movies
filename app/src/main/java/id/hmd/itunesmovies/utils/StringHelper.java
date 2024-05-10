package id.hmd.itunesmovies.utils;

public class StringHelper {

    public static void getStringBuilderToString(String... items) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : items) {
            stringBuilder.append(s);
        }
        //stringBuilder.toString();
    }



}