package sh.idktheflag.idk.api.utils.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Http {
    private static final HttpClient CLIENT = HttpClient.newBuilder()
        .executor(Executors.newCachedThreadPool())
        .build();

    private static final Gson GSON = new GsonBuilder().create();

    public static class Request {
        private final HttpRequest.Builder builder;
        private String method;
        private Consumer<Exception> exceptionHandler = Exception::printStackTrace;

        private Request(String method, String url) {
            try {
                this.builder = HttpRequest.newBuilder().uri(new URI(url)).header("User-Agent", "Mozilla/5.0");
                this.method = method;
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException(e);
            }
        }

        public Request bearer(String token) {
            builder.header("Authorization", "Bearer " + token);
            return this;
        }

        public Request bodyForm(String string) {
            builder.header("Content-Type", "application/x-www-form-urlencoded");
            builder.method(method, HttpRequest.BodyPublishers.ofString(string));
            method = null;
            return this;
        }

        public Request bodyJson(String string) {
            builder.header("Content-Type", "application/json");
            builder.method(method, HttpRequest.BodyPublishers.ofString(string));
            method = null;
            return this;
        }

        public <T> T sendJson(Class<T> klass) {
            if (method != null) builder.method(method, HttpRequest.BodyPublishers.noBody());
            try {
                HttpResponse<String> response = CLIENT.send(builder.build(), HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    return GSON.fromJson(response.body(), klass);
                }
            } catch (IOException | InterruptedException e) {
                exceptionHandler.accept(e);
            }
            return null;
        }

        public <T> T sendJson(Type type) {
            if (method != null) builder.method(method, HttpRequest.BodyPublishers.noBody());
            try {
                HttpResponse<String> response = CLIENT.send(builder.build(), HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    return GSON.fromJson(response.body(), type);
                }
            } catch (IOException | InterruptedException e) {
                exceptionHandler.accept(e);
            }
            return null;
        }

        public String sendString() {
            if (method != null) builder.method(method, HttpRequest.BodyPublishers.noBody());
            try {
                HttpResponse<String> response = CLIENT.send(builder.build(), HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    return response.body();
                }
            } catch (IOException | InterruptedException e) {
                exceptionHandler.accept(e);
            }
            return null;
        }
    }

    public static Request get(String url) {
        return new Request("GET", url);
    }

    public static Request post(String url) {
        return new Request("POST", url);
    }
}
