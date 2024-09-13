package org.parmenid.jsontestmapper.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.parmenid.jsontestmapper.JsonTestMapper;

class JsonArgumentsProviderTest {

    /**
     * @param testObject is mapped object from json file
     */
    @JsonTestMapper(resources = "/single-object.json")
    @DisplayName("provides single object from file")
    void singleObjectFromFile(TestObject testObject) {
        Assertions.assertEquals(testObject.getKey(), "value");
    }

    /**
     * @param testObject is mapped object from json string
     */
    @JsonTestMapper(resources = "{\"key\":\"value\"}")
    @DisplayName("provides single object from string")
    void singleObjectFromString(TestObject testObject) {
        Assertions.assertEquals(testObject.getKey(), "value");
    }

    /**
     * @param testObject is mapped each object in array from json file
     */
    @JsonTestMapper(resources = "/array-of-objects.json")
    @DisplayName("provides single object for each array element from file")
    void arrayOftObjectsFromFile(TestObject testObject) {
        Assertions.assertTrue(testObject.getKey().startsWith("value"));
    }

    /**
     * @param testObject is mapped each object in array from json string
     */
    @JsonTestMapper(resources = "[{\"key\":\"value1\"}, {\"key\":\"value\"}]")
    @DisplayName("provides single object for each array element from string")
    void arrayOftObjectsFromString(TestObject testObject) {
        Assertions.assertTrue(testObject.getKey().startsWith("value"));
    }

    /**
     * @param number is mapped each number in array from json file
     */
    @JsonTestMapper(resources = "/array-of-numbers.json")
    @DisplayName("provides single number for each array element from file")
    void arrayOfNumbersFromFile(Integer number) {
        Assertions.assertTrue(number > 0);
    }

    /**
     * @param number is mapped each number in array from json string
     */
    @JsonTestMapper(resources = "[1,2,3]")
    @DisplayName("provides single number for each array element from string")
    void arrayOfNumbersFromString(Integer number) {
        Assertions.assertTrue(number > 0);
    }

    /**
     * @param string is mapped each string in array from json file
     */
    @JsonTestMapper(resources = "/array-of-strings.json")
    @DisplayName("provides single string for each array element from file")
    void arrayOfStringsFromFile(String string) {
        Assertions.assertTrue(string.startsWith("value"));
    }

    /**
     * @param string is mapped each string in array from json string
     */
    @JsonTestMapper(resources = "[\"value1\", \"value2\", \"value3\"]")
    @DisplayName("provides single string for each array element from string")
    void arrayOfStringsFromString(String string) {
        Assertions.assertTrue(string.startsWith("value"));
    }

    /**
     * @param strings is mapped array of string from json string
     */
    @JsonTestMapper(resources = "[\"value1\", \"value2\", \"value3\"]")
    @DisplayName("provides single string for each array element from string")
    void arrayOfStringsFromString(String[] strings) {
        Assertions.assertEquals(3, strings.length);
    }
}
