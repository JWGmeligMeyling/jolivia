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

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.DispatcherType;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.dyndns.jkiddo.dmap.chunks.audio.SongAlbum;
import org.dyndns.jkiddo.dmap.chunks.audio.SongArtist;
import org.dyndns.jkiddo.dmp.chunks.media.AuthenticationMethod.PasswordMethod;
import org.dyndns.jkiddo.dmp.chunks.media.ItemName;
import org.dyndns.jkiddo.dmp.chunks.media.Listing;
import org.dyndns.jkiddo.dmp.chunks.media.ListingItem;
import org.dyndns.jkiddo.dmp.model.MediaItem;
import org.dyndns.jkiddo.guice.JoliviaServer;
import org.dyndns.jkiddo.jetty.extension.DmapConnector;
import org.dyndns.jkiddo.logic.desk.DeskImageStoreReader;
import org.dyndns.jkiddo.logic.desk.DeskMusicStoreReader;
import org.dyndns.jkiddo.logic.interfaces.IImageStoreReader;
import org.dyndns.jkiddo.logic.interfaces.IMusicStoreReader;
import org.dyndns.jkiddo.raop.ISpeakerListener;
import org.dyndns.jkiddo.raop.server.IPlayingInformation;
import org.dyndns.jkiddo.service.daap.client.IClientSessionListener;
import org.dyndns.jkiddo.service.daap.client.Session;
import org.eclipse.jetty.security.Authenticator;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.security.authentication.DigestAuthenticator;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.inject.servlet.GuiceFilter;

public class Jolivia
{
	static Logger logger = LoggerFactory.getLogger(Jolivia.class);

	public static void main(String[] args)
	{

		// return ServiceInfo.create("_mobileiphoto._udp.local.",
		// "00pYaGq1A..SPACE", port, "");
		try
		{
			IMusicStoreReader reader = null;
			/*if(args.length == 2)
			{
				reader = new GoogleStoreReader(args[0], args[1]);
				new GReporter(args[0]);
			}
			else
			{*/
				reader = new DeskMusicStoreReader();
				//new GReporter("local version");
			//}
			// new
			// Jolivia.JoliviaBuilder().port(4000).pairingCode(1337).musicStoreReader(reader).imageStoreReader(new
			// DeskImageStoreReader()).build();
			new Jolivia.JoliviaBuilder().port(8770).security(PasswordMethod.NO_PASSWORD).pairingCode(1337).musicStoreReader(reader).imageStoreReader(new DeskImageStoreReader("C:\\Users\\JensKristian\\Desktop\\test")).build();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static class JoliviaBuilder
	{
		private Integer port = 4000;
		private Integer airplayPort = 5000;
		private Integer pairingCode = 1337;
		private String name = "Jolivia";
		private ISpeakerListener speakerListener;
		private IClientSessionListener clientSessionListener = new DefaultClientSessionListener();
		private IMusicStoreReader musicStoreReader = new DefaultMusicStoreReader();
		private IImageStoreReader imageStoreReader = new DefaultImageStoreReader();
		private IPlayingInformation iplayingInformation = new DefaultIPlayingInformation();
		private PasswordMethod security = PasswordMethod.NO_PASSWORD;

		public JoliviaBuilder port(int port)
		{
			this.port = port;
			return this;
		}

		public JoliviaBuilder pairingCode(int pairingCode)
		{
			this.pairingCode = pairingCode;
			return this;
		}

		public JoliviaBuilder airplayPort(int airplayPort)
		{
			this.airplayPort = airplayPort;
			return this;
		}

		public JoliviaBuilder name(String name)
		{
			this.name = name;
			return this;
		}

		public JoliviaBuilder security(PasswordMethod security)
		{
			this.security = security;
			return this;
		}

		public JoliviaBuilder musicStoreReader(IMusicStoreReader musicStoreReader)
		{
			this.musicStoreReader = musicStoreReader;
			return this;
		}

		public JoliviaBuilder imageStoreReader(IImageStoreReader imageStoreReader)
		{
			this.imageStoreReader = imageStoreReader;
			return this;
		}

		public JoliviaBuilder playingInformation(IPlayingInformation iplayingInformation)
		{
			this.iplayingInformation = iplayingInformation;
			return this;
		}

		public JoliviaBuilder clientSessionListener(IClientSessionListener clientSessionListener)
		{
			this.clientSessionListener = clientSessionListener;
			return this;
		}

		public Jolivia build() throws Exception
		{
			SLF4JBridgeHandler.removeHandlersForRootLogger(); 
			SLF4JBridgeHandler.install();
			return new Jolivia(this);
		}

		class DefaultClientSessionListener implements IClientSessionListener
		{
			private Session session;

			@Override
			public void registerNewSession(Session session) throws Exception
			{
				this.session = session;
			}

			@Override
			public void tearDownSession(String server, int port)
			{
				try
				{
					session.logout();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		class DefaultIPlayingInformation implements IPlayingInformation
		{
			private JFrame frame;
			private JLabel label;

			public DefaultIPlayingInformation()
			{
				frame = new JFrame("Cover");
				label = new JLabel();
				frame.getContentPane().add(label, BorderLayout.CENTER);
				frame.pack();
				frame.setVisible(false);
			}

			@Override
			public void notify(BufferedImage image)
			{
				try
				{
					ImageIcon icon = new ImageIcon(image);
					label.setIcon(icon);
					frame.pack();
					frame.setSize(icon.getIconWidth(), icon.getIconHeight());
					frame.setVisible(true);
				}
				catch(Exception e)
				{
					logger.debug(e.getMessage(), e);
				}
			}

			@Override
			public void notify(ListingItem listingItem)
			{
				String title = listingItem.getSpecificChunk(ItemName.class).getValue();
				String artist = listingItem.getSpecificChunk(SongArtist.class).getValue();
				String album = listingItem.getSpecificChunk(SongAlbum.class).getValue();
				frame.setTitle("Playing: " + title + " - " + album + " - " + artist);
			}
		}

		class DefaultImageStoreReader implements IImageStoreReader
		{
			@Override
			public Set<IImageItem> readImages() throws Exception
			{
				return Sets.newHashSet();
			}

			@Override
			public URI getImage(IImageItem image) throws Exception
			{
				return null;
			}

			@Override
			public byte[] getImageThumb(IImageItem image) throws Exception
			{
				return null;
			}
		}

		class DefaultMusicStoreReader implements IMusicStoreReader
		{
			@Override
			public Set<MediaItem> readTunes() throws Exception
			{
				return Sets.newHashSet();
			}

			@Override
			public URI getTune(String tuneIdentifier) throws Exception
			{
				return null;
			}

			@Override
			public void readTunesMemoryOptimized(Listing listing, Map<Long, String> map) throws Exception
			{
				// TODO Auto-generated method stub
				
			}
		}
	}

	private final JoliviaServer joliviaServer;

	private Jolivia(JoliviaBuilder builder) throws Exception
	{
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();

		Preconditions.checkArgument(!(builder.pairingCode > 9999 || builder.pairingCode < 0), "Pairingcode must be expressed within 4 ciphers");
		logger.info("Starting " + builder.name + " on port " + builder.port);
		Server server = new Server(builder.port);
		// Server server = new
		// Server(InetSocketAddress.createUnresolved("0.0.0.0", port));
		Connector dmapConnector = new DmapConnector();
		dmapConnector.setPort(builder.port);
		// ServerConnector dmapConnector = new ServerConnector(server, new
		// DmapConnectionFactory());
		// dmapConnector.setPort(port);
		server.setConnectors(new Connector[] { dmapConnector });

		// Guice
		ServletContextHandler sch = new ServletContextHandler(server, "/");
		joliviaServer = new JoliviaServer(builder.port, builder.airplayPort, builder.pairingCode, builder.name, builder.clientSessionListener, builder.speakerListener, builder.imageStoreReader, builder.musicStoreReader, builder.iplayingInformation, builder.security);
		sch.addEventListener(joliviaServer);
		sch.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
		// if(builder.security == AuthenticationSchemes.BASIC_SCHEME)
		// sch.setSecurityHandler(getSecurityHandler("iTunes_11.1.2","admin",DmapUtil.DAAP_REALM, new BasicAuthenticator()));
		// if(builder.security == AuthenticationSchemes.DIGEST_SCHEME)
		// sch.setSecurityHandler(getSecurityHandler("iTunes_11.1.2","admin",DmapUtil.DAAP_REALM, new DigestAuthenticator()));
		sch.addServlet(DefaultServlet.class, "/");

		server.start();
		logger.info(builder.name + " started");
		// server.join();
	}

	private SecurityHandler getSecurityHandler(String username, String password, String realm, Authenticator authenticator)
	{

		HashLoginService loginService = new HashLoginService();
		loginService.putUser(username, Credential.getCredential(password), new String[] { "user" });
		loginService.setName(realm);

		Constraint globalConstraint = new Constraint();
		if(BasicAuthenticator.class.equals(authenticator.getClass()))
			globalConstraint.setName(Constraint.__BASIC_AUTH);
		if(DigestAuthenticator.class.equals(authenticator.getClass()))
			globalConstraint.setName(Constraint.__DIGEST_AUTH);
		globalConstraint.setRoles(new String[] { "user" });
		globalConstraint.setAuthenticate(true);

		ConstraintMapping globalConstraintMapping = new ConstraintMapping();
		globalConstraintMapping.setConstraint(globalConstraint);
		globalConstraintMapping.setPathSpec("/*");

		ConstraintSecurityHandler csh = new ConstraintSecurityHandler();
		csh.setAuthenticator(authenticator);
		csh.setRealmName(realm);
		csh.addConstraintMapping(globalConstraintMapping);
		csh.addConstraintMapping(createRelaxation("/server-info"));
		csh.addConstraintMapping(createRelaxation("/logout"));
		// Following is a hack! It should state /databases/*/items/* instead - however, that cannot be used.
		csh.addConstraintMapping(createRelaxation("/databases/*"));
		csh.setLoginService(loginService);

		return csh;
	}

	private static ConstraintMapping createRelaxation(String pathSpec)
	{
		Constraint relaxation = new Constraint();
		relaxation.setName(Constraint.ANY_ROLE);
		relaxation.setAuthenticate(false);
		ConstraintMapping constraintMapping = new ConstraintMapping();
		constraintMapping.setConstraint(relaxation);
		constraintMapping.setPathSpec(pathSpec);
		return constraintMapping;
	}

	public void reRegister()
	{
		this.joliviaServer.reRegister();
	}

	/*
	 * @Override public void update(Observable arg0, Object arg1) { if(trayIcon != null) { trayIcon.displayMessage(null, arg1.toString(), MessageType.INFO); } }
	 */
}