/******************************************************************************
** See the file LICENSE for the full license governing this code.
******************************************************************************/

package test;

import com.franz.agraph.repository.AGCatalog;
import com.franz.agraph.repository.AGRepository;
import com.franz.agraph.repository.AGServer;
import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.ExcludeCategory;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
import org.eclipse.rdf4j.repository.Repository;
import test.TestSuites.NonPrepushTest;

import java.io.File;

public class AGRepositoryConnectionTest extends RepositoryConnectionTest {

    @RunWith(Categories.class)
    @ExcludeCategory(NonPrepushTest.class)
    @SuiteClasses( { AGRepositoryConnectionTest.class })
    public static class Prepush {}

    @RunWith(Categories.class)
    @IncludeCategory(TestSuites.Broken.class)
    @SuiteClasses( { AGRepositoryConnectionTest.class })
    public static class Broken {}

    public String TEST_DIR_PREFIX = System.getProperty("com.franz.agraph.test.dataDir",
           System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"  + File.separator);

    protected Repository createRepository() throws Exception {
        AGServer server = new AGServer(AGAbstractTest.findServerUrl(), AGAbstractTest.username(), AGAbstractTest.password());
        AGCatalog catalog = server.getCatalog(AGAbstractTest.CATALOG_ID);
        if (catalog == null) {
            throw new Exception("Test catalog " + AGAbstractTest.CATALOG_ID + " not available");
        }
        AGRepository repo = catalog.createRepository("testRepo2");
        return repo;
    }

}
