package cat.aubricoc.holcost.util;

public class StringUtils {

	public static final String EMPTY = "";

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static boolean areEmpty(String... strs) {
		for (String str : strs) {
			if (!isEmpty(str)) {
				return false;
			}
		}
		return true;
	}

	public static boolean areNotEmpty(String... strs) {
		for (String str : strs) {
			if (isEmpty(str)) {
				return false;
			}
		}
		return true;
	}

	public static String join(Object[] array, String separator) {
		boolean isFirst = true;
		String result = "";
		for (Object object : array) {
			if (isFirst) {
				isFirst = false;
			} else {
				result += separator;
			}
			result += object.toString();
		}
		return result;
	}
	
	public static String cleanToNumber(String text) {
		String number = "";
		for (char c : text.toCharArray()) {
			if (isAsciiNumeric(c)) {
				number += c;
			}
		}
		if (isEmpty(number)) {
			return null;
		}
		return number;
	}
	
	public static boolean isAsciiNumeric(char ch) {
        return ch >= '0' && ch <= '9';
    }
}
