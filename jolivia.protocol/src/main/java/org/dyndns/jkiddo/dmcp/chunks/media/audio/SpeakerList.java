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
package org.dyndns.jkiddo.dmcp.chunks.media.audio;

import org.dyndns.jkiddo.dmp.chunks.ContainerChunk;
import org.dyndns.jkiddo.dmp.chunks.media.Dictionary;

import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.casp)
public class SpeakerList extends ContainerChunk
{
	public SpeakerList()
	{
		super("casp", "dacp.speakers");
	}

	public Iterable<Dictionary> getDictionaries()
	{
		return getMultipleChunks(Dictionary.class);
	}
}
