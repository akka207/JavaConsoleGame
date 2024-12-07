package staticClasses;

public class Colors {
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public static final String BLACK_BACKGROUND = "\u001B[40m";
    public static final String RED_BACKGROUND = "\u001B[41m";
    public static final String GREEN_BACKGROUND = "\u001B[42m";
    public static final String YELLOW_BACKGROUND = "\u001B[43m";
    public static final String BLUE_BACKGROUND = "\u001B[44m";
    public static final String PURPLE_BACKGROUND = "\u001B[45m";
    public static final String CYAN_BACKGROUND = "\u001B[46m";
    public static final String WHITE_BACKGROUND = "\u001B[47m";

    public static final String RESET = "\u001B[0m";

    private static final int COLOR_CODE_LENGTH = 4;


    public static String Format(String text, String color) {
        return color + text + RESET;
    }

    public static String Reformat(String text) {
        if (text.endsWith(RESET))
            return text.substring(COLOR_CODE_LENGTH, COLOR_CODE_LENGTH + text.length());
        else
            return text;
    }

    public static String GetColor(String text){
        if (text.endsWith(RESET))
            return text.substring(0, COLOR_CODE_LENGTH);
        else
            return Colors.WHITE;
    }
}
