/**
 * Created on 10-Jan-2006
 * Created by Allan Crooks
 * Copyright (C) 2006 Aelitis, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * AELITIS, SAS au capital de 46,603.30 euros
 * 8 Allee Lenotre, La Grille Royale, 78600 Le Mesnil le Roi, France.
 *
 */
package com.aelitis.azureus.plugins.xmlhttp;


import java.io.File;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.biglybt.core.util.ByteFormatter;
import com.biglybt.pif.PluginInterface;
import com.biglybt.pifimpl.remote.RPObject;
import com.biglybt.pifimpl.remote.rpexceptions.RPInternalProcessException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class XMLSerialisationUtils {

    public static String serialise(Object o, Class c) {
    	if (o == null) {return "";}
    	
        Boolean simple_class = (Boolean)simple_class_mapping.get(c);

        // Unsupported type.
        if (simple_class == null) {
            return null;
        }

        // Simple type.
        else if (simple_class.booleanValue()) {
            return o.toString();
        }

        // Trickier type.
        else {
            if (c == byte_array_class) {
                return ByteFormatter.nicePrint((byte[])o, true);
            }else if ( o instanceof InetAddress ){
            	return(((InetAddress)o).getHostAddress());
            }else{
            	throw new RuntimeException("class is unsupported but not supported? " + c);
            }
        }
    }

    private static Map simple_class_mapping = new HashMap();
    private static Map class_string_mapping = new HashMap();
    private static final Class byte_array_class = new byte[0].getClass();
    private static Map deserialise_functions = new HashMap();

    static {

        /**
         * Supported classes that we recognise.
         */
        Class[][] supported_types = new Class[][] {

        			// first set can use toString
        	{
	            // Wrapper classes.
	            Boolean.class, Byte.class, Character.class, Float.class,
	            Integer.class, Long.class, Short.class,
	
	            // Primitive classes.
	            boolean.class, byte.class, char.class, float.class,
	            int.class, long.class, short.class,
	
	            	// Core classes.
	            
	            String.class,
	       		
	            	// 	Other classes - byte strings, URL's, Files.
        		File.class, URL.class,

        	},
        		// second set can't use toString and are special cased above
        	{
        		byte_array_class,
        		InetAddress.class, Inet4Address.class, Inet6Address.class,
        	}
         };

        for (int i=0; i<supported_types.length; i++) {

        	Class[] classes = supported_types[i];
        	
        	Boolean use_to_string = Boolean.valueOf(i==0);
        	
        	for (int j=0;j<classes.length;j++){
        		
        		Class cla = classes[j];
        		
	            /**
	             * Doesn't matter if the primitive classes replace the wrapper
	             * classes in this dictionary.
	             */
        		
	            class_string_mapping.put(RPUtils.getName(cla).toLowerCase(), cla);
	
	            simple_class_mapping.put(cla, use_to_string );
        	}
        }

 
        ParseFunction pf = null;

        pf = new PrimitiveParseFunction(Float.class);
        deserialise_functions.put(Float.class, pf);
        deserialise_functions.put(float.class, pf);

        pf = new PrimitiveParseFunction(Integer.class);
        deserialise_functions.put(Integer.class, pf);
        deserialise_functions.put(int.class, pf);

        pf = new PrimitiveParseFunction(Long.class);
        deserialise_functions.put(Long.class, pf);
        deserialise_functions.put(long.class, pf);

        pf = new PrimitiveParseFunction(Short.class);
        deserialise_functions.put(Short.class, pf);
        deserialise_functions.put(short.class, pf);

        pf = new ConstructorParseFunction(File.class);
        deserialise_functions.put(File.class, pf);

        pf = new ConstructorParseFunction(URL.class);
        deserialise_functions.put(URL.class, pf);

        pf = new BooleanParseFunction();
        deserialise_functions.put(Boolean.class, pf);
        deserialise_functions.put(boolean.class, pf);

        pf = new ByteArrayParseFunction();
        deserialise_functions.put(byte_array_class, pf);

        pf = new CharacterParseFunction();
        deserialise_functions.put(char.class, pf);
        deserialise_functions.put(Character.class, pf);

        pf = new StringParseFunction();
        deserialise_functions.put(String.class, pf);
    }


    public static Class getClass(String name) {
        return (Class)class_string_mapping.get(name.toLowerCase());
    }

    public static Object deserialise(String s, Class c) {
        ParseFunction pf = (ParseFunction)deserialise_functions.get(c);
        if (pf == null) {return null;}
        return pf.parse(s);
    }

    public static Map[] getAttributeData(Object o, int modifier_filter) {
        Map[] result = new Map[2];
        if (o instanceof GenericRPObject || o instanceof GenericRPPluginInterface) {
            RPObject ro = (RPObject)o;
            Class pclass;
            if (o instanceof GenericRPObject) {
            	pclass = ((GenericRPObject)ro)._getPluginClass();
            }
            else {
            	pclass = PluginInterface.class;
            }
            result[0] = new HashMap();
            result[1] = new HashMap();
            result[0].putAll(GenericRPAttributes.getAttributeTypes(pclass));
            result[1].putAll(GenericRPAttributes.getAttributes(ro._getDelegate(), pclass, result[0]));
            result[0].putAll(GenericRPAttributes.getRPAttributeTypes(ro.getClass()));
            result[1].putAll(GenericRPAttributes.getRPAttributes(ro, ro.getClass(), result[0]));
            //Debug.outNoStack("Types for " + o + ": " + result[0]);
            //Debug.outNoStack("Attributes for " + o + ": " + result[1]);
        }
        else {
            result[0] = new HashMap();
            result[1] = new HashMap();
            Class c = o.getClass();
            do {
                Field[] fields = c.getDeclaredFields();
                for (int i=0; i<fields.length; i++) {
                    Field field = fields[i];
                    int modifiers = field.getModifiers();
                    if ((modifiers | modifier_filter) != modifier_filter)
                        continue;
                    String field_name = field.getName();
                    result[0].put(field_name, field.getType());
                    try {
                        result[1].put(field_name, field.get(o));
                    }
                    catch (IllegalAccessException iae) {
                        throw new RPInternalProcessException(iae);
                    }
                }
                c = c.getSuperclass();
            }
            while (c != null);
        }
        return result;
    }

    /* Parse functions. */

    private interface ParseFunction {
        public Object parse(String s);
    }

    private static class PrimitiveParseFunction implements ParseFunction {

        private Method method_object;

        public PrimitiveParseFunction(Class c_object) {
            try {
                this.method_object = c_object.getMethod("valueOf", new Class[] {String.class});
            }
            catch (NoSuchMethodException nsme) {
                throw new RuntimeException(nsme);
            }
        }

        public Object parse(String s) {
            try {return method_object.invoke(null, new Object[]{s});}
            catch (java.lang.reflect.InvocationTargetException ite) {
                if (ite.getCause() instanceof RuntimeException) {
                    throw (RuntimeException)ite.getCause();
                }
                throw new RuntimeException((ite.getCause() == null) ? ite : ite.getCause());
            }
            catch (RuntimeException e) {throw e;}
            catch (Exception e) {throw new RuntimeException(e);}
        }
    }

    private static class ConstructorParseFunction implements ParseFunction {

        private Constructor constructor_object;

        public ConstructorParseFunction(Class c_object) {
            try {
                constructor_object = c_object.getConstructor(new Class[] {String.class});
            }
            catch (NoSuchMethodException nsme) {
                throw new RuntimeException(nsme);
            }
        }

        public Object parse(String s) {
            try {return constructor_object.newInstance(new Object[]{s});}
            catch (java.lang.reflect.InvocationTargetException ite) {
                if (ite.getCause() instanceof RuntimeException) {
                    throw (RuntimeException)ite.getCause();
                }
                throw new RuntimeException((ite.getCause() == null) ? ite : ite.getCause());
            }
            catch (RuntimeException e) {throw e;}
            catch (Exception e) {throw new RuntimeException(e);}
        }

    }

    private static class BooleanParseFunction implements ParseFunction {
        public Object parse(String s) {
            if (s.equals("true"))
                return Boolean.TRUE;
            else if (s.equals("false"))
                return Boolean.FALSE;
            else
                throw new IllegalArgumentException("not boolean argument: \"" + s + "\"");
        }
    }

    private static class ByteArrayParseFunction implements ParseFunction {
        public Object parse(String s) {
            return ByteFormatter.decodeString(s);
        }
    }

    private static class CharacterParseFunction implements ParseFunction {
        public Object parse(String s) {
            if (s.length() == 1)
                return new Character(s.charAt(0));
            throw new IllegalArgumentException("string is not one character long - cannot convert to char: \"" + s + "\"");
        }
    }

    private static class StringParseFunction implements ParseFunction {
        public Object parse(String s) {return s;}
    }

}
