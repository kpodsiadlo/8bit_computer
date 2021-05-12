package com.kpodsiadlo.eightbitcomputer.engine;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

public class EngineRestClient {

    public static final Logger logger = LoggerFactory.getLogger(EngineRestClient.class);
    private static final OkHttpClient client = new OkHttpClient();
    private static final String computerUri = "localhost";

    public static void sendControlMessage(
            EngineControlMessage controlMessage, String targetId) {

        String method = getMethodName(controlMessage);

        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(computerUri)
                .port(5000)
                .addQueryParameter("uuid", targetId)
                .build();

        RequestBody requestBody = RequestBody.create(new byte[0]);

        Request request = new Request.Builder()
                .url(url)
                .method(method, requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!(response).isSuccessful())
                throw new IOException("Unexpected code " + response);
            if (response.isSuccessful()) {
                String message = Objects.requireNonNull(response.body()).string();
                logger.info("{}", message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getMethodName (EngineControlMessage message) {
        if (message == EngineControlMessage.START) {
            return "POST";
        }
        return "DELETE";
    }
}
