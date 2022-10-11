package telegrambot.service;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public class HttpService {

    public String get(String url) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);

            try (CloseableHttpResponse response1 = httpClient.execute(httpGet)) {
                HttpEntity entity1 = response1.getEntity();

                String result = IOUtils.toString(entity1.getContent(), StandardCharsets.UTF_8);
                EntityUtils.consume(entity1);

                return result;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return  "";
    }

}
