import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class App {
    private static final String URL =
            "https://api.nasa.gov/planetary/apod?api_key=xMNoHfnYeY2Tot2gKgW7Zaw3SkCwyrMjo727Ha3b";

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();
        HttpGet requestNasa = new HttpGet(URL);
        CloseableHttpResponse response = httpClient.execute(requestNasa);

        ObjectMapper mapper = new ObjectMapper();
        Nasa nasa = mapper.readValue(response.getEntity().getContent(), Nasa.class);

        HttpGet requestContent = new HttpGet(nasa.getUrl());
        CloseableHttpResponse content = httpClient.execute(requestContent);

        String fileName = nasa.getUrl()
                .substring((nasa.getUrl().lastIndexOf("/")) + 1);
        BufferedImage image = ImageIO.read(content.getEntity().getContent());
        ImageIO.write(image, "jpg", new File(fileName));
    }
}
