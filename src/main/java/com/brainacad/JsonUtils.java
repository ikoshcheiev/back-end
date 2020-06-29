package com.brainacad;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import java.util.List;

public class JsonUtils {
    //TODO: Используя библиотеку com.jayway.jsonpath (Maven) напишите метод извлечения строки из JSON по JSON Path:

    public static String stringFromJSONByPath(String json, String jsonPath) {
        return JsonPath.read(json, jsonPath);
    }


    //TODO: Используя библиотеку com.jayway.jsonpath (Maven) напишите метод извлечения int из JSON по JSON Path:

    public static int intFromJSONByPath(String json, String jsonPath) throws NumberFormatException {
        DocumentContext context = JsonPath.parse(json);
        return context.read(jsonPath);
    }


    //TODO: Используя библиотеку com.jayway.jsonpath (Maven) напишите метод извлечения double из JSON по JSON Path:
    public static double doubleFromJSONByPath(String json, String jsonPath) throws NumberFormatException {
        DocumentContext jsonContext = JsonPath.parse(json);
        return jsonContext.read(jsonPath);
    }


    //TODO: Используя библиотеку com.jayway.jsonpath (Maven) напишите метод извлечения списка (List) из JSON по JSON Path:
    public static List<Object> listFromJSONByPath(String json, String jsonPath) throws NumberFormatException {
        DocumentContext jsonContext = JsonPath.parse(json);
        //String jsonPathCreatorName = jsonContext.read(jsonPath);
        List<Object> jsonpathCreatorLocation = jsonContext.read(jsonPath); // example of correct path: "$.data[*].id"

        return jsonpathCreatorLocation;
    }

}
