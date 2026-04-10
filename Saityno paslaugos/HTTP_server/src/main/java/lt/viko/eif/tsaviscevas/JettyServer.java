package lt.viko.eif.tsaviscevas;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletHandler;

public class JettyServer {
    private Server server;


    public void start() throws Exception{
        server=new Server();
        ServerConnector connector= new ServerConnector(server);
        connector.setPort(8080);
        server.setConnectors(new Connector[]{connector});

        ResourceHandler resourceHandler=new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase("src/main/webapp");

        ServletHandler servletHandler=new ServletHandler();

        HandlerList handlers=new HandlerList();
        handlers.addHandler(resourceHandler);
        handlers.addHandler(servletHandler);
        server.setHandler(handlers);

        servletHandler.addServletWithMapping(JettyStatusServlet.class, "/status");
        servletHandler.addServletWithMapping(JettyHTMLServlet.class,"/html");
        servletHandler.addServletWithMapping(JettyHTMLServletForms.class,"/forms");

        server.start();
        server.join();
    }
}