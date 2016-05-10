package me.itzg.kidsbank;

import org.easymock.EasyMock;
import org.springframework.data.mongodb.core.query.Query;

public abstract class MongoMatchers {

	/**
	 * Used with EasyMock to assert that the given Query has the same criteria content. The sort and fields
	 * content is not evaluated. 
	 * @param in
	 * @return
	 */
	public static Query eqQuery(Query in) {
		EasyMock.reportMatcher(new QueryEquals(in));
		return null;
	}

}
