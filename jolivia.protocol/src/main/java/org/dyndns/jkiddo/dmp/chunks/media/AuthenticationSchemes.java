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

package org.dyndns.jkiddo.dmp.chunks.media;

import org.dyndns.jkiddo.dmp.chunks.UByteChunk;

/**
 * Authentication Schemes. ATTENTION: There's a bug in DAAP/iTunes! /content-codes says 'msas' is of type 0x0005 (signed int) however it has the 'msas' Chunk a length of 1 in /server-info respones and is thus a Byte!
 * 
 * @author Roger Kapsi
 */
import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.msas)
public class AuthenticationSchemes extends UByteChunk
{

	public static final int BASIC_SCHEME = 0x01;
	public static final int DIGEST_SCHEME = 0x02;

	/** Creates a new instance of AuthenticationSchemes */
	public AuthenticationSchemes()
	{
		this(BASIC_SCHEME | DIGEST_SCHEME);
	}

	public AuthenticationSchemes(int schemes)
	{
		super("msas", "dmap.authenticationschemes", schemes);
	}
}
