package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// import the Prometheus packages.
import io.prometheus.client.spring.boot.EnablePrometheusEndpoint;
import io.prometheus.client.spring.boot.EnableSpringBootMetricsCollector;
// Prometheus counter package.
import io.prometheus.client.Counter;
// Prometheus Histogram package.
import io.prometheus.client.Histogram;

@SpringBootApplication
@RestController
// Add a Prometheus metrics enpoint to the route `/prometheus`. `/metrics` is already taken by Actuator.
@EnablePrometheusEndpoint
// Pull all metrics from Actuator and expose them as Prometheus metrics. Need to disable security feature in properties file.
@EnableSpringBootMetricsCollector

public class Application {
	// Define a counter metric for /prometheus
	static final Counter requests = Counter.build()
			.name("requests_total").help("Total number of requests.").register();
	// Define a histogram metric for /prometheus
	static final Histogram requestLatency = Histogram.build()
			.name("requests_latency_seconds").help("Request latency in seconds.").register()
	@RequestMapping("/")
	public String home() {
		// Increase the counter metric
		requests.inc();
		// Start the histogram timer
		Histogram.Timer requestTimer = requestLatency.startTimer();
		try {
		return "This is a Monitoring Project";
		} finally {
			// Stop the histogram timer
			requestTimer.observeDuration();
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
