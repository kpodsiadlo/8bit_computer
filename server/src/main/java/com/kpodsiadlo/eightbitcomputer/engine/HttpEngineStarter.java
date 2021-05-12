package com.kpodsiadlo.eightbitcomputer.engine;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

public class HttpEngineStarter {

    public static final Logger logger = LoggerFactory.getLogger(HttpEngineStarter.class);
    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient();
    private final String computerUri = "http://127.0.0.1:5000/";

    public void start(String uuid) {

        RequestBody formBody = new FormBody.Builder()
                .add("uuid", uuid)
                .build();

        Request request = new Request.Builder()
                .url(computerUri)
                .post(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if  (!(response).isSuccessful()) throw new IOException("Unexpected code " + response);
            if (response.isSuccessful()) {
                String message = Objects.requireNonNull(response.body()).string();
                logger.info("{}", message);
        }
    } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
