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
package org.dyndns.jkiddo.dmcp.chunks.media.extension;

import org.dyndns.jkiddo.dmp.chunks.UByteChunk;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.ceGS)
public class GeniusSelectable extends UByteChunk
{
	public GeniusSelectable()
	{
		this(0);
	}

	public GeniusSelectable(int value)
	{
		super("ceGS", "com.apple.itunes.genius-selectable", value);
	}

}
