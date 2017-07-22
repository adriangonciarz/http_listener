import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MyHandler  implements HttpHandler {
    private List<Long> timestamps = new ArrayList<>();

    public void handle(HttpExchange t) throws IOException {
        String response = "This is a sample response";
        Long timestamp = System.currentTimeMillis();

        timestamps.add(timestamp);

        t.sendResponseHeaders(200, response.length());
        System.out.printf("%s: Request received.", timestamp);

        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    Long[] returnRequestTimestamps() {
        return timestamps.toArray(new Long[timestamps.size()]);
    }
}