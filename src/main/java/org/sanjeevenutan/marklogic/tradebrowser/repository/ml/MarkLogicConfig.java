package org.sanjeevenutan.marklogic.tradebrowser.repository.ml;

import org.sanjeevenutan.marklogic.tradebrowser.domain.Trade;
import org.sanjeevenutan.marklogic.tradebrowser.repository.TradeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.DatabaseClientFactory.Authentication;
import com.marklogic.client.pojo.PojoRepository;
import com.marklogic.client.query.QueryManager;

@Configuration
public class MarkLogicConfig {
		
	@Bean 
	public QueryManager qm()
	{
		return client().newQueryManager();
	}
	
	@Bean
	public DatabaseClient client()
	{
		return DatabaseClientFactory.newClient("localhost", 8004, "admin", "admin", Authentication.DIGEST);
	}
	
	@Bean
    public PojoRepository<Trade, Long> tradeRepo() {		
		return client().newPojoRepository(Trade.class, Long.class);
    }	
	
}
