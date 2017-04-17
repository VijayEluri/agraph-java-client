/******************************************************************************
** See the file LICENSE for the full license governing this code.
******************************************************************************/

package test;

import com.franz.agraph.jena.*;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDFS;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class UntypedLiteralMatchingTest extends AGAbstractTest {

    /**
     * This was a failing ARQ test sent in by Holger (spr38458)
     * 
     */
	@Test
	@Category(TestSuites.Broken.class)
	public void testARQUntypedLiteralMatching() {
		// Model model = AG.getInstance().createModel("http://aldi.com.au");
		AGGraphMaker maker = closeLater(new AGGraphMaker(conn));
		AGGraph graph = closeLater(maker.createGraph("http://aldi.com.au"));
		AGModel model = closeLater(new AGModel(graph));

		String str = "MatchThis";
		model.begin();
		Resource subject = OWL.Thing;
		Property predicate = RDFS.label;
		RDFNode typedObject = ResourceFactory.createTypedLiteral(str);
		model.add(subject, predicate, typedObject);
		model.commit();

		Query query = QueryFactory.create("ASK WHERE { <" + subject + "> <"
				+ predicate + "> ?x }");

		// This works fine
		{
			QuerySolutionMap initialBinding = new QuerySolutionMap();
			initialBinding.add("x", typedObject);
			QueryExecution qexec = QueryExecutionFactory.create(query, model,
					initialBinding);
			Assert.assertTrue(qexec.execAsk());
		}

		// This fails
		{
			QuerySolutionMap initialBinding = new QuerySolutionMap();
			initialBinding.add("x", ResourceFactory.createPlainLiteral(str));
			QueryExecution qexec = QueryExecutionFactory.create(query, model,
					initialBinding);
			Assert.assertTrue(qexec.execAsk());
		}

		model.begin();
		model.removeAll();
		model.commit();
		model.abort();
	}

	@Test
    @Category(TestSuites.Prepush.class)
    /**
     * This is the AG equivalent of that test (rfe10983)
     */
	public void testAGUntypedLiteralMatching() {
		// Model model = AG.getInstance().createModel("http://aldi.com.au");
		AGGraphMaker maker = closeLater(new AGGraphMaker(conn));
		AGGraph graph = closeLater(maker.createGraph("http://aldi.com.au"));
		AGModel model = closeLater(new AGModel(graph));

		String str = "MatchThis";
		model.begin();
		Resource subject = OWL.Thing;
		Property predicate = RDFS.label;
		RDFNode typedObject = ResourceFactory.createTypedLiteral(str);
		model.add(subject, predicate, typedObject);
		model.commit();

		AGQuery query = AGQueryFactory.create("ASK WHERE { <" + subject + "> <"
				+ predicate + "> ?x }");

		// This works fine
		{
			QuerySolutionMap initialBinding = new QuerySolutionMap();
			initialBinding.add("x", typedObject);
			AGQueryExecution qexec = AGQueryExecutionFactory.create(query,
					model, initialBinding);
			Assert.assertTrue(qexec.execAsk());
		}

		// This works too
		{
			QuerySolutionMap initialBinding = new QuerySolutionMap();
			initialBinding.add("x", ResourceFactory.createPlainLiteral(str));
			AGQueryExecution qexec = AGQueryExecutionFactory.create(query,
					model, initialBinding);
			Assert.assertTrue(qexec.execAsk());
		}

		model.begin();
		model.removeAll();
		model.commit();
		model.abort();
	}

}
