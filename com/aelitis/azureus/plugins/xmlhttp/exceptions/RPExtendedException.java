/**
 * 
 */
package com.aelitis.azureus.plugins.xmlhttp.exceptions;

import com.aelitis.azureus.plugins.xmlhttp.RPUtils;
import com.biglybt.pifimpl.remote.RPException;

/**
 * @author Allan Crooks
 *
 */
public class RPExtendedException extends RPException {

    public RPExtendedException(String str) {
        super(str);
    }

    public RPExtendedException(String str, Throwable e) {
        super(str, e);
    }
    
    public RPExtendedException(Throwable e) {
    	super(e);
    }
	
	public String getSerialisationMessage() {
		return RPUtils.exceptionToString(this);
	}
	
}
