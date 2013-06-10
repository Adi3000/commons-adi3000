package com.adi3000.common.database.hibernate.postgres;

import org.hibernate.dialect.PostgreSQL82Dialect;

public class PostgresEnhancedSQLDialect extends PostgreSQL82Dialect {
	public PostgresEnhancedSQLDialect() {
		super();
		registerFunction("fulltextsearch", new PostgreSQLFullTextSearchFunction());
	}
}
