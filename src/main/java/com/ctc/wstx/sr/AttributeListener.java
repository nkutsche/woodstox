package com.ctc.wstx.sr;

import javax.xml.stream.Location;

import org.codehaus.stax2.XMLStreamProperties;

/**
 * Interface to implement an Attribute Listener Used in BasicStreamReader to
 * notifications about the locations of - attribute name start / end - attribute
 * value start / end - attribute region start / end (starts with the end of the
 * element name, ends with the closing bracket (&gt;) or the closing slash (/&gt;) -
 * entity start / end in an attribute value This notifications will be thrown,
 * during the parsing process
 */
public interface AttributeListener {
	
	public abstract String getSystemId();
	
	/**
	 * Notification about a parsing start of an attribute name
	 * 
	 * @param loc
	 *            Location of the name start
	 */
	public abstract void attributeNameStart(Location loc);

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
	public abstract void attributeNameEnd(Location loc, String prefix, String localName);

	/**
	 * Notification about a parsing start of an attribute value
	 * 
	 * @param loc
	 *            Location of the value start
	 */
	public abstract void attributeValueStart(Location loc);

	/**
	 * Notification about a parsing end of an attribute value
	 * 
	 * @param loc
	 *            Location of the value end
	 * @param value
	 *            Attribute value (parsed)
	 */
	public abstract void attributeValueEnd(Location loc, String value);

	/**
	 * Notification about a parsing start of an entity inside of an attribute
	 * value
	 * 
	 * @param loc
	 *            Location of the entity start
	 */
	public abstract void attributeEntityStart(Location loc);

	/**
	 * Notification about a parsing end of an entity inside of an attribute
	 * value
	 * 
	 * @param loc
	 *            Location of the entity end
	 * @param valueLength
	 *            Length of the parsed entity value
	 */
	public abstract void attributeEntityEnd(Location loc, Integer valueLength);

	/**
	 * Notification about a parsing start of an attribute region
	 * 
	 * @param currentLocation
	 *            Location of the region start - the end of the element name
	 */
	public abstract void attributeRegionStart(Location currentLocation);

	/**
	 * Notification about a parsing end of an attribute region
	 * 
	 * @param currentLocation
	 *            Location of the region end Closing bracket (&gt;) or closing
	 *            slash (/&gt;) of a start tag
	 */
	public abstract void attributeRegionEnd(Location currentLocation);
}
