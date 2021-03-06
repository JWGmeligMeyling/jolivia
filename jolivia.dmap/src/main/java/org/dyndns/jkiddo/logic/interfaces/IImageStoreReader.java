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
package org.dyndns.jkiddo.logic.interfaces;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import java.util.Set;

public interface IImageStoreReader extends Serializable
{
	public Set<IImageItem> readImages() throws Exception;

	public URI getImage(IImageItem image) throws Exception;
	
	byte[] getImageThumb(IImageItem image) throws Exception;
	
	interface IImageItem
	{
		String getImageFilename();

		long getSize();

		String getFormat();

		int getRating();

		Date getCreationDate();
		
		int getImageWidth();
		
		int getImageHeight();
	}
}
