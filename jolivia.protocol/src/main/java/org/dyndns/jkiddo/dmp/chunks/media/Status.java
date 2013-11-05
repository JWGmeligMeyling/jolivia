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

import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

/**
 * Seems to be the equivalent to HTTP/1.1 200 OK but it's never changing even if an error occurs.
 * 
 * @author Roger Kapsi
 */
public class Status extends UIntChunk
{

	/**
	 * The default status
	 */
	public static final int STATUS_200 = 200;

	public Status()
	{
		this(STATUS_200);
	}

	public Status(long status)
	{
		super("mstt", "dmap.status", status);
	}
}