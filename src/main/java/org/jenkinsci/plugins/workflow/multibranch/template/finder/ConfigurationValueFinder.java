package org.jenkinsci.plugins.workflow.multibranch.template.finder;

/**
 * Finds a key's value from a string in many config formats (e.g. properties, yaml, json)
 */
public class ConfigurationValueFinder {

    private ConfigurationValueFinder() {}

    /**
     * This method will find the <b>first</b> given {@code keyToFind} in {@code configurationContents} and then
     * return the value of it assuming that the key/value is represented in the following formats:
     * <ul>
     * <li>"key" : "value"
     * <li>'key' : 'value'
     * <li>key : value
     * <li>key:value
     * <li>key=value
     * </ul>
     * Whitespace and " and ' characters are removed.
     * @param configurationContents the configuration text to search
     * @param keyToFind the key to find in the {@code configurationContents}
     * @return value of the associated {@code keyToFind}; returns null if not found
     */
    public static String findFirstConfigurationValue(String configurationContents, String keyToFind) {
        if (keyToFind == null || configurationContents == null) {
            return null;
        }
        int indexOfKey = configurationContents.indexOf(keyToFind);
        if (indexOfKey == -1) {
            return null;
        }
        int indexAfterKey = keyToFind.length() + indexOfKey;
        int firstNewLine = configurationContents.indexOf("\n", indexAfterKey);
        String delimiterAndValueString = configurationContents.substring(indexAfterKey, firstNewLine);
        String valueWithoutDelimitersQuotesAndTicks = removeDelimitersQuotesAndTicks(delimiterAndValueString);
        if (valueWithoutDelimitersQuotesAndTicks == null) {
            return null;
        }
        return valueWithoutDelimitersQuotesAndTicks.trim();
    }

    /**
     * Remove colon or equal delimiters as well as "s and 's from the {@code inputString}
     * @param inputString small string which potentially contains a colon or equals sign
     * @return string without delimiters
     */
    private static String removeDelimitersQuotesAndTicks(String inputString) {
        String charsToRemove = ":='\"";
        if (inputString == null) {
            return null;
        }
        StringBuilder outputString = new StringBuilder();
        for (char character : inputString.toCharArray()) {
            // If character isn't in charsToRemove
            if (charsToRemove.indexOf(character) == -1) {
                outputString.append(character);
            }
        }
        return outputString.toString();
    }
}
