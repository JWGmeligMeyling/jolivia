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
/*
 * Digital Audio Access Protocol (DAAP) Library
 * Copyright (C) 2004-2010 Roger Kapsi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dyndns.jkiddo.dmp;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import org.dyndns.jkiddo.dmap.chunks.audio.SongArtist;
import org.dyndns.jkiddo.dmp.chunks.ByteChunk;
import org.dyndns.jkiddo.dmp.chunks.Chunk;
import org.dyndns.jkiddo.dmp.chunks.ChunkFactory;
import org.dyndns.jkiddo.dmp.chunks.ContainerChunk;
import org.dyndns.jkiddo.dmp.chunks.DateChunk;
import org.dyndns.jkiddo.dmp.chunks.IntChunk;
import org.dyndns.jkiddo.dmp.chunks.LongChunk;
import org.dyndns.jkiddo.dmp.chunks.RawChunk;
import org.dyndns.jkiddo.dmp.chunks.ShortChunk;
import org.dyndns.jkiddo.dmp.chunks.StringChunk;
import org.dyndns.jkiddo.dmp.chunks.VersionChunk;
import org.dyndns.jkiddo.dmp.chunks.media.ListingItem;
import org.dyndns.jkiddo.dmp.util.DmapUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DmapInputStream extends BufferedInputStream
{

	private static final Logger logger = LoggerFactory.getLogger(DmapInputStream.class);

	private ChunkFactory factory = null;

	private final boolean specialCaseProtocolViolation;

	private int contentLength;

	private int getChunkContentLength()
	{
		return contentLength;
	}

	public DmapInputStream(InputStream in)
	{
		super(in);
		this.specialCaseProtocolViolation = false;
	}

	public DmapInputStream(InputStream in, boolean specialCaseProtocolViolation)
	{
		super(in);
		this.specialCaseProtocolViolation = specialCaseProtocolViolation;
	}

	@Override
	public int read() throws IOException
	{
		int b = super.read();
		if(b < 0)
		{
			throw new EOFException();
		}
		return b;
	}

	/*
	 * Re: skip(length-Chunk.XYZ_LENGTH); iTunes states in Content-Codes responses that Chunk X is of type Y and has hence the length Z. A Byte has for example the length 1. But in some cases iTunes uses a different length for Bytes! It's probably a bug in iTunes...
	 */

	private int readByte(int length) throws IOException
	{
		skip(length - Chunk.BYTE_LENGTH);
		return read();
	}

	private int readShort(int length) throws IOException
	{
		skip(length - Chunk.SHORT_LENGTH);
		return (read() << 8) | read();
	}
	

	private long readDate(int length) throws IOException
	{
		skip(length - Chunk.INT_LENGTH);
		long v = read();
		v = v << 24;
		long v2,v3,v4;
		v2 = (read() << 16);
		v3 = (read() << 8);
		v4 = read();
		v = v | v2 | v3 | v4;
		return  v;
	}

	private int readInt(int length) throws IOException
	{
		skip(length - Chunk.INT_LENGTH);
		return (read() << 24) | (read() << 16) | (read() << 8) | read();
	}

	private long readLong(int length) throws IOException
	{
		skip(length - Chunk.LONG_LENGTH);
		return (read() << 54l) | (read() << 48l) | (read() << 40l)
                | (read() << 32l) | (read() << 24l) | (read() << 16l)
                | (read() << 8l) | read();
	}

	private String readString(int length) throws IOException
	{
		if(length == 0)
		{
			return null;
		}

		byte[] b = new byte[length];
		read(b, 0, b.length);
		return new String(b, DmapUtil.UTF_8);
	}

	private int readContentCode() throws IOException
	{
		return readInt(Chunk.INT_LENGTH);
	}

	private int readLength() throws IOException
	{
		return readInt(Chunk.INT_LENGTH);
	}

	@SuppressWarnings("unchecked")
	public <T> T getChunk(Class<T> clazz) throws IOException, ProtocolViolationException
	{
		return (T) getChunk();
	}

	public Chunk getChunk() throws IOException, ProtocolViolationException
	{
		int contentCode = readContentCode();
		contentLength = readLength();

		if(factory == null)
		{
			factory = new ChunkFactory();
		}
		Chunk chunk = factory.newChunk(contentCode);

		if(specialCaseProtocolViolation)
		{
			if(chunk.getClass().equals(ListingItem.class))
			{
				chunk = new SongArtist();
			}
		}

		if(contentLength > 0)
		{
			if(chunk instanceof ByteChunk)
			{
				checkLength(chunk, Chunk.BYTE_LENGTH, contentLength);
				((ByteChunk) chunk).setValue(readByte(contentLength));
			}
			else if(chunk instanceof ShortChunk)
			{
				checkLength(chunk, Chunk.SHORT_LENGTH, contentLength);
				((ShortChunk) chunk).setValue(readShort(contentLength));
			}
			else if(chunk instanceof IntChunk)
			{
				checkLength(chunk, Chunk.INT_LENGTH, contentLength);
				((IntChunk) chunk).setValue(readInt(contentLength));
			}
			else if(chunk instanceof LongChunk)
			{
				checkLength(chunk, Chunk.LONG_LENGTH, contentLength);
				((LongChunk) chunk).setValue(readLong(contentLength));
			}
			else if(chunk instanceof StringChunk)
			{
				((StringChunk) chunk).setValue(readString(contentLength));
			}
			else if(chunk instanceof DateChunk)
			{
				checkLength(chunk, Chunk.DATE_LENGTH, contentLength);
				((DateChunk) chunk).setValue(readDate(contentLength));
			}
			else if(chunk instanceof VersionChunk)
			{
				checkLength(chunk, Chunk.VERSION_LENGTH, contentLength);
				((VersionChunk) chunk).setValue(readInt(contentLength));
			}
			else if(chunk instanceof RawChunk)
			{
				byte[] b = new byte[contentLength];
				read(b, 0, b.length);
				((RawChunk) chunk).setValue(b);
			}
			else if(chunk instanceof ContainerChunk)
			{
				byte[] b = new byte[contentLength];
				read(b, 0, b.length);
				DmapInputStream in = new DmapInputStream(new ByteArrayInputStream(b), this.specialCaseProtocolViolation);
				while(in.available() > 0)
				{

					try
					{
						((ContainerChunk) chunk).add(in.getChunk());
					}
					catch(ProtocolViolationException pve)
					{
						logger.warn(pve.getMessage(), pve);
						in.skip(in.getChunkContentLength());
					}
					//
					// Chunk newChunk = in.getChunk();
					// if(newChunk != null)
					// ((ContainerChunk) chunk).add(newChunk);
				}
				in.close();
			}
			else
			{
				throw new IOException("Unknown Chunk Type: " + chunk);
			}
		}

		return chunk;
	}

	/**
	 * Throws an IOE if expected differs from length
	 */
	private static void checkLength(Chunk chunk, int expected, int length)
	{
		if(expected != length)
		{
			// throw new IOException("Expected a chunk with length " + expected
			// + " but got " + length + " (" + chunk.getContentCodeString() +
			// ")");

			if(logger.isWarnEnabled())
			{
				logger.warn("Expected a chunk with length " + expected + " but got " + length + " (" + chunk.getContentCodeString() + ") " + chunk.getClass());
			}
		}
	}
}
