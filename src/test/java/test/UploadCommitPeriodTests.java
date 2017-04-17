/******************************************************************************
** See the file LICENSE for the full license governing this code.
******************************************************************************/

package test;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openrdf.model.URI;
import org.openrdf.rio.RDFFormat;

public class UploadCommitPeriodTests extends AGAbstractTest {

    @Test
    @Category(TestSuites.Prepush.class)
    public void uploadCommitPeriod_rfe10059() throws Exception {
    	String path = "/tutorial/java-kennedy.ntriples";
		Assert.assertEquals("expected commit period 0", 0, conn.getUploadCommitPeriod());
		URI g_0 = vf.createURI("urn:x-allegrograph:0");
		Util.add(conn, path, null, RDFFormat.NTRIPLES,g_0);
		long size_0 = conn.size(g_0);
		conn.setUploadCommitPeriod(100);
		Assert.assertEquals("expected commit period 100", 100, conn.getUploadCommitPeriod());
		Util.add(conn, path, null, RDFFormat.NTRIPLES,vf.createURI("urn:x-allegrograph:100auto"));
		conn.setAutoCommit(false);
		URI g_100nonauto = vf.createURI("urn:x-allegrograph:100nonauto");
		Assert.assertEquals("expected 0 triples in context", 0, conn.size(g_100nonauto));
		Util.add(conn, path, null, RDFFormat.NTRIPLES,g_100nonauto);
		conn.rollback();
		Assert.assertEquals("expected triples committed", size_0, conn.size(g_100nonauto));
    }

}
