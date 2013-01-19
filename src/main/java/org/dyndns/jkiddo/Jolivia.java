/*******************************************************************************
 * Copyright (c) 2013 Jens Kristian Villadsen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Jens Kristian Villadsen - Lead developer, owner and creator
 ******************************************************************************/
package org.dyndns.jkiddo;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.dyndns.jkiddo.guice.JoliviaListener;
import org.dyndns.jkiddo.jetty.extension.DmapConnectionFactory;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.servlet.GuiceFilter;

public class Jolivia
{
	public static final String name = "joliv1a";
	static Logger logger = LoggerFactory.getLogger(Jolivia.class);
	// http://randomizedsort.blogspot.dk/2011/05/using-guice-ified-jersey-in-embedded.html
	// http://blog.palominolabs.com/2011/08/15/a-simple-java-web-stack-with-guice-jetty-jersey-and-jackson/
	// https://github.com/teamlazerbeez/simple-web-stack
	public static void main(String[] args) throws Exception
	{
		new Jolivia();
	}
	static int port = 4002;

	public Jolivia() throws Exception
	{
		logger.info("Starting " + name + " on port " + port);
		Server server = new Server(port);
		ServerConnector sc = new ServerConnector(server, new DmapConnectionFactory());
		sc.setPort(port);
		server.setConnectors(new Connector[] { sc });
		ServletContextHandler sch = new ServletContextHandler(server, "/");
		sch.addEventListener(new JoliviaListener(port));
		sch.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
		sch.addServlet(DefaultServlet.class, "/");
		server.setSendServerVersion(false);
		server.start();
		logger.info(name + " started");
		server.join();
	}
}
