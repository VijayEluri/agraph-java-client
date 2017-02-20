/******************************************************************************
 ** Copyright (c) 2008-2016 Franz Inc.
 ** All rights reserved. This program and the accompanying materials
 ** are made available under the terms of the Eclipse Public License v1.0
 ** which accompanies this distribution, and is available at
 ** http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/

package test;

import com.franz.agraph.repository.AGGraphQuery;
import com.franz.agraph.repository.AGTupleQuery;
import info.aduna.iteration.Iterations;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openrdf.model.Literal;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFFormat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests handling of non-ascii characters with various RDF formats.
 */
@RunWith(Parameterized.class)
public class UnicodeRDFFormatTest extends AGAbstractTest {
    private final RDFFormat format;
    private RDFFormat oldFormat;

    // @Parameters(name="{index}: {0}") -- need newer JUnit for that?
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { RDFFormat.NQUADS },
                { RDFFormat.TRIX }
        });
    }

    @Before
    public void setUpFormat() {
        oldFormat = conn.getHttpRepoClient().getPreferredRDFFormat();
        conn.getHttpRepoClient().setPreferredRDFFormat(format);
    }

    @After
    public void tearDownFormat() {
        conn.getHttpRepoClient().setPreferredRDFFormat(oldFormat);
    }

    public UnicodeRDFFormatTest(final RDFFormat format) {
        this.format = format;
    }

    @Test
    public void testAddUnicodeLiteral() throws RepositoryException {
        URI s = vf.createURI("http://franz.com/s");
        URI p = vf.createURI("http://franz.com/p");
        Literal o = vf.createLiteral("जुप");
        conn.add(s, p, o);
        List<Statement> result = Iterations.asList(conn.getStatements(s, p, null, false));
        assertThat(result.size(), is(1));
        assertThat(result.get(0).getObject().stringValue(), is("जुप"));
    }

    @Test
    public void testAddUnicodeSubject() throws RepositoryException {
        URI s = vf.createURI("http://franz.com/जुप");
        URI p = vf.createURI("http://franz.com/p");
        Literal o = vf.createLiteral("o");
        conn.add(s, p, o);
        List<Statement> result = Iterations.asList(conn.getStatements(s, p, null, false));
        assertThat(result.size(), is(1));
        assertThat(result.get(0).getSubject().stringValue(), is("http://franz.com/जुप"));
    }

    @Test
    public void testUnicodeCreate() throws RepositoryException, QueryEvaluationException {
        AGGraphQuery query = conn.prepareGraphQuery(QueryLanguage.SPARQL,
                "CONSTRUCT { <s> <p> \"जुप\"} WHERE {}");
        List<Statement> result = Iterations.asList(query.evaluate());
        assertThat(result.size(), is(1));
        assertThat(result.get(0).getObject().stringValue(), is("जुप"));
    }
}