package com.adi3000.common.database.hibernate.postgres;

import java.util.List;

import org.hibernate.QueryException;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.BooleanType;
import org.hibernate.type.Type;

public class PostgreSQLFullTextSearchFunction implements SQLFunction {

	@Override
	public boolean hasArguments() {
		return true;
	}

	@Override
	public boolean hasParenthesesIfNoArguments() {
		return false;
	}

	@Override
	public Type getReturnType(Type firstArgumentType, Mapping mapping)
			throws QueryException {
		return new BooleanType();

	}

	@Override
	public String render(Type firstArgumentType, @SuppressWarnings("rawtypes") List arguments,
			SessionFactoryImplementor factory) throws QueryException {
		if (arguments.size() != 3) {
			throw new IllegalArgumentException(
					"The function must be passed 3 arguments");
		}

		String ftsConfig = (String) arguments.get(0);
		String field = (String) arguments.get(1);
		String value = (String) arguments.get(2);

		String fragment = null;
		if (ftsConfig == null) {
			fragment = "to_tsvector(" + field + ") @@ " + "to_tsquery('"
					+ value + "')";
		} else {
			fragment = "to_tsvector(" + ftsConfig + "::regconfig, " + field + ") @@ "
					+ "to_tsquery(" + ftsConfig + ", " + value + ")";
		}
		return fragment;
	}

}
