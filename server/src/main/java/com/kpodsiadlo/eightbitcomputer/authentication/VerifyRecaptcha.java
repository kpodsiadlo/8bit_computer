package com.kpodsiadlo.eightbitcomputer.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class VerifyRecaptcha {


    public static final String URL = "https://www.google.com/recaptcha/api/siteverify";
    public static final String SECRET = "6LcD5NcaAAAAANjiI8Y8v-vnRuZwyG9Ok3gvI5V7";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Logger logger = LoggerFactory.getLogger(VerifyRecaptcha.class);


    public static boolean verify(String gRecaptchaResponse) throws IOException {
        if (gRecaptchaResponse == null || "".equals(gRecaptchaResponse)) {
            return false;
        }

        final HttpUrl parse = HttpUrl.parse(URL);

        HttpUrl apiUrl = parse.newBuilder()
                .addQueryParameter("secret", SECRET)
                .addQueryParameter("response", gRecaptchaResponse)
                .build();

        logger.info(String.valueOf(apiUrl));


        RequestBody emptyBody = RequestBody.create(new byte[0]);

        Request request = new Request.Builder()
                .url(apiUrl)
                .method("POST", emptyBody)
                .build();

        try (Response apiResp = client.newCall(request).execute()) {
            if (!apiResp.isSuccessful()) {
                throw new IOException("Unexpected code " + apiResp);
            } else {
                final String responseBody = apiResp.body().string();
                ObjectMapper mapper = new ObjectMapper();
                final RecaptchaResponse recaptchaResponse = mapper.readValue(
                        responseBody, RecaptchaResponse.class);
                return recaptchaResponse.isSuccess();
            }
        }
    }
}


