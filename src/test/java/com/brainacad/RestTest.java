package com.brainacad;

import org.apache.http.HttpResponse;
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class RestTest{

    private static final String URL="https://reqres.in/";

    @Test//GET метод
    public void checkGetResponseStatusCode() throws IOException {
        String endpoint="/api/users";

        //TODO: Избавится он хедеров в тесте добавив методы с хедерами по умолчанию в класс HttpClientHelper
        //Выполняем REST GET запрос с нашими параметрами
        // и сохраняем результат в переменную response.
        HttpResponse response = HttpClientHelper.get(URL+endpoint,"page=2");

        //получаем статус код из ответа
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Response Code : " + statusCode);
        Assert.assertEquals("Response status code should be 200", 200, statusCode);
    }

    @Test//GET метод
    public void checkGetResponseBodyNotNull() throws IOException {
        String endpoint="/api/users";
        HttpResponse response = HttpClientHelper.get(URL+endpoint,"page=2");

        //Конвертируем входящий поток тела ответа в строку
        String body=HttpClientHelper.getBodyFromResponse(response);
        System.out.println(body);
        Assert.assertNotEquals("Body shouldn't be null", null, body);
    }

    @Test//POST метод
    public void checkPostResponseStatusCode() throws IOException {
        String endpoint="/api/users";

        //TODO: Избавится он хедеров в тесте добавив методы с хедерами по умолчанию в класс HttpClientHelper

        //создаём тело запроса
        String requestBody="{\"name\": \"morpheus\",\"job\": \"leader\"}";

        //Выполняем REST POST запрос с нашими параметрами
        // и сохраняем результат в переменную response.
        HttpResponse response = HttpClientHelper.post(URL+endpoint,requestBody);

        //получаем статус код из ответа
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Response Code : " + statusCode);
        Assert.assertEquals("Response status code should be 201", 201, statusCode);
    }

    @Test//POST метод
    public void checkPostResponseBodyNotNull() throws IOException {
        String endpoint="/api/users";
        String requestBody="{\"name\": \"morpheus\",\"job\": \"leader\"}";

        HttpResponse response = HttpClientHelper.post(URL+endpoint,requestBody);

        //Конвертируем входящий поток тела ответа в строку
        String body=HttpClientHelper.getBodyFromResponse(response);
        System.out.println(body);
        Assert.assertNotEquals("Body shouldn't be null", null, body);
    }

    //TODO: напишите по тесткейсу на каждый вариант запроса на сайте https://reqres.in
    @Test//Put метод
    public void checkPutResponseStatusCode() throws IOException {
        String endpoint="/api/users";
        String path = "/2";
        String requestBody="{\"name\": \"morpheus\",\"job\": \"zion resident\"}";

        //TODO: Избавится он хедеров в тесте добавив методы с хедерами по умолчанию в класс HttpClientHelper
        //Выполняем REST GET запрос с нашими параметрами
        // и сохраняем результат в переменную response.
        HttpResponse response = HttpClientHelper.put(URL+endpoint+path,requestBody);

        //получаем статус код из ответа
        int statusCode = response.getStatusLine().getStatusCode();
        String body = HttpClientHelper.getBodyFromResponse(response);
        String job = JsonUtils.stringFromJSONByPath(body, "job");

        System.out.println("Response Code : " + statusCode);
        Assert.assertEquals("Response status code should be 200", 200, statusCode);

        System.out.println("Job in response is : " + job);
        Assert.assertEquals("Response should contain Job : \"zion resident\"", "zion resident", job);
    }


    //TODO: в тескейсах проверьте Result Code и несколько параметров из JSON ответа (если он есть)
    @Test//POST метод
    public void checkPostResponseParams() throws IOException {
        String endpoint="/api/users";
        //TODO: Избавится он хедеров в тесте добавив методы с хедерами по умолчанию в класс HttpClientHelper
        //создаём тело запроса
        String requestBody="{\"name\": \"morpheus\",\"job\": \"leader\"}";
        //Выполняем REST POST запрос с нашими параметрами
        // и сохраняем результат в переменную response.
        HttpResponse response = HttpClientHelper.post(URL+endpoint,requestBody);

        //получаем статус код из ответа
        String body = HttpClientHelper.getBodyFromResponse(response);
        String name = JsonUtils.stringFromJSONByPath(body, "name");
        String job = JsonUtils.stringFromJSONByPath(body, "job");
        int statusCode = response.getStatusLine().getStatusCode();

        System.out.println("Response Code : " + statusCode);
        Assert.assertEquals("Response status code should be 201", 201, statusCode);

        System.out.println("Name in response is : " + name);
        Assert.assertEquals("Response should contain Name : \"morpheus\"", "morpheus", name);

        System.out.println("Job in response is : " + job);
        Assert.assertEquals("Response should contain Job : \"leader\"", "leader", job);
    }
}
