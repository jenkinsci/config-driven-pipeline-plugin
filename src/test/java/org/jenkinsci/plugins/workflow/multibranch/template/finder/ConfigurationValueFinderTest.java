package org.jenkinsci.plugins.workflow.multibranch.template.finder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationValueFinderTest {

    /**
     * Simple helper method to call ConfigurationValueFinder.findFirstConfigurationValue
     * to improve test readability.
     *
     * @param configurationContents test config
     * @param keyToFind test key
     * @return evaluated result
     */
    private String find(String configurationContents, String keyToFind) {
        return ConfigurationValueFinder.findFirstConfigurationValue(configurationContents, keyToFind);
    }

    @Test
    void findNullKeyOutOfConfigReturnsNull() {
        assertNull(find("test", null));
    }

    @Test
    void findStringKeyOutOfNullConfigReturnsNull() {
        assertNull(find(null, "test1"));
    }

    @Test
    void cannotFindKeyReturnsNull() {
        assertNull(find("keynotfound", "test1"));
    }

    @Test
    void foundFirstKey() {
        assertEquals("hi",
                find("bare:hi\n test1:second\n test1:third",
                        "bare"));
    }

    @Test
    void foundKeyWithQuotesAroundKey() {
        assertEquals("hi",
                find("\"quotes\":\"hi\"\n test1:second\n test1:third",
                        "quotes"));
    }

    @Test
    void foundKeyWithTicksAroundKey() {
        assertEquals("hi",
                find("'ticks':'hi'\n test1:second\n test1:third",
                        "ticks"));
    }

    @Test
    void foundKeyWithTicksAroundKeyAndManySpaces() {
        assertEquals("hi",
                find("'spaces' :          'hi'        \n test1:second\n test1:third",
                        "spaces"));
    }

    @Test
    void foundKeyWithEquals() {
        assertEquals("hi",
                find("equals=\"hi\"\ntest1=second\ntest2=third",
                        "equals"));
    }

    @Test
    void foundKeyWithSpacesAroundEquals() {
        assertEquals("hi",
                find("equals_space = \"hi\"\ntest1 = second\ntest2 = third",
                        "equals_space"));
    }
}