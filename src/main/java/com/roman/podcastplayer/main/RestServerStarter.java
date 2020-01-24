package com.roman.podcastplayer.main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class RestServerStarter {
    public static void main(String[] args) {
        Server server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);

        context.setContextPath("/");
        server.setHandler(context);

        ServletHolder holder = context.addServlet(ServletContainer.class, "/rest/*");
        holder.setInitParameter("javax.ws.rs.Application", "com.roman.podcastplayer.rest.PodcastApplication");
//        holder.setInitParameter("jersey.config.server.provider.packages", "com.roman.podcastplayer.rest");
        holder.setInitOrder(1);

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.destroy();
        }
    }
}
