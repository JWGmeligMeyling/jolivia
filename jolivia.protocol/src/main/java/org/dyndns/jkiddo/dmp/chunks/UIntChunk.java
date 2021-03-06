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

package org.dyndns.jkiddo.dmp.chunks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An unsigned int
 */
public abstract class UIntChunk extends AbstractChunk implements IntChunk
{

	private static final Logger logger = LoggerFactory.getLogger(UIntChunk.class);

	public static final long MIN_VALUE = 0l;
	public static final long MAX_VALUE = 0xFFFFFFFFl;

	protected int value = 0;

	public UIntChunk(int type, String name, long value)
	{
		super(type, name);
		setValue(value);
	}

	public UIntChunk(String type, String name, long value)
	{
		super(type, name);
		setValue(value);
	}

	@Override
	public void setValue(int value)
	{
		this.value = value;
	}

	public void setValue(long value)
	{
		setValue((int) checkUIntRange(value));
	}

	@Override
	public int getValue()
	{
		return value;
	}

	public long getUnsignedValue()
	{
		return getValue() & MAX_VALUE;
	}

	/**
	 * Checks if #MIN_VALUE <= value <= #MAX_VALUE and if not an IllegalArgumentException is thrown.
	 */
	public static long checkUIntRange(long value) throws IllegalArgumentException
	{
		if(value < MIN_VALUE || value > MAX_VALUE)
		{
			logger.error("Value is outside of unsigned int range: " + value);
		}
		return value;
	}

	/**
	 * Returns {@see #U_INT_TYPE}
	 */
	@Override
	public int getType()
	{
		return Chunk.U_INT_TYPE;
	}

	@Override
	public String toString(int indent)
	{
		return indent(indent) + name + "(" + getContentCodeString() + "; uint)=" + getUnsignedValue();
	}
	
	@Override
	public void setObjectValue(Object object)
	{
		setValue((Integer) object);
	}
}
