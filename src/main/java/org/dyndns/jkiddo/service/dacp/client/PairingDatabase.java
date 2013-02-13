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
package org.dyndns.jkiddo.service.dacp.client;

import java.util.Random;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class PairingDatabase implements IPairingDatabase
{
	public static final String DB_URL = "DB_URL";

	@Inject
	public PairingDatabase(@Named(DB_URL) String url) throws ClassNotFoundException
	{
		Class.forName("org.sqlite.JDBC");

		dbi = new DBI(url);
		dbHandler = dbi.open().attach(PairingDatabaseCommands.class);
		dbHandler.createTable();

		Random random = new Random();

		// generate pair code
		byte[] pair = new byte[8];
		random.nextBytes(pair);
		dbHandler.updateEntry(KEY_PAIRING_CODE, toHex(pair));

		// generate remote guid
		// this is the thing that uniquely identifies this remote
		byte[] serviceguid = new byte[20];
		random.nextBytes(serviceguid);
		dbHandler.updateEntry(KEY_SERVICE_GUID, toHex(serviceguid));
	}

	private final DBI dbi;
	private final PairingDatabaseCommands dbHandler;

	private final static String TABLE_PAIR = "pairing";
	private final static String FIELD_PAIR_SERVICENAME = "servicename";
	private final static String FIELD_PAIR_GUID = "guid";
	private final static String KEY_PAIRING_CODE = "pair";
	private final static String KEY_SERVICE_GUID = "serviceguid";
	private final static String KEY_LAST_SESSION = "lastsession";

	private interface PairingDatabaseCommands
	{
		@SqlUpdate("CREATE TABLE IF NOT EXISTS " + TABLE_PAIR + " (" + FIELD_PAIR_SERVICENAME + " text primary key, " + FIELD_PAIR_GUID + " text)")
		public void createTable();

		@SqlUpdate("insert or replace into " + TABLE_PAIR + " (" + FIELD_PAIR_SERVICENAME + "," + FIELD_PAIR_GUID + ") VALUES (:" + FIELD_PAIR_SERVICENAME + ", :" + FIELD_PAIR_GUID + ")")
		public void updateEntry(@Bind(FIELD_PAIR_SERVICENAME) String serviceName, @Bind(FIELD_PAIR_GUID) String guid);

		@SqlQuery("select " + FIELD_PAIR_GUID + " from " + TABLE_PAIR + " where " + FIELD_PAIR_SERVICENAME + " = :" + FIELD_PAIR_SERVICENAME + "")
		public String getCode(@Bind(FIELD_PAIR_SERVICENAME) String servicename);
	}

	@Override
	public String findCode(String serviceName)
	{
		return dbHandler.getCode(serviceName);
	}

	@Override
	public void updateCode(String serviceName, String guid)
	{
		if(serviceName != null && guid != null)
		{
			dbHandler.updateEntry(serviceName, guid);
		}
	}
	@Override
	public String getPairCode()
	{
		return findCode(KEY_PAIRING_CODE);
	}

	@Override
	public String getServiceGuid()
	{
		return findCode(KEY_SERVICE_GUID);
	}

	@Override
	public String getLastSession()
	{
		return findCode(KEY_LAST_SESSION);
	}

	@Override
	public void setLastSession(String serviceName)
	{
		updateCode(KEY_LAST_SESSION, serviceName);
	}

	public static String toHex(byte[] code)
	{
		StringBuilder sb = new StringBuilder();
		for(byte b : code)
		{
			sb.append(String.format("%02x", b & 0xff));
		}
		return sb.toString().toUpperCase();
	}
}