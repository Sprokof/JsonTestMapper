package org.parmenid.jsontestmapper;

import java.rmi.AccessException;
import java.util.regex.Pattern;

final class JsonMapperUtil {
    private static final Pattern BRACKET_PATTERN = Pattern.compile("(\\[|\\])");
    private JsonMapperUtil() throws AccessException {
        throw new AccessException("Constructor can't be created");
    }
    public static boolean isArray(String json) {
        json = json.trim();
        int length = json.length();
        String firstJsonChar = String.valueOf(json.charAt(0));
        String lastJsonChar = String.valueOf(json.charAt(length - 1));
        return BRACKET_PATTERN.matcher(firstJsonChar).find() && BRACKET_PATTERN.matcher(lastJsonChar).find();
    }

    public static boolean isArrayOrCollection(Class<?> target) {
        if (target.isArray()) {
            return true;
        }
        for (Class<?> c : target.getInterfaces()) {
            if (c.getSimpleName().contains("Collection")) {
                return true;
            }
        }
        return false;
    }
    public static boolean resourceIsFile(String[] resources) {
        String filePattern = "(/?).+\\.(json)";
        return resources[0].matches(filePattern);

    }
}
