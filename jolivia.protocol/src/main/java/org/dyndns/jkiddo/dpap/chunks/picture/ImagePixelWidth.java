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
package org.dyndns.jkiddo.dpap.chunks.picture;

import org.dyndns.jkiddo.dmp.chunks.UIntChunk;

/**
 * DPAP.ImagePixelWidth The width of the image in pixels.
 * 
 * @author Charles Ikeson
 */
import org.dyndns.jkiddo.dmp.IDmapProtocolDefinition.DmapProtocolDefinition;
import org.dyndns.jkiddo.dmp.DMAPAnnotation;

@DMAPAnnotation(type=DmapProtocolDefinition.pwth)
public class ImagePixelWidth extends UIntChunk
{
	/**
	 * Default constructor used when reading tags from a stream.
	 */
	public ImagePixelWidth()
	{
		this(0);
	}

	/**
	 * Constructor for the height parameter using the passed value.
	 * 
	 * @param width
	 *            The width of the image.
	 */
	public ImagePixelWidth(int width)
	{
		super("pwth", "dpap.imagepixelwidth", width);
	}
}
