package stax2.stream;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.stream.*;

import org.codehaus.stax2.*;

import com.ctc.wstx.sr.AttributeListener;
import com.ctc.wstx.sr.BasicStreamReader;

import stax2.BaseStax2Test;

/**
 * Set of unit tests that checks that the {@link AttributeInfo} implementation
 * works as expected.
 *
 * @author Tatu Saloranta
 */
public class TestAttrListener
    extends BaseStax2Test
{
    final static String DEFAULT_VALUE = "default value";

    final static String TEST_DOC_BASIC =
        "<?xml version='1.0'?>"
        +"<root idAttr='idValue' textAttr='value' notation='not2' textAttr3='1'"
        +"><leaf />"
        +"</root>";

    final static String TEST_DOC_DTD =
        "<?xml version='1.0'?>"
        +"<!DOCTYPE root [\n"
        +"<!ELEMENT root (leaf)>\n"
        +"<!ATTLIST root defaultAttr CDATA '"+DEFAULT_VALUE+"'>\n"
        +"<!ATTLIST root textAttr CDATA #IMPLIED>\n"
        +"<!ATTLIST root idAttr ID #IMPLIED>\n"
        +"<!NOTATION not1 PUBLIC 'some-public-id'>\n"
        +"<!NOTATION not2 PUBLIC 'other-public-id'>\n"
        +"<!ATTLIST root notation NOTATION (not1 | not2) #IMPLIED>\n"
        +"<!ATTLIST root textAttr2 CDATA #IMPLIED>\n"
        +"<!ATTLIST root textAttr3 CDATA #IMPLIED>\n"
        +"<!ELEMENT leaf EMPTY>\n"
        +"<!ATTLIST leaf dummyAttr CDATA #IMPLIED>\n"
        +"]>"
        +"<root idAttr='idValue' textAttr='value' notation='not2' textAttr3='1'"
        +"><leaf />"
        +"</root>";
    
    final static String TEST_DOC_ENTITIES =
            "<?xml version='1.0'?>"
            +"<!DOCTYPE root [\n"
            +"<!ELEMENT root (leaf)>\n"
            +"<!ATTLIST root defaultAttr CDATA '"+DEFAULT_VALUE+"'>\n"
            +"<!ATTLIST root textAttr CDATA #IMPLIED>\n"
            +"<!ATTLIST root idAttr ID #IMPLIED>\n"
            +"<!NOTATION not1 PUBLIC 'some-public-id'>\n"
            +"<!NOTATION not2 PUBLIC 'other-public-id'>\n"
            +"<!ATTLIST root notation CDATA #IMPLIED>\n"
            +"<!ATTLIST root textAttr2 CDATA #IMPLIED>\n"
            +"<!ATTLIST root textAttr3 CDATA #IMPLIED>\n"
            +"<!ELEMENT leaf EMPTY>\n"
            +"<!ATTLIST leaf dummyAttr CDATA #IMPLIED>\n"
            + "<!ENTITY abc \"very-long-character\" >\n"
            + "<!ENTITY def \"&#x000A;\" >\n"
            +"]>"
            +"<root idAttr='&abc;' textAttr='prefix&abc;suffix' notation='bla&#x000A;' textAttr3='two&#x000A;entities&abc;are&def;nice'"
            +"><leaf />"
            +"</root>";
    
    /**
     * Baseline test case that does not use any information originating
     * from DTDs.
     */
    public void testAttrFindBasic()
        throws XMLStreamException
    {
        XMLStreamReader2 sr = getAttrReader(TEST_DOC_BASIC);

        // Let's verify basic facts...
        assertEquals(4, attrListener.attrStack.size());
        
        int basicOffset = sr.getLocation().getCharacterOffset();
        // Check the basic offset 
        assertEquals(21, basicOffset);
        
        // Where is the Attribute region?
        
        assertEquals(5, attrListener.regionStart.getCharacterOffset() - basicOffset);
        assertEquals(69, attrListener.regionEnd.getCharacterOffset() - basicOffset);
        
        // Look at the single Attributes
        
        // Remember, this is the unparsed element:
//       <root idAttr='idValue' textAttr='value' notation='not2' textAttr3='1'
        
//		First attribute: idAttr
        AttributeParseInfo idAttr = attrListener.attrStack.get(0);
        
//        check name & value
        assertEquals("idAttr", idAttr.name);
        assertEquals("idValue", idAttr.value);
//        check positions
        assertEquals(5, idAttr.locations.get("startName").getCharacterOffset() - basicOffset);
        assertEquals(12, idAttr.locations.get("endName").getCharacterOffset() - basicOffset);
        assertEquals(13, idAttr.locations.get("startValue").getCharacterOffset() - basicOffset);
        assertEquals(22, idAttr.locations.get("endValue").getCharacterOffset() - basicOffset);
        

//		Second attribute: textAttr
        AttributeParseInfo textAttr = attrListener.attrStack.get(1);
        
//        check name & value
        assertEquals("textAttr", textAttr.name);
        assertEquals("value", textAttr.value);
//        check positions
        assertEquals(22, textAttr.locations.get("startName").getCharacterOffset() - basicOffset);
        assertEquals(31, textAttr.locations.get("endName").getCharacterOffset() - basicOffset);
        assertEquals(32, textAttr.locations.get("startValue").getCharacterOffset() - basicOffset);
        assertEquals(39, textAttr.locations.get("endValue").getCharacterOffset() - basicOffset);
        

//		Third attribute: notation
        AttributeParseInfo notation = attrListener.attrStack.get(2);
        
//        check name & value
        assertEquals("notation", notation.name);
        assertEquals("not2", notation.value);
//        check positions
        assertEquals(39, notation.locations.get("startName").getCharacterOffset() - basicOffset);
        assertEquals(48, notation.locations.get("endName").getCharacterOffset() - basicOffset);
        assertEquals(49, notation.locations.get("startValue").getCharacterOffset() - basicOffset);
        assertEquals(55, notation.locations.get("endValue").getCharacterOffset() - basicOffset);
        

//		And last attribute: textAttr3
        AttributeParseInfo textAttr3 = attrListener.attrStack.get(3);
        
//        check name & value
        assertEquals("textAttr3", textAttr3.name);
        assertEquals("1", textAttr3.value);
//        check positions
        assertEquals(55, textAttr3.locations.get("startName").getCharacterOffset() - basicOffset);
        assertEquals(65, textAttr3.locations.get("endName").getCharacterOffset() - basicOffset);
        assertEquals(66, textAttr3.locations.get("startValue").getCharacterOffset() - basicOffset);
        assertEquals(69, textAttr3.locations.get("endValue").getCharacterOffset() - basicOffset);

        
        finishAttrReader(sr);
    }

    /**
     * More complex test case, in which information from DTD
     * (like attribute default values, notations) are needed.
     */
    public void testAttrFindDTD()
        throws XMLStreamException
    {
    	XMLStreamReader2 sr = getAttrReader(TEST_DOC_DTD);

        // Let's verify basic facts...
        assertEquals(4, attrListener.attrStack.size());
        
        int basicOffset = sr.getLocation().getCharacterOffset();
        // Check the basic offset 
        assertEquals(473, basicOffset);
        
        // Where is the Attribute region?
        
        assertEquals(5, attrListener.regionStart.getCharacterOffset() - basicOffset);
        assertEquals(69, attrListener.regionEnd.getCharacterOffset() - basicOffset);
        
        // Look at the single Attributes
        
        // Remember, this is the unparsed element:
//       <root idAttr='idValue' textAttr='value' notation='not2' textAttr3='1'
        
//		First attribute: idAttr
        AttributeParseInfo idAttr = attrListener.attrStack.get(0);
        
//        check name & value
        assertEquals("idAttr", idAttr.name);
        assertEquals("idValue", idAttr.value);
//        check positions
        assertEquals(5, idAttr.locations.get("startName").getCharacterOffset() - basicOffset);
        assertEquals(12, idAttr.locations.get("endName").getCharacterOffset() - basicOffset);
        assertEquals(13, idAttr.locations.get("startValue").getCharacterOffset() - basicOffset);
        assertEquals(22, idAttr.locations.get("endValue").getCharacterOffset() - basicOffset);
        

//		Second attribute: textAttr
        AttributeParseInfo textAttr = attrListener.attrStack.get(1);
        
//        check name & value
        assertEquals("textAttr", textAttr.name);
        assertEquals("value", textAttr.value);
//        check positions
        assertEquals(22, textAttr.locations.get("startName").getCharacterOffset() - basicOffset);
        assertEquals(31, textAttr.locations.get("endName").getCharacterOffset() - basicOffset);
        assertEquals(32, textAttr.locations.get("startValue").getCharacterOffset() - basicOffset);
        assertEquals(39, textAttr.locations.get("endValue").getCharacterOffset() - basicOffset);
        

//		Third attribute: notation
        AttributeParseInfo notation = attrListener.attrStack.get(2);
        
//        check name & value
        assertEquals("notation", notation.name);
        assertEquals("not2", notation.value);
//        check positions
        assertEquals(39, notation.locations.get("startName").getCharacterOffset() - basicOffset);
        assertEquals(48, notation.locations.get("endName").getCharacterOffset() - basicOffset);
        assertEquals(49, notation.locations.get("startValue").getCharacterOffset() - basicOffset);
        assertEquals(55, notation.locations.get("endValue").getCharacterOffset() - basicOffset);
        

//		And last attribute: textAttr3
        AttributeParseInfo textAttr3 = attrListener.attrStack.get(3);
        
//        check name & value
        assertEquals("textAttr3", textAttr3.name);
        assertEquals("1", textAttr3.value);
//        check positions
        assertEquals(55, textAttr3.locations.get("startName").getCharacterOffset() - basicOffset);
        assertEquals(65, textAttr3.locations.get("endName").getCharacterOffset() - basicOffset);
        assertEquals(66, textAttr3.locations.get("startValue").getCharacterOffset() - basicOffset);
        assertEquals(69, textAttr3.locations.get("endValue").getCharacterOffset() - basicOffset);

        
        finishAttrReader(sr);
    }
    /**
     * Special test case, for checking Entity positions 
     * resolved from DTD.
     */
    public void testAttrCheckEntities()
            throws XMLStreamException
        {
        	XMLStreamReader2 sr = getAttrReader(TEST_DOC_ENTITIES);

            // Let's verify basic facts...
            assertEquals(4, attrListener.attrStack.size());
            
            int basicOffset = sr.getLocation().getCharacterOffset();
            // Check the basic offset 
            assertEquals(519, basicOffset);
            
            // Where is the Attribute region?
            
            assertEquals(5, attrListener.regionStart.getCharacterOffset() - basicOffset);
            assertEquals(121, attrListener.regionEnd.getCharacterOffset() - basicOffset);
            
            // Look at the single Attributes
            
            // Remember, this is the unparsed element:
//       <root idAttr='&abc;' textAttr='prefix&abc;suffix' notation='bla&#x000A;' textAttr3='two&#x000A;entities&abc;are&def;nice'
            
//    		First attribute: idAttr
            AttributeParseInfo idAttr = attrListener.attrStack.get(0);
            
//            check name & value
            assertEquals("idAttr", idAttr.name);
            assertEquals("very-long-character", idAttr.value);
//            check positions
            assertEquals(5, idAttr.locations.get("startName").getCharacterOffset() - basicOffset);
            assertEquals(12, idAttr.locations.get("endName").getCharacterOffset() - basicOffset);
            assertEquals(13, idAttr.locations.get("startValue").getCharacterOffset() - basicOffset);
            assertEquals(20, idAttr.locations.get("endValue").getCharacterOffset() - basicOffset);
            

//    		Second attribute: textAttr
            AttributeParseInfo textAttr = attrListener.attrStack.get(1);
            
//            check name & value
            assertEquals("textAttr", textAttr.name);
            assertEquals("prefixvery-long-charactersuffix", textAttr.value);
//            check positions
            assertEquals(20, textAttr.locations.get("startName").getCharacterOffset() - basicOffset);
            assertEquals(29, textAttr.locations.get("endName").getCharacterOffset() - basicOffset);
            assertEquals(30, textAttr.locations.get("startValue").getCharacterOffset() - basicOffset);
            assertEquals(49, textAttr.locations.get("endValue").getCharacterOffset() - basicOffset);
            

//    		Third attribute: notation
            AttributeParseInfo notation = attrListener.attrStack.get(2);
            
//            check name & value
            assertEquals("notation", notation.name);
            assertEquals("bla\n", notation.value);
//            check positions
            assertEquals(49, notation.locations.get("startName").getCharacterOffset() - basicOffset);
            assertEquals(58, notation.locations.get("endName").getCharacterOffset() - basicOffset);
            assertEquals(59, notation.locations.get("startValue").getCharacterOffset() - basicOffset);
            assertEquals(72, notation.locations.get("endValue").getCharacterOffset() - basicOffset);
            

//    		And last attribute: textAttr3
            AttributeParseInfo textAttr3 = attrListener.attrStack.get(3);
            
//            check name & value
            assertEquals("textAttr3", textAttr3.name);
            assertEquals("two\nentitiesvery-long-characterare nice", textAttr3.value);
//            check positions
            assertEquals(72, textAttr3.locations.get("startName").getCharacterOffset() - basicOffset);
            assertEquals(82, textAttr3.locations.get("endName").getCharacterOffset() - basicOffset);
            assertEquals(83, textAttr3.locations.get("startValue").getCharacterOffset() - basicOffset);
            assertEquals(121, textAttr3.locations.get("endValue").getCharacterOffset() - basicOffset);

            
            finishAttrReader(sr);
        }

    /*
    ////////////////////////////////////////
    // Private methods
    ////////////////////////////////////////
     */

    private XMLStreamReader2 getAttrReader(String str)
        throws XMLStreamException
    {
        XMLStreamReader2 sr = getReader(str);
        assertTokenType(START_DOCUMENT, sr.getEventType());
        int type = sr.next();
        if (type == DTD) {
            type = sr.next();
        }
        assertTokenType(START_ELEMENT, type);
        return sr;
    }

    private void finishAttrReader(XMLStreamReader sr)
        throws XMLStreamException
    {
        while (sr.getEventType() != END_DOCUMENT) {
            sr.next();
        }
    }

    private XMLStreamReader2 getReader(String contents)
        throws XMLStreamException
    {
        XMLInputFactory f = getInputFactory();
        setCoalescing(f, false); // shouldn't really matter
        setNamespaceAware(f, true);
        setSupportDTD(f, true);
        /* Probably need to enable validation, to get all the attribute
         * type info processed and accessible?
         */
        setValidating(f, true);
        BasicStreamReader.ATTRIBUTE_LISTENER = attrListener;
        return constructStreamReader(f, contents);
    }
    
    /*
    ////////////////////////////////////////
    // Private helper classes
    ////////////////////////////////////////
     */

    private class AttributeParseInfo {
    	public HashMap<String, Location> locations = new HashMap<String, Location>(); 
    	public ArrayList<int[]> entities = new ArrayList<int[]>();
		public String value = null;
		public String name = null;
    }
    
    private class AttributeListenerTest implements AttributeListener {
    	public ArrayList<AttributeParseInfo> attrStack = new ArrayList<TestAttrListener.AttributeParseInfo>();
    	public Location regionStart = null;
    	public Location regionEnd = null;
    	
    	private AttributeParseInfo getCash(){
    		return attrStack.get(attrStack.size() - 1);
    	}
    	
		@Override
		public void attributeValueStart(Location loc) {
			
			getCash().locations.put("startValue", loc);
		}
		
		@Override
		public void attributeValueEnd(Location loc, String value) {
			getCash().locations.put("endValue", loc);
			getCash().value = value;
		}
		
		@Override
		public void attributeRegionStart(Location currentLocation) {
			this.regionStart = currentLocation;
		}
		
		@Override
		public void attributeRegionEnd(Location currentLocation) {
			this.regionEnd = currentLocation;
		}
		
		@Override
		public void attributeNameStart(Location loc) {
			attrStack.add(new AttributeParseInfo());
			getCash().locations.put("startName", loc);
		}
		
		@Override
		public void attributeNameEnd(Location loc, String prefix, String localName) {
			getCash().locations.put("endName", loc);
			getCash().name = prefix == null ? localName : prefix + ":" + localName;
		}
		
		private Location entityStart = null;
		
		@Override
		public void attributeEntityStart(Location loc) {
			entityStart = loc;
		}
		
		@Override
		public void attributeEntityEnd(Location loc, Integer valueLength) {
			if(entityStart == null){
				fail("Entity end without an entity start!");
			}
			getCash().entities.add(new int[]{entityStart.getCharacterOffset(), loc.getCharacterOffset(), valueLength});
		}
    }
    
    final AttributeListenerTest attrListener = new AttributeListenerTest();

    
    
}
