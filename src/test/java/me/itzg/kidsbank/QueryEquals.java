package me.itzg.kidsbank;

import org.easymock.IArgumentMatcher;
import org.springframework.data.mongodb.core.query.Query;

public class QueryEquals implements IArgumentMatcher {
	private Query expected;
	
	public QueryEquals(Query expected) {
		this.expected = expected;
	}

	public void appendTo(StringBuffer buffer) {
		buffer.append("eqQuery(");
		buffer.append(expected.getQueryObject().toString());
		buffer.append(")");
	}

	public boolean matches(Object actual) {
		Query actualQuery = (Query) actual;
		return actualQuery.getQueryObject().toMap().equals(expected.getQueryObject().toMap());
	}

}
