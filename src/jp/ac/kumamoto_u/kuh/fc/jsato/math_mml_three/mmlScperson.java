/**
 *
 * mmlScperson.java
 * Created on 2003/1/4 2:29:56
 */
package jp.ac.kumamoto_u.kuh.fc.jsato.math_mml_three;

import java.awt.*;
import java.util.*;
import org.xml.sax.*;

import java.io.*;
/**
 *
 * @author	Junzo SATO
 * @version
 */
public class mmlScperson extends MMLObject {
	
	/* fields */
	private Vector _personName = new Vector();
	
	public mmlScperson() {
	}
	
	
	/* print */
	public void printObject(PrintWriter pw, MMLVisitor visitor) throws IOException {
		if ( this.getQName() != null ) {
			visitor.goDown();// adjust tab
			pw.print( visitor.getTabPadding() + "<" + this.getQName() );
			/* print atts */
			if ( this.getLocalName().equals("levelone") ) {
				visitor.printNamespaces(pw);
			}
			pw.print( ">" );
			/* print content */
			if (this._personName != null) {
				visitor.setIgnoreTab( false );
				pw.print( "\n" );
				// print each element in the vector assumming that it doesn't contain String object...
				for (int i = 0; i < this._personName.size(); ++i ) {
					((mmlScpersonName)this._personName.elementAt(i)).printObject(pw, visitor);
				}
			}

			// only compound element requires to add tab padding before closing tag
			if ( visitor.getIgnoreTab() == false ) {
				pw.print( visitor.getTabPadding() );
			}
			pw.print( "</" + this.getQName() + ">\n" );
			pw.flush();
			visitor.setIgnoreTab( false );
			visitor.goUp();// adjust tab
		}
	}
	
	public boolean buildStart(String namespaceURI, String localName, String qName, Attributes atts, MMLBuilder builder) {
		if (qName.equals("mmlSc:person") == true) {
			super.buildStart(namespaceURI,localName,qName,atts,builder);
			printlnStatus(parentElement.getQName() + " " + qName);
			
			/* create tree node */
			mmlScperson obj = new mmlScperson();
			builder.getMmlTree().addElement( obj );
			obj.setParentIndex( builder.mmlTreeIndex );
			builder.adjustIndex();
			((mmlScperson)builder.getElement()).setNamespace( getNamespace() );
			((mmlScperson)builder.getElement()).setLocalName( getLocalName() );
			((mmlScperson)builder.getElement()).setQName( getQName() );
			((mmlScperson)builder.getElement()).setAtts( getAtts() );/* :-) */
			/* atts */
			
			return true;
		}
		return false;
	}
	
	public boolean buildEnd(String namespaceURI, String localName, String qName, MMLBuilder builder) {
		if (qName.equals("mmlSc:person") == true) {
			
			/* connection */
			if (parentElement.getQName().equals("mml:accessRight")) {
				((mmlaccessRight)builder.getParent()).set_person((mmlScperson)builder.getElement());
			}

			
			printlnStatus(parentElement.getQName()+" /"+qName);


			builder.restoreIndex();
			super.buildEnd(namespaceURI,localName,qName,builder);
			return true;
		}
		return false;
	}
	
	/* characters */
	
	
	/* setters and getters */
	public void set_personName(Vector _personName) {
		if (this._personName != null) this._personName.removeAllElements();
		// copy entire elements in the vector
		this._personName = new Vector();
		for (int i = 0; i < _personName.size(); ++i) {
			this._personName.addElement( _personName.elementAt(i) );
		}
	}
	public Vector get_personName() {
		return _personName;
	}
	
}