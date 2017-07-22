import com.sun.net.httpserver.HttpServer;
import org.assertj.core.data.Percentage;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RequestingTest {
    private final static long LISTENING_TIME = 30000;
    private final static long EXPECTED_INTERVAL_MS = 2000;
    private final static Double ALLOWED_PERCENTAGE = 50.0;

    @Test
    public void shouldRequestForItemsEvery5Seconds() throws IOException, InterruptedException {
        MyHandler handler = new MyHandler();

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/all_items", handler);
        server.start();

        Thread.sleep(LISTENING_TIME);

        Long[] timestamps = handler.returnRequestTimestamps();
        Long[] intervals = calculateIntervalsBetweenRequests(timestamps);

        assertThat(intervals.length).
                as("Number of requests collected").
                isGreaterThan(2);

        assertThat(intervals).as("Intervals between requests").allSatisfy(
                i -> assertThat(i).isCloseTo(EXPECTED_INTERVAL_MS, Percentage.withPercentage(ALLOWED_PERCENTAGE))
        );
    }

    private Long[] calculateIntervalsBetweenRequests(Long[] timestamps){
        List<Long> intervals = new ArrayList<>();
        for(int i=1; i<timestamps.length; i++){
            intervals.add(timestamps[i] - timestamps[i-1]);
        }
        return intervals.toArray(new Long[intervals.size()]);
    }
}
