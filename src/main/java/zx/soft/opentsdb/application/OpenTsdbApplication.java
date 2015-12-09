package zx.soft.opentsdb.application;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import jersey.repackaged.com.google.common.collect.ImmutableMap;
import zx.soft.opentsdb.client.OpenTsdbClient;
import zx.soft.opentsdb.main.OpenTsdbMain;
import zx.soft.opentsdb.reporter.OpenTsdbReporter;

public class OpenTsdbApplication extends Application<OpenTsdbConfiguration> {
	private static final Logger logger = LoggerFactory.getLogger(OpenTsdbMain.class);
	static final MetricRegistry metrics = new MetricRegistry();

	public static void main(String[] args) throws Exception {
		ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics).convertRatesTo(TimeUnit.SECONDS)
				.convertDurationsTo(TimeUnit.MILLISECONDS).build();
		metrics.register("jvm.mem", new MemoryUsageGaugeSet());
		metrics.register("jvm.gc", new GarbageCollectorMetricSet());
		reporter.start(30, TimeUnit.SECONDS);
		
		new OpenTsdbApplication().run(args);
		TimeUnit.SECONDS.sleep(500);
	}

	@Override
	public void initialize(Bootstrap<OpenTsdbConfiguration> bootstrap) {
	}

	@Override
	public void run(OpenTsdbConfiguration configuration, Environment environment) throws Exception {
		environment.jersey().register(OpenTsdbClient.class);
		OpenTsdbReporter.forRegistry(environment.metrics()).prefixedWith("app_name")
				.withTags(ImmutableMap.of("other", "tags"))
				.build(OpenTsdbClient.forService(configuration.getBaseUrl()).create()).start(30L, TimeUnit.SECONDS);
	}
}
