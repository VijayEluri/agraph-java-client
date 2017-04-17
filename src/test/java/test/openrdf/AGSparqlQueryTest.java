/******************************************************************************
** See the file LICENSE for the full license governing this code.
******************************************************************************/

package test.openrdf;

import com.franz.agraph.http.exception.AGHttpException;
import com.franz.agraph.repository.AGServer;
import junit.framework.Test;
import org.openrdf.query.Dataset;
import org.openrdf.query.parser.sparql.manifest.SPARQLQueryTest;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import test.AGAbstractTest;

public class AGSparqlQueryTest extends SPARQLQueryTest {

	public static Test suite()
		throws Exception
	{
		return test.openrdf.SPARQL11ManifestTest.suite(new Factory() {

			public AGSparqlQueryTest createSPARQLQueryTest(String testURI, String name,
					String queryFileURL, String resultFileURL, Dataset dataSet, boolean laxCardinality)
			{
				return createSPARQLQueryTest(testURI, name, queryFileURL, resultFileURL, dataSet, laxCardinality, false);
			}
			
			public AGSparqlQueryTest createSPARQLQueryTest(String testURI, String name,
					String queryFileURL, String resultFileURL, Dataset dataSet, boolean laxCardinality, boolean checkOrder)
			{
				return new AGSparqlQueryTest(testURI, name, queryFileURL, resultFileURL, dataSet,
						laxCardinality, checkOrder);
			}
		}, true, true, true);
	}

	private AGSparqlQueryTest(String testURI, String name, String queryFileURL, String resultFileURL,
			Dataset dataSet, boolean laxCardinality)
	{
		this(testURI, name, queryFileURL, resultFileURL, dataSet, laxCardinality, false);
	}

	private AGSparqlQueryTest(String testURI, String name, String queryFileURL, String resultFileURL,
			Dataset dataSet, boolean laxCardinality, boolean checkOrder)
	{
		super(testURI, name, queryFileURL, resultFileURL, dataSet, laxCardinality, checkOrder);
	}
	
	protected Repository newRepository() {
		String repoName = this.getClass().getSimpleName();
		try {
			return new AGServer(AGAbstractTest.findServerUrl(),AGAbstractTest.username(), AGAbstractTest.password()).getCatalog(AGAbstractTest.CATALOG_ID).createRepository(repoName);
		} catch (AGHttpException e) {
			throw new RuntimeException(e);
		} catch (RepositoryException e) {
			throw new RuntimeException(e);
		}
	}
}
