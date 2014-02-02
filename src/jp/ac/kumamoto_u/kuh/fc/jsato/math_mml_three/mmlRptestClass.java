/**
 *
 * mmlRptestClass.java
 * Created on 2003/1/4 2:30:15
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
public class mmlRptestClass extends MMLObject {
	
	/* fields */
	private String __mmlRptestClassCode = null;
	private String __mmlRptestClassCodeId = null;

	private String text = null;
	
	public mmlRptestClass() {
	}
	
	
	/* print */
	public void printObject(PrintWriter pw, MMLVisitor visitor) throws IOException {
		if ( this.getQName() != null ) {
			visitor.goDown();// adjust tab
			pw.print( visitor.getTabPadding() + "<" + this.getQName() );
			/* print atts */
			if ( __mmlRptestClassCode != null ) pw.print(" " + "mmlRp:testClassCode" +  "=" + "'" + __mmlRptestClassCode + "'");
			if ( __mmlRptestClassCodeId != null ) pw.print(" " + "mmlRp:testClassCodeId" +  "=" + "'" + __mmlRptestClassCodeId + "'");

			if ( this.getLocalName().equals("levelone") ) {
				visitor.printNamespaces(pw);
			}
			pw.print( ">" );
			/* print content */
			// this element need not to print tab padding before the closing tag.
			visitor.setIgnoreTab( true );
			if (text != null) {
				if ( this.getText().equals("") == false ) pw.print( this.getText() );
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
		if (qName.equals("mmlRp:testClass") == true) {
			super.buildStart(namespaceURI,localName,qName,atts,builder);
			printlnStatus(parentElement.getQName() + " " + qName);
			
			/* create tree node */
			mmlRptestClass obj = new mmlRptestClass();
			builder.getMmlTree().addElement( obj );
			obj.setParentIndex( builder.mmlTreeIndex );
			builder.adjustIndex();
			((mmlRptestClass)builder.getElement()).setNamespace( getNamespace() );
			((mmlRptestClass)builder.getElement()).setLocalName( getLocalName() );
			((mmlRptestClass)builder.getElement()).setQName( getQName() );
			((mmlRptestClass)builder.getElement()).setAtts( getAtts() );/* :-) */
			/* atts */
			if (atts != null) {
				for (int i=0; i < atts.getLength(); ++i) {
					if ( ((String)atts.getQName(i)).equals("mmlRp:testClassCode") ) {
						set__mmlRptestClassCode( atts.getValue(i) );
						((mmlRptestClass)builder.getElement()).set__mmlRptestClassCode( atts.getValue(i) );
					}
					if ( ((String)atts.getQName(i)).equals("mmlRp:testClassCodeId") ) {
						set__mmlRptestClassCodeId( atts.getValue(i) );
						((mmlRptestClass)builder.getElement()).set__mmlRptestClassCodeId( atts.getValue(i) );
					}
				}
			}

			
			return true;
		}
		return false;
	}
	
	public boolean buildEnd(String namespaceURI, String localName, String qName, MMLBuilder builder) {
		if (qName.equals("mmlRp:testClass") == true) {
			
			/* connection */
			if (parentElement.getQName().equals("mmlRp:information")) {
				((mmlRpinformation)builder.getParent()).set_testClass((mmlRptestClass)builder.getElement());
			}

			
			printlnStatus(parentElement.getQName()+" /"+qName);


			builder.restoreIndex();
			super.buildEnd(namespaceURI,localName,qName,builder);
			return true;
		}
		return false;
	}
	
	/* characters */
	public boolean characters(char[] ch, int start, int length, MMLBuilder builder) {
		if (builder.getCurrentElement().getQName().equals("mmlRp:testClass")) {
			StringBuffer buffer=new StringBuffer(length);
			buffer.append(ch, start, length);
			setText(buffer.toString());
			((mmlRptestClass)builder.getElement()).setText( getText() );
			printlnStatus(parentElement.getQName()+" "+this.getQName()+":"+this.getText());
			return true;
		}
		return false;
	}
	
	
	/* setters and getters */
	public void set__mmlRptestClassCode(String __mmlRptestClassCode) {
		this.__mmlRptestClassCode = __mmlRptestClassCode;
	}
	public String get__mmlRptestClassCode() {
		return __mmlRptestClassCode;
	}
	public void set__mmlRptestClassCodeId(String __mmlRptestClassCodeId) {
		this.__mmlRptestClassCodeId = __mmlRptestClassCodeId;
	}
	public String get__mmlRptestClassCodeId() {
		return __mmlRptestClassCodeId;
	}

	public void setText(String text) {
		this.text = text;
	}
	public String getText() {
		return text;
	}
	
}