package com.slgerkamp.daily.life.generic.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * <p>DatabaseのConfigurationを設定します。</p>
 *
 */
@Configuration
public class DatabaseConfiguration {

	@Autowired
	DataSourceProperties properties;
	DataSource dataSource;

	@Bean(destroyMethod = "close")
	DataSource realDataSource() throws URISyntaxException {
		String url;
		String username;
		String password;

		String databaseUrl = System.getenv("DATABASE_URL");
		if (databaseUrl != null) {
			URI dbUri = new URI(databaseUrl);
			url = "jdbc:postgresql://" + dbUri.getHost() + ":"
					+ dbUri.getPort() + dbUri.getPath();
			username = dbUri.getUserInfo().split(":")[0];
			password = dbUri.getUserInfo().split(":")[1];
		} else {
			url = this.properties.getUrl();
			username = this.properties.getUsername();
			password = this.properties.getPassword();
		}

		DataSourceBuilder factory = DataSourceBuilder
				.create(this.properties.getClassLoader()).url(url)
				.username(username).password(password);
		this.dataSource = factory.build();
		return this.dataSource;
	}
//
//	@Bean
//	DataSource dataSource() {
//		return new DataSource(this.dataSource);
//	}
}
