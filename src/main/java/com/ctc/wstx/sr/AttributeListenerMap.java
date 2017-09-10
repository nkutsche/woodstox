package com.ctc.wstx.sr;

import java.util.HashMap;

import javax.xml.stream.Location;

public class AttributeListenerMap extends HashMap<String, AttributeListener> {
	
	public void put(AttributeListener aListener){
		this.put(aListener.getSystemId(), aListener);
	}
	
	public void remove(AttributeListener aListener){
		super.remove(aListener.getSystemId());
	}
	
	private AttributeListener get(Location loc){
		return this.get(loc.getSystemId());
	}
	
	public void attributeNameStart(Location loc){
		AttributeListener al = this.get(loc);
		if(al != null){
			al.attributeNameStart(loc);
		}
		
	}

	/**
	 * Notification about a parsing end of an attribute name
	 * 
	 * @param loc
	 *            Location of the name end
	 * @param prefix
	 *            Prefix of the attribute name, null if there is no prefix
	 * @param localName
	 *            Local name of the attribute name
	 */
	public void attributeNameEnd(Location loc, String prefix, String localName){
		AttributeListener al = this.get(loc);
		if(al != null){
			al.attributeNameEnd(loc, prefix, localName);
		}
	}

	/**
	 * Notification about a parsing start of an attribute value
	 * 
	 * @param loc
	 *            Location of the value start
	 */
	public void attributeValueStart(Location loc){
		AttributeListener al = this.get(loc);
		if(al != null){
			al.attributeValueStart(loc);
		}
	}

	/**
	 * Notification about a parsing end of an attribute value
	 * 
	 * @param loc
	 *            Location of the value end
	 * @param value
	 *            Attribute value (parsed)
	 */
	public void attributeValueEnd(Location loc, String value){
		AttributeListener al = this.get(loc);
		if(al != null){
			al.attributeValueEnd(loc, value);
		}
	}

	/**
	 * Notification about a parsing start of an entity inside of an attribute
	 * value
	 * 
	 * @param loc
	 *            Location of the entity start
	 */
	public void attributeEntityStart(Location loc){
		AttributeListener al = this.get(loc);
		if(al != null){
			al.attributeEntityStart(loc);
		}
	}

	/**
	 * Notification about a parsing end of an entity inside of an attribute
	 * value
	 * 
	 * @param loc
	 *            Location of the entity end
	 * @param valueLength
	 *            Length of the parsed entity value
	 */
	public void attributeEntityEnd(Location loc, Integer valueLength){
		AttributeListener al = this.get(loc);
		if(al != null){
			al.attributeEntityEnd(loc, valueLength);
		}
	}

	/**
	 * Notification about a parsing start of an attribute region
	 * 
	 * @param currentLocation
	 *            Location of the region start - the end of the element name
	 */
	public void attributeRegionStart(Location loc){
		AttributeListener al = this.get(loc);
		if(al != null){
			al.attributeRegionStart(loc);
		}
	}

	/**
	 * Notification about a parsing end of an attribute region
	 * 
	 * @param currentLocation
	 *            Location of the region end Closing bracket (&gt;) or closing
	 *            slash (/&gt;) of a start tag
	 */
	public void attributeRegionEnd(Location loc){
		AttributeListener al = this.get(loc);
		if(al != null){
			al.attributeRegionEnd(loc);
		}
	}
}
