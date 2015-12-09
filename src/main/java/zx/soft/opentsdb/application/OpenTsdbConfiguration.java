package zx.soft.opentsdb.application;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

public class OpenTsdbConfiguration extends Configuration {

	@NotEmpty
	@JsonProperty
	private String opentsdbip;

	@NotEmpty
	@JsonProperty
	private String opentsdbport;

	public String getBaseUrl() {
		String baseUrl = opentsdbip + ":" + opentsdbport;
		return (baseUrl);
	}
}
