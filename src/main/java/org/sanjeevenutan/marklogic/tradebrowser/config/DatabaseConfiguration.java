package org.sanjeevenutan.marklogic.tradebrowser.config;

import java.util.Arrays;

import javax.sql.DataSource;

import liquibase.integration.spring.SpringLiquibase;

import org.sanjeevenutan.marklogic.tradebrowser.domain.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.DatabaseClientFactory.Authentication;
import com.marklogic.client.pojo.PojoRepository;
import com.marklogic.client.query.QueryManager;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableJpaRepositories("org.sanjeevenutan.marklogic.tradebrowser.repository")
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnableTransactionManagement
@EnableElasticsearchRepositories("org.sanjeevenutan.marklogic.tradebrowser.repository.search")
public class DatabaseConfiguration implements EnvironmentAware {

	private final Logger log = LoggerFactory
			.getLogger(DatabaseConfiguration.class);

	private RelaxedPropertyResolver propertyResolver;

	private Environment env;

	@Autowired(required = false)
	private MetricRegistry metricRegistry;

	@Override
	public void setEnvironment(Environment env) {
		this.env = env;
		this.propertyResolver = new RelaxedPropertyResolver(env,
				"spring.datasource.");
	}

	@Bean(destroyMethod = "shutdown")
	@ConditionalOnMissingClass(name = "org.sanjeevenutan.marklogic.tradebrowser.config.HerokuDatabaseConfiguration")
	@Profile("!" + Constants.SPRING_PROFILE_CLOUD)
	public DataSource dataSource() {
		log.debug("Configuring Datasource");
		if (propertyResolver.getProperty("url") == null
				&& propertyResolver.getProperty("databaseName") == null) {
			log.error(
					"Your database connection pool configuration is incorrect! The application"
							+ " cannot start. Please check your Spring profile, current profiles are: {}",
					Arrays.toString(env.getActiveProfiles()));

			throw new ApplicationContextException(
					"Database connection pool is not configured correctly");
		}
		HikariConfig config = new HikariConfig();
		config.setDataSourceClassName(propertyResolver
				.getProperty("dataSourceClassName"));
		if (StringUtils.isEmpty(propertyResolver.getProperty("url"))) {
			config.addDataSourceProperty("databaseName",
					propertyResolver.getProperty("databaseName"));
			config.addDataSourceProperty("serverName",
					propertyResolver.getProperty("serverName"));
		} else {
			config.addDataSourceProperty("url",
					propertyResolver.getProperty("url"));
		}
		config.addDataSourceProperty("user",
				propertyResolver.getProperty("username"));
		config.addDataSourceProperty("password",
				propertyResolver.getProperty("password"));

		// MySQL optimizations, see
		// https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration
		if ("com.mysql.jdbc.jdbc2.optional.MysqlDataSource"
				.equals(propertyResolver.getProperty("dataSourceClassName"))) {
			config.addDataSourceProperty("cachePrepStmts",
					propertyResolver.getProperty("cachePrepStmts", "true"));
			config.addDataSourceProperty("prepStmtCacheSize",
					propertyResolver.getProperty("prepStmtCacheSize", "250"));
			config.addDataSourceProperty("prepStmtCacheSqlLimit",
					propertyResolver.getProperty("prepStmtCacheSqlLimit",
							"2048"));
			config.addDataSourceProperty("useServerPrepStmts",
					propertyResolver.getProperty("useServerPrepStmts", "true"));
		}
		if (metricRegistry != null) {
			config.setMetricRegistry(metricRegistry);
		}
		return new HikariDataSource(config);
	}

	@Bean
	public SpringLiquibase liquibase(DataSource dataSource) {
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setDataSource(dataSource);
		liquibase.setChangeLog("classpath:config/liquibase/master.xml");
		liquibase.setContexts("development, production");
		if (env.acceptsProfiles(Constants.SPRING_PROFILE_FAST)) {
			if ("org.h2.jdbcx.JdbcDataSource".equals(propertyResolver
					.getProperty("dataSourceClassName"))) {
				liquibase.setShouldRun(true);
				log.warn(
						"Using '{}' profile with H2 database in memory is not optimal, you should consider switching to"
								+ " MySQL or Postgresql to avoid rebuilding your database upon each start.",
						Constants.SPRING_PROFILE_FAST);
			} else {
				liquibase.setShouldRun(false);
			}
		} else {
			log.debug("Configuring Liquibase");
		}
		return liquibase;
	}

	@Bean
	public Hibernate4Module hibernate4Module() {
		return new Hibernate4Module();
	}

	@Bean
	public QueryManager qm() {
		return client().newQueryManager();
	}

	@Bean
	public DatabaseClient client() {
		return DatabaseClientFactory.newClient(
				propertyResolver.getProperty("ml-hostname"),
				Integer.valueOf(propertyResolver.getProperty("ml-port")),
				propertyResolver.getProperty("ml-username"),
				propertyResolver.getProperty("ml-password"),
				Authentication.DIGEST);
	}

	@Bean
	public PojoRepository<Trade, Long> tradeRepo() {
		return client().newPojoRepository(Trade.class, Long.class);
	}

}
