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
package org.dyndns.jkiddo.protocol.dmap.chunks.dmap;

import org.dyndns.jkiddo.protocol.dmap.chunks.UIntChunk;

public class DatabaseShareType extends UIntChunk
{
	public static final int LOCAL = 0x01;
	public static final int SHARED = 0x02;
	public static final int RADIO = 0x64;

	public DatabaseShareType()
	{
		this(0);
	}

	public DatabaseShareType(long type)
	{
		super("mdbk", "dmap.databasesharetype", type);
	}
}
