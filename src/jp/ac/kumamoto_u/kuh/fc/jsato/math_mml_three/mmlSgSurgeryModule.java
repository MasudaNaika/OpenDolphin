/**
 *
 * mmlSgSurgeryModule.java
 * Created on 2003/1/4 2:30:8
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
public class mmlSgSurgeryModule extends MMLObject {
	
	/* fields */
	private Vector _surgeryItem = new Vector();
	
	public mmlSgSurgeryModule() {
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
			if (this._surgeryItem != null) {
				visitor.setIgnoreTab( false );
				pw.print( "\n" );
				// print each element in the vector assumming that it doesn't contain String object...
				for (int i = 0; i < this._surgeryItem.size(); ++i ) {
					((mmlSgsurgeryItem)this._surgeryItem.elementAt(i)).printObject(pw, visitor);
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
		if (qName.equals("mmlSg:SurgeryModule") == true) {
			super.buildStart(namespaceURI,localName,qName,atts,builder);
			printlnStatus(parentElement.getQName() + " " + qName);
			
			/* create tree node */
			mmlSgSurgeryModule obj = new mmlSgSurgeryModule();
			builder.getMmlTree().addElement( obj );
			obj.setParentIndex( builder.mmlTreeIndex );
			builder.adjustIndex();
			((mmlSgSurgeryModule)builder.getElement()).setNamespace( getNamespace() );
			((mmlSgSurgeryModule)builder.getElement()).setLocalName( getLocalName() );
			((mmlSgSurgeryModule)builder.getElement()).setQName( getQName() );
			((mmlSgSurgeryModule)builder.getElement()).setAtts( getAtts() );/* :-) */
			/* atts */
			
			return true;
		}
		return false;
	}
	
	public boolean buildEnd(String namespaceURI, String localName, String qName, MMLBuilder builder) {
		if (qName.equals("mmlSg:SurgeryModule") == true) {
			
			/* connection */
			if (parentElement.getQName().equals("local_markup")) {
				Vector v = ((local_markup)builder.getParent()).getVt();
				if (v == null) printlnStatus("parent's vector is null!!!");
				v.addElement( (mmlSgSurgeryModule)builder.getElement() );
			}

			if (parentElement.getQName().equals("mmlSm:SummaryModule")) {
				Vector v = ((mmlSmSummaryModule)builder.getParent()).get_SurgeryModule();
				v.addElement(builder.getElement());
			}

			if (parentElement.getQName().equals("mml:content")) {
				((mmlcontent)builder.getParent()).set_SurgeryModule((mmlSgSurgeryModule)builder.getElement());
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
	public void set_surgeryItem(Vector _surgeryItem) {
		if (this._surgeryItem != null) this._surgeryItem.removeAllElements();
		// copy entire elements in the vector
		this._surgeryItem = new Vector();
		for (int i = 0; i < _surgeryItem.size(); ++i) {
			this._surgeryItem.addElement( _surgeryItem.elementAt(i) );
		}
	}
	public Vector get_surgeryItem() {
		return _surgeryItem;
	}
	
}