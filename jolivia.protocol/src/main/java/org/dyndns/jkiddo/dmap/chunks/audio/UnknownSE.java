package org.dyndns.jkiddo.dmap.chunks.audio;

import org.dyndns.jkiddo.dmp.chunks.ULongChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.asse)
public class UnknownSE extends ULongChunk
{
	public UnknownSE()
	{
		this(0);
	}

	public UnknownSE(int i)
	{
		super("asse", "com.apple.itunes.unknown-se", i);
	}
}
