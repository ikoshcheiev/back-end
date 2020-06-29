package com.brainacad;

import org.apache.http.HttpResponse;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;


public class RestTest {

    private static final String URL = "https://reqres.in/";

    @Test//GET метод
    public void checkGetResponseStatusCode() throws IOException {
        String endpoint = "/api/users";

        //TODO: Избавится он хедеров в тесте добавив методы с хедерами по умолчанию в класс HttpClientHelper
        //Выполняем REST GET запрос с нашими параметрами
        // и сохраняем результат в переменную response.
        HttpResponse response = HttpClientHelper.get(URL + endpoint, "page=2");
        //much quick the .get 2s380ms vs 1s 800ms
        //HttpResponse response = HttpClientHelper.delayedGet(URL + endpoint, "page=2");

        //получаем статус код из ответа
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Response Code : " + statusCode);
        Assert.assertEquals("Response status code should be 200", 200, statusCode);
    }

    @Test//GET метод
    public void checkGetResponseBodyNotNull() throws IOException {
        String endpoint = "/api/users";
        HttpResponse response = HttpClientHelper.get(URL + endpoint, "page=2");

        //Конвертируем входящий поток тела ответа в строку
        String body = HttpClientHelper.getBodyFromResponse(response);
        System.out.println(body);
        Assert.assertNotEquals("Body shouldn't be null", null, body);
    }

    @Test//POST метод
    public void checkPostResponseStatusCode() throws IOException {
        String endpoint = "/api/users";

        //создаём тело запроса
        String requestBody = "{\"name\": \"morpheus\",\"job\": \"leader\"}";

        //Выполняем REST POST запрос с нашими параметрами
        // и сохраняем результат в переменную response.
        HttpResponse response = HttpClientHelper.post(URL + endpoint, requestBody);

        //получаем статус код из ответа
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Response Code : " + statusCode);
        Assert.assertEquals("Response status code should be 201", 201, statusCode);
    }

    @Test//POST метод
    public void checkPostResponseBodyNotNull() throws IOException {
        String endpoint = "/api/users";
        String requestBody = "{\"name\": \"morpheus\",\"job\": \"leader\"}";

        HttpResponse response = HttpClientHelper.post(URL + endpoint, requestBody);

        //Конвертируем входящий поток тела ответа в строку
        String body = HttpClientHelper.getBodyFromResponse(response);
        System.out.println(body);
        Assert.assertNotEquals("Body shouldn't be null", null, body);
    }

    //TODO: в тескейсах проверьте Result Code и несколько параметров из JSON ответа (если он есть)
    @Test//POST метод
    public void checkPostResponseParams() throws IOException {
        String endpoint = "/api/users";

        //создаём тело запроса
        String requestBody = "{\"name\": \"morpheus\",\"job\": \"leader\"}";
        //Выполняем REST POST запрос с нашими параметрами
        // и сохраняем результат в переменную response.
        HttpResponse response = HttpClientHelper.post(URL + endpoint, requestBody);

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

    //TODO: напишите по тесткейсу на каждый вариант запроса на сайте https://reqres.in
    @Test//Put метод
    public void checkPutResponse() throws IOException {
        String endpoint = "/api/users";
        String path = "/2";
        String requestBody = "{\"name\": \"morpheus\",\"job\": \"zion resident\"}";

        HttpResponse response = HttpClientHelper.put(URL + endpoint + path, requestBody);

        //получаем статус код из ответа
        int statusCode = response.getStatusLine().getStatusCode();
        String body = HttpClientHelper.getBodyFromResponse(response);
        String job = JsonUtils.stringFromJSONByPath(body, "job");

        System.out.println("Response Code : " + statusCode);
        Assert.assertEquals("Response status code should be 200", 200, statusCode);

        System.out.println("Job in response is : " + job);
        Assert.assertEquals("Response should contain Job : \"zion resident\"", "zion resident", job);
    }

    @Test// 2 Get метод
    public void checkGetResponseSingleUser() throws IOException {
        int userId = 2;
        String endpoint = "/api/users";
        String path = "/" + userId;

        HttpResponse response = HttpClientHelper.get(URL + endpoint + path);

        //получаем статус код из ответа
        int statusCode = response.getStatusLine().getStatusCode();
        String body = HttpClientHelper.getBodyFromResponse(response);
        int id = JsonUtils.intFromJSONByPath(body, "$.data.id");

        System.out.println("Response Code : " + statusCode);
        Assert.assertEquals("Response status code should be 200", 200, statusCode);

        System.out.println("Id in response is : " + userId);
        Assert.assertEquals("Response should contain userId : \"2\"", userId, id);
    }

    @Test// 3 Get метод
    public void checkGetResponseSingleUserNotFound() throws IOException {
        int userId = 32;
        String endpoint = "/api/users";
        String path = "/" + userId;

        HttpResponse response = HttpClientHelper.get(URL + endpoint + path);

        //получаем статус код из ответа 404
        int statusCode = response.getStatusLine().getStatusCode();
        String body = HttpClientHelper.getBodyFromResponse(response);
        //List<Object> list = JsonUtils.listFromJSONByPath(body, "$"); //?? hot to find all. An error appears com.jayway.jsonpath.InvalidPathException: Path must not end with a '.' or '..'

        System.out.println("Response Code : " + statusCode);
        Assert.assertEquals("Response status code should be 404", 404, statusCode);

        System.out.println("Body in response should be empty : " + body);
        Assert.assertTrue("Response should contain empty list", body.equals("{}"));
    }

    @Test// 4 Get метод
    public void checkGetResponseList() throws IOException {
        String endpoint = "/api/unknown";
        HttpResponse response = HttpClientHelper.get(URL + endpoint);

        int statusCode = response.getStatusLine().getStatusCode();//200
        String body = HttpClientHelper.getBodyFromResponse(response);
        List<Object> list = JsonUtils.listFromJSONByPath(body, "$.data[*].id");

        System.out.println("Response Code : " + statusCode);
        Assert.assertEquals("Response status code should be 200", 200, statusCode);

        System.out.println("Body in response should be not empty : " + list);
        Assert.assertFalse("Response should not be empty", list.isEmpty());
    }

    @Test// 5 Get метод
    public void checkGetResponseSingleResource() throws IOException {
        String endpoint = "/api/unknown";
        String path = "/2";
        HttpResponse response = HttpClientHelper.get(URL + endpoint + path);

        int statusCode = response.getStatusLine().getStatusCode();//200
        String body = HttpClientHelper.getBodyFromResponse(response);
        int id = JsonUtils.intFromJSONByPath(body, "$.data.id");

        System.out.println("Response Code : " + statusCode);
        Assert.assertEquals("Response status code should be 200", 200, statusCode);

        System.out.println("Body in response should contain only single resource : " + body);
        Assert.assertEquals("Response should contain single correcy id", id, 2);
    }

    @Test// 6 Get метод
    public void checkGetResponseSingleResourceNotFound() throws IOException {
        String endpoint = "/api/unknown";
        String path = "/23";
        HttpResponse response = HttpClientHelper.get(URL + endpoint + path);

        int statusCode = response.getStatusLine().getStatusCode();//404
        String body = HttpClientHelper.getBodyFromResponse(response);

        System.out.println("Response Code : " + statusCode);
        Assert.assertEquals("Response status code should be 404", 404, statusCode);

        System.out.println("Body in response should be empty : " + body);
        Assert.assertEquals("Response should contain empty body", "{}", body);
    }

    //@Test//Patch метод
    @Test//Patch метод
    public void checkPatchResponse() throws IOException {
        String endpoint = "/api/users";
        String path = "/2";
        String requestBody = "{\"name\": \"morpheus\",\"job\": \"zion resident\"}";

        HttpResponse response = HttpClientHelper.patch(URL + endpoint + path, requestBody);

        //получаем статус код из ответа
        int statusCode = response.getStatusLine().getStatusCode();
        String body = HttpClientHelper.getBodyFromResponse(response);
        String job = JsonUtils.stringFromJSONByPath(body, "job");

        System.out.println("Response Code : " + statusCode);
        Assert.assertEquals("Response status code should be 200", 200, statusCode);

        System.out.println("Body is not empty. Job: " + job);
        Assert.assertEquals("Response should contain Job : \"zion resident\"", "zion resident", job);
    }

    //@Test//Delete метод
    @Test//Delete метод
    public void checkDeleteResponse() throws IOException {
        String endpoint = "/api/users";
        String path = "/2";

        HttpResponse response = HttpClientHelper.delete(URL + endpoint + path);

        //получаем статус код из ответа
        int statusCode = response.getStatusLine().getStatusCode();//204
        System.out.println("Response Code : " + statusCode);
        Assert.assertEquals("Response status code should be 204", 204, statusCode);

        try {
            String body = HttpClientHelper.getBodyFromResponse(response);
        } catch (java.lang.NullPointerException e) {
            System.out.println("Body is absent :" + e.toString());
            //????how to make checking here?
        }

    }

    @Test//POST метод
    public void checkPostRegisterSuccessful() throws IOException {
        String endpoint = "/api/register";
        String requestBody = "{\"email\": \"eve.holt@reqres.in\",\"password\": \"pistol\"}";

        HttpResponse response = HttpClientHelper.post(URL + endpoint, requestBody);

        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Response Code : " + statusCode);
        Assert.assertEquals("Response status code should be 200", 200, statusCode);

        String body = HttpClientHelper.getBodyFromResponse(response);
        List<Object> values = JsonUtils.listFromJSONByPath(body, "$[*]");

        System.out.print("New id created: " + values.get(0));
        Assert.assertEquals("New user should be created with unique Id", 4, values.get(0));
    }

    @Test//POST метод
    public void checkPostRegisterUnsuccessful() throws IOException {
        String endpoint = "/api/register";
        String requestBody = "{\"email\": \"sydney@fife\"}";

        HttpResponse response = HttpClientHelper.post(URL + endpoint, requestBody);

        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Response Code : " + statusCode);
        Assert.assertEquals("Response status code should be 400", 400, statusCode);

        String body = HttpClientHelper.getBodyFromResponse(response);
        List<Object> values = JsonUtils.listFromJSONByPath(body, "$[*]");

        System.out.print("Response body contains: " + body);
        Assert.assertEquals("An error message should appear", "Missing password", values.get(0));
    }

    @Test//POST метод
    public void checkPostLoginSuccessful() throws IOException {
        String endpoint = "/api/login";
        String requestBody = "{\"email\": \"eve.holt@reqres.in\",\"password\": \"cityslicka\"}";

        HttpResponse response = HttpClientHelper.post(URL + endpoint, requestBody);

        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Response Code : " + statusCode);
        Assert.assertEquals("Response status code should be 200", 200, statusCode);

        String body = HttpClientHelper.getBodyFromResponse(response);

        System.out.print("Body is not empty: " + body);
        Assert.assertTrue("Token should appear in response", body.contains("token"));
    }

    @Test//POST метод
    public void checkPostLoginUnsuccessful() throws IOException {
        String endpoint = "/api/login";
        String requestBody = "{\"email\": \"peter@klaven\"}";

        HttpResponse response = HttpClientHelper.post(URL + endpoint, requestBody);

        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Response Code : " + statusCode);
        Assert.assertEquals("Response status code should be 400", 400, statusCode);

        String body = HttpClientHelper.getBodyFromResponse(response);
        List<Object> values = JsonUtils.listFromJSONByPath(body, "$[*]");

        System.out.print("Response body contains: " + body);
        Assert.assertEquals("An error message should appear", "Missing password", values.get(0));
    }

    @Test//GET метод
    public void checkGetDelayedResponse() throws IOException {
        String endpoint = "/api/users";
        HttpResponse response = HttpClientHelper.delayedGet(URL + endpoint, "delay=3");

        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Response Code : " + statusCode);
        Assert.assertEquals("Response status code should be 200", 200, statusCode);

        String body = HttpClientHelper.getBodyFromResponse(response);
        List<Object> ids = JsonUtils.listFromJSONByPath(body, "$.data[*].id");

        System.out.println("Body in response : " + body);
        Assert.assertTrue("Response should contain list of ids", ids.size() > 1);
    }

}