/*
 * RadiologyStampEditor.java
 * Copyright (C) 2002 Dolphin Project. All rights reserved.
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
package open.dolphin.order;

import javax.swing.*;
import javax.swing.border.*;

import open.dolphin.client.*;

import java.awt.*;

/**
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public final class RadiologyStampEditor extends StampModelEditor  {
    
    //private static final String orderClass      = "70";
    //private static final String orderName       = "�摜�f�f";
    //private static final String classCode       = "700";
    //private static final String classCodeId     = "Claim007";
    //private static final String classCodeName   = "�摜�f�f";    
    //private static final String subclassCodeId  = "Claim003";
            
    private ItemTablePanel testTable;
    private MasterTabPanel masterPanel;
    
    /** 
     * Creates new InjectionStampEditor 
     */
    public RadiologyStampEditor() {
        
        String[] spec = ClientContext.getStringArray("claim.order.radiology.spec");
        String orderName = spec[0];
        String serachClass = spec[1];
        String claimClassCode = spec[2];
        String claimClassCodeId = spec[3];
        String subclassCodeId = spec[4];
		String entityName = spec[5];
        
        setTitle(orderName);
        
        RadiologyMethod method = new RadiologyMethod();
        
        // Creates table
        testTable = new ItemTablePanel();
        testTable.setOrderName(orderName);
        testTable.setClassCode(claimClassCode);
        testTable.setClassCodeId(claimClassCodeId);
        testTable.setSubClassCodeId(subclassCodeId);
		testTable.setEntityName(entityName);
		
        Border b = BorderFactory.createEtchedBorder();
        testTable.setBorder(BorderFactory.createTitledBorder(b, orderName));
                
        masterPanel = new MasterTabPanel();
        masterPanel.setSearchClass(serachClass);
        masterPanel.setRadLocationEnabled(true);
        masterPanel.startTest(testTable);
        
        // Connect
        method.addPropertyChangeListener(RadiologyMethod.RADIOLOGY_MEYTHOD_PROP, testTable);
        testTable.setParent(this);
                
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
        top.add(method);
        top.add(testTable);
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(top);
        add(masterPanel);
        setPreferredSize(new Dimension(920, 610));
    }
    
    public Object getValue() {
        return testTable.getValue();
    }
    
    public void setValue(Object val) {
        testTable.setValue(val);
    }
    
    public void dispose() {
        masterPanel.stopTest(testTable);
    }
}