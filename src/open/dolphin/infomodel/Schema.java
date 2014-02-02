/*
 * Schema.java
 * Copyright (C) 2002 Dolphin Project. All rights reserved.
 * Copyright (C) 2003 Digital Globe, Inc. All rights reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *	
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *	
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package open.dolphin.infomodel;

import javax.swing.ImageIcon;

/**
 * @author Kazushi Minagawa, Digital Globe, Inc.
 *
 */
public class Schema extends InfoModel {
	
	ImageIcon icon;
    
	String fileName;
    
	IInfoModel model;
    
	byte[] jpegByte;
    
	/** Creates new Schema */
	public Schema() {
	}
    
	public ImageIcon getIcon() {
		return icon;
	}
    
	public void setIcon(ImageIcon val) {
		icon = val;
	}
    
	public String getFileName() {
		return fileName;
	}
    
	public void setFileName(String val) {
		fileName = val;
	}
    
	public byte[] getJPEGByte() {
		return jpegByte;
	}
    
	public void setJPEGByte(byte[] val) {
		jpegByte = val;
	}
    
	public IInfoModel getModel() {
		return model;
	}
    
	public void setModel(IInfoModel val) {
		model = val;
	}
}
