/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.mongodb.repository.support;

import org.springframework.data.mongodb.core.MongoOperations;

import com.google.common.base.Function;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.querydsl.mongodb.AbstractMongodbQuery;

/**
 * Spring Data specific {@link MongodbQuery} implementation.
 * 
 * @author Oliver Gierke
 */
public class SpringDataMongodbQuery<T> extends AbstractMongodbQuery<T, SpringDataMongodbQuery<T>> {

	private final MongoOperations operations;

	/**
	 * Creates a new {@link SpringDataMongodbQuery}.
	 * 
	 * @param operations must not be {@literal null}.
	 * @param type must not be {@literal null}.
	 */
	public SpringDataMongodbQuery(final MongoOperations operations, final Class<? extends T> type) {
		this(operations, type, operations.getCollectionName(type));
	}

	/**
	 * Creates a new {@link SpringDataMongodbQuery} to query the given collection.
	 * 
	 * @param operations must not be {@literal null}.
	 * @param type must not be {@literal null}.
	 * @param collectionName must not be {@literal null} or empty.
	 */
	public SpringDataMongodbQuery(final MongoOperations operations, final Class<? extends T> type,
			String collectionName) {

		super(operations.getCollection(collectionName), new Function<DBObject, T>() {
			public T apply(DBObject input) {
				return operations.getConverter().read(type, input);
			}
		}, new SpringDataMongodbSerializer(operations.getConverter()));

		this.operations = operations;
	}

	/*
	 * (non-Javadoc)
	 * @see com.querydsl.mongodb.AbstractMongodbQuery#getCollection(java.lang.Class)
	 */
	@Override
	protected DBCollection getCollection(Class<?> type) {
		return operations.getCollection(operations.getCollectionName(type));
	}
}
