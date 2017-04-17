/******************************************************************************
** See the file LICENSE for the full license governing this code.
******************************************************************************/

package com.franz.agraph.repository;

import org.openrdf.query.GraphQuery;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.impl.GraphQueryResultImpl;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.helpers.StatementCollector;

import com.franz.agraph.http.handler.AGLongHandler;
import com.franz.agraph.http.handler.AGRDFHandler;

/**
 * Implements the Sesame GraphQuery interface for AllegroGraph.
 * 
 */
public class AGGraphQuery extends AGQuery implements GraphQuery {

	/**
	 * Creates an AGGraphQuery instance for the given connection.
	 * 
	 * @param con  the connection
	 * @param ql  the query language
	 * @param queryString  the query
	 * @param baseURI  the base URI for the query
	 */
	public AGGraphQuery(AGRepositoryConnection con, QueryLanguage ql,
			String queryString, String baseURI) {
		super(con, ql, queryString, baseURI);
	}

	/**
	 * Evaluates the query and returns a GraphQueryResult.
	 */
	public GraphQueryResult evaluate() throws QueryEvaluationException {
		try {
			// TODO: make this efficient for large result sets
			StatementCollector collector = new StatementCollector();
			evaluate(collector);
			return new GraphQueryResultImpl(collector.getNamespaces(),
					collector.getStatements());
		} catch (RDFHandlerException e) {
			// Found a bug in StatementCollector?
			throw new RuntimeException(e);
		}
	}

	/**
	 * Evaluates the query and uses handler to process the result. 
	 */
	public void evaluate(RDFHandler handler) throws QueryEvaluationException,
			RDFHandlerException {
		final RDFFormat format = httpCon.getHttpRepoClient().getPreferredRDFFormat();
		evaluate(new AGRDFHandler(format, handler, httpCon.getValueFactory(),httpCon.getHttpRepoClient().getAllowExternalBlankNodeIds()));
	}

	/**
	 * Evaluates the query and returns only the number of results
	 * to the client (counting is done on the server, the results
	 * are not returned).
	 * 
	 * @return long  the number of results
	 * @throws QueryEvaluationException  if an error occurs while evaluating the query
	 */
	public long count() throws QueryEvaluationException {
		AGLongHandler handler = new AGLongHandler();
		evaluate(handler);
		return handler.getResult();
	}
}
