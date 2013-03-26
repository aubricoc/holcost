package cat.aubricoc.holcost.util;

public class StringUtils {

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
    
    public static boolean equals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }
}
