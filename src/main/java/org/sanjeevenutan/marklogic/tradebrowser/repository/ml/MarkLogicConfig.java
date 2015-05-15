package org.sanjeevenutan.marklogic.tradebrowser.repository.ml;

import org.sanjeevenutan.marklogic.tradebrowser.domain.Trade;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.DatabaseClientFactory.Authentication;
import com.marklogic.client.pojo.PojoRepository;
import com.marklogic.client.query.QueryManager;

@Configuration
public class MarkLogicConfig implements EnvironmentAware {
	
	private String hostname;
	private int port;
	private String username;
	private String password;
	
	private Environment env;
    private RelaxedPropertyResolver propertyResolver;
	
    @Override
	public void setEnvironment(Environment environment) {
		this.env = env;
	}
    
	
	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	

	
}
