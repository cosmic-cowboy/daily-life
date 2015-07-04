package com.slgerkamp.daily.life;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <p>daily life のアプリケーションです。
 *
 */
@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)
public class Application {

	// CheckStyleの誤認防止用（ユーティリティクラスと誤認識するため）
	public int dummy = 0;

	public static void main(final String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}

}
