package com.brainacad;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpClientHelper {

    public static HttpResponse get(String endpointUrl) throws IOException {
        return get(endpointUrl, null);
    }

    public static HttpResponse get(String endpointUrl, String parameters) throws IOException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return get(endpointUrl, parameters, headers);
    }

    //REST GET запрос
    public static HttpResponse get(String endpointUrl, String parameters, Map<String, String> headers) throws IOException {
        //Создаём экземпляр HTTP клиента
        HttpClient client = HttpClientBuilder.create().build();
        //Создаём HTTP GET запрос из URL и параметров
        HttpGet request;
        if(parameters != null) {
            request = new HttpGet(endpointUrl + "?" + parameters);
        }else request = new HttpGet(endpointUrl);

        //добавляем в запрос необходимые хедеры
        for (String headerKey : headers.keySet()) {
            request.addHeader(headerKey, headers.get(headerKey));
        }

        //выполняем запрос в HTTP клиенте и получаем ответ
        HttpResponse response = client.execute(request);

        //возвращаем response
        return response;
    }


    public static HttpResponse delayedGet(String endpointUrl) throws IOException {
        return get(endpointUrl, null);
    }

    public static HttpResponse delayedGet(String endpointUrl, String parameters) throws IOException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return get(endpointUrl, parameters, headers);
    }

    public static HttpResponse delayedGet(String endpointUrl, String parameters, Map<String, String> headers) throws IOException {
        HttpResponse httpResponse;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(1000)
                    .setConnectTimeout(1000)
                    .setSocketTimeout(1000)
                    .build();

            final HttpGet httpGet;
            if(parameters != null) {
                httpGet = new HttpGet(endpointUrl + "?" + parameters);
            }else httpGet = new HttpGet(endpointUrl);

            for (String headerKey : headers.keySet()) {
                httpGet.addHeader(headerKey, headers.get(headerKey));
            }

            httpGet.setConfig(requestConfig);
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                EntityUtils.consumeQuietly(response.getEntity());
                httpResponse = response;
            }
        }
        return httpResponse;
    }


    public static HttpResponse post(String endpointUrl, String body) throws IOException {
        //TODO: написать метод для POST запроса с хедерами по умолчанию
        //Создаём переменую headers типа Map
        Map<String, String> headers = new HashMap<>();
        //Добавляем в headers наш заголовок
        headers.put("Content-Type", "application/json");
        return post(endpointUrl, body, headers);
    }

    public static HttpResponse post(String endpointUrl, String body, Map<String, String> headers) throws IOException {
        //Создаём экземпляр HTTP клиента
        HttpClient client = HttpClientBuilder.create().build();
        //Создаём HTTP POST запрос из URL и параметров
        HttpPost post = new HttpPost(endpointUrl);

        //добавляем в запрос необходимые хедеры
        for (String headerKey : headers.keySet()) {
            post.addHeader(headerKey, headers.get(headerKey));
        }

        //добавляем к запросу тело запроса
        post.setEntity(new StringEntity(body));

        //выполняем запрос в HTTP клиенте и получаем ответ
        HttpResponse response = client.execute(post);

        //возвращаем response
        return response;
    }


    public static String getBodyFromResponse(HttpResponse response) throws IOException {
        //создаём ридер буффера и передаём в него входящий поток респонса
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line;

        //получаем в цикле построчно строки из входящего потока и собираем в одну строку
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }

    //TODO: допишите методы для запросов PUT, PATCH и DELETE
    public static HttpResponse put(String endpointUrl, String body) throws IOException {
        //Создаём переменую headers типа Map
        Map<String, String> headers = new HashMap<>();
        //Добавляем в headers наш заголовок
        headers.put("Content-Type", "application/json");

        //возвращаем response
        return put(endpointUrl, body, headers);
    }

    public static HttpResponse put(String endpointUrl, String body, Map<String, String> headers) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPut put = new HttpPut(endpointUrl);
        for (String headerKey : headers.keySet()) {
            put.addHeader(headerKey, headers.get(headerKey));
        }
        put.setEntity(new StringEntity(body));

        HttpResponse response = client.execute(put);
        return response;
    }

    public static HttpResponse patch(String endpointUrl, String body) throws IOException {
        //Создаём переменую headers типа Map
        Map<String, String> headers = new HashMap<>();
        //Добавляем в headers наш заголовок
        headers.put("Content-Type", "application/json");

        //возвращаем response
        return patch(endpointUrl, body, headers);
    }

    public static HttpResponse patch(String endpointUrl, String body, Map<String, String> headers) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPatch patch = new HttpPatch(endpointUrl);
        for (String headerKey : headers.keySet()) {
            patch.addHeader(headerKey, headers.get(headerKey));
        }
        patch.setEntity(new StringEntity(body));

        HttpResponse response = client.execute(patch);
        return response;
    }

    public static HttpResponse delete(String endpointUrl) throws IOException {
        //Создаём переменую headers типа Map
        Map<String, String> headers = new HashMap<>();
        //Добавляем в headers наш заголовок
        headers.put("Content-Type", "application/json");

        //возвращаем response
        return delete(endpointUrl, headers);
    }

    public static HttpResponse delete(String endpointUrl, Map<String, String> headers) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpDelete delete = new HttpDelete(endpointUrl);
        for (String headerKey : headers.keySet()) {
            delete.addHeader(headerKey, headers.get(headerKey));
        }

        HttpResponse response = client.execute(delete);
        return response;
    }
}
