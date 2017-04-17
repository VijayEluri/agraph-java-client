/******************************************************************************
** See the file LICENSE for the full license governing this code.
******************************************************************************/

package test.web;

import com.franz.agraph.pool.AGConnPool;
import com.franz.util.Closer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AGExampleServletContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {
        Closer c = new Closer();
        try {
            Context initCtx = c.closeLater(new InitialContext());
            Context envCtx = (Context) c.closeLater(initCtx.lookup("java:comp/env"));
            AGConnPool pool = (AGConnPool) envCtx.lookup("connection-pool/agraph");
            pool.close();
        } catch (Exception e) {
        	e.printStackTrace();
        } finally{
            c.close();
        }
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
	}
	
}
