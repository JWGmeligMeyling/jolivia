package org.ardverk.daap.chunks.impl.dmap;

import org.ardverk.daap.chunks.UIntChunk;

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