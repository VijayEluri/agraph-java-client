/******************************************************************************
** Copyright (c) 2008-2016 Franz Inc.
** All rights reserved. This program and the accompanying materials
** are made available under the terms of the Eclipse Public License v1.0
** which accompanies this distribution, and is available at
** http://www.eclipse.org/legal/epl-v10.html
******************************************************************************/

package test;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openrdf.model.Statement;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.RepositoryResult;

import org.openrdf.rio.RDFFormat;
import com.franz.agraph.repository.AGRepositoryConnection;

public class DeleteDuplicatesTests extends AGAbstractTest {

	@Test
	@Category(TestSuites.Prepush.class)
	public void testDuplicateSuppressionPolicy() throws Exception {
		Assert.assertEquals("expected false","false",repo.getDuplicateSuppressionPolicy());
		repo.setDuplicateSuppressionPolicy("spog");
		Assert.assertEquals("expected spog","spog",repo.getDuplicateSuppressionPolicy());
		conn.add(new File("src/test/example.nq"), null, RDFFormat.NQUADS);
		Assert.assertEquals("expected size 10", 10, conn.size());
		conn.add(new File("src/test/example.nq"), null, RDFFormat.NQUADS);
		Assert.assertEquals("expected size 15", 15, conn.size());
		repo.setDuplicateSuppressionPolicy("spo");
		Assert.assertEquals("expected spo","spo",repo.getDuplicateSuppressionPolicy());
		repo.setDuplicateSuppressionPolicy("false");
		Assert.assertEquals("expected false","false",repo.getDuplicateSuppressionPolicy());
	}
	
    @Test
    @Category(TestSuites.Prepush.class)
    public void testSPOG() throws Exception {
        conn.add(new File("src/test/example.nq"), null, RDFFormat.NQUADS);
        Assert.assertEquals("expected size 10", 10, conn.size());
        conn.add(new File("src/test/example.nq"), null, RDFFormat.NQUADS);
        Assert.assertEquals("expected size 20", 20, conn.size());

	RepositoryResult<Statement> result = conn.getDuplicateStatements("spog");
	int count = 0;
	for ( ; result.hasNext() ; ++count ) result.next();
	Assert.assertEquals("expected duplicate count 5", 5, count);
	
        conn.deleteDuplicates("spog");
        // Note: this doesn't result in 10 triples, due to blank nodes. 
        Assert.assertEquals("expected size 15", 15, conn.size());
   }

    @Test
    @Category(TestSuites.Prepush.class)
    public void testSPO() throws Exception {
    	conn.add(new File("src/test/example.nq"), null, RDFFormat.NQUADS);
    	Assert.assertEquals("expected size 10", 10, conn.size());
    	// add an spo duplicate
    	conn.add(vf.createURI("http://example.org/alice/foaf.rdf#me"),
    			RDF.TYPE,vf.createURI("http://xmlns.com/foaf/0.1/Person"));
    	Assert.assertEquals("expected size 11", 11, conn.size());

    	conn.deleteDuplicates("spog");
    	// there are no spog duplicates
    	Assert.assertEquals("expected size 11", 11, conn.size());

	RepositoryResult<Statement> result = conn.getDuplicateStatements("spo");
	int count = 0;
	for ( ; result.hasNext() ; ++count ) result.next();
	Assert.assertEquals("expected duplicate count 1", 1, count);

    	conn.deleteDuplicates("spo");
    	Assert.assertEquals("expected size 10", 10, conn.size());
    }
    
    @Test
    @Category(TestSuites.Broken.class)
    public void testDefault() throws Exception {
    	conn.add(new File("src/test/example.nq"), null, RDFFormat.NQUADS);
    	Assert.assertEquals("expected size 10", 10, conn.size());
    	// add an spo duplicate
    	conn.add(vf.createURI("http://example.org/alice/foaf.rdf#me"),
    			RDF.TYPE,vf.createURI("http://xmlns.com/foaf/0.1/Person"));
    	Assert.assertEquals("expected size 11", 11, conn.size());
    	// null is the default, "spog"
    	conn.deleteDuplicates(null);
    	// there are no spog duplicates
    	Assert.assertEquals("expected size 11", 11, conn.size());
    }
    
    @Test
    @Category(TestSuites.Prepush.class)
    public void testCommit() throws Exception {
    	conn.add(new File("src/test/example.nq"), null, RDFFormat.NQUADS);
    	Assert.assertEquals("expected size 10", 10, conn.size());
    	AGRepositoryConnection conn2 = repo.getConnection();
    	conn.setAutoCommit(false);
    	conn.add(new File("src/test/example.nq"), null, RDFFormat.NQUADS);
    	Assert.assertEquals("expected size 20", 20, conn.size());
    	conn.deleteDuplicates("spog");
    	// Note: this doesn't result in 10 triples, due to blank nodes. 
    	Assert.assertEquals("expected size 15", 15, conn.size());
    	Assert.assertEquals("expected conn2 size 10", 10, conn2.size());
    	conn.commit();
    	Assert.assertEquals("expected size 15", 15, conn.size());
    	Assert.assertEquals("expected conn2 size 15", 15, conn2.size());
    }
    
    @Test
    @Category(TestSuites.Prepush.class)
    public void testRollback() throws Exception {
    	conn.add(new File("src/test/example.nq"), null, RDFFormat.NQUADS);
    	Assert.assertEquals("expected size 10", 10, conn.size());
    	conn.add(new File("src/test/example.nq"), null, RDFFormat.NQUADS);
    	Assert.assertEquals("expected size 20", 20, conn.size());
    	AGRepositoryConnection conn2 = repo.getConnection();
    	conn.setAutoCommit(false);
    	conn.deleteDuplicates("spog");
    	// Note: this doesn't result in 10 triples, due to blank nodes. 
    	Assert.assertEquals("expected size 15", 15, conn.size());
    	Assert.assertEquals("expected conn2 size 20", 20, conn2.size());
    	conn.rollback();
    	Assert.assertEquals("expected size 20", 20, conn.size());
    }

}
