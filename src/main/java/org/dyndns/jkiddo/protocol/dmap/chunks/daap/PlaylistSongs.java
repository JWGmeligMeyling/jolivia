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

package org.dyndns.jkiddo.protocol.dmap.chunks.daap;

import org.dyndns.jkiddo.protocol.dmap.chunks.ContainerChunk;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.Listing;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.ReturnedCount;
import org.dyndns.jkiddo.protocol.dmap.chunks.dmap.SpecifiedTotalCount;

/**
 * Container for the <tt>/databases/id/containers/id/items</tt> request
 * 
 * @author Roger Kapsi
 */
public class PlaylistSongs extends ContainerChunk
{

	public PlaylistSongs()
	{
		super("apso", "daap.playlistsongs");
	}

	public Listing getListing()
	{
		return getSingleChunk(Listing.class);
	}

	public SpecifiedTotalCount getSpecifiedTotalCount()
	{
		return getSingleChunk(SpecifiedTotalCount.class);
	}

	public ReturnedCount getReturnedCount()
	{
		return getSingleChunk(ReturnedCount.class);
	}
}
