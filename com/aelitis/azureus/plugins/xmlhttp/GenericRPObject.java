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

import com.aelitis.azureus.plugins.xmlhttp.RPUtils;
import com.biglybt.pif.logging.LoggerChannel;
import com.biglybt.pifimpl.remote.RPObject;
import com.biglybt.pifimpl.remote.RPReply;


public class GenericRPObject extends RPObject {
	
    private transient Class plugin_class;

    public static GenericRPObject create(Object _delegate) {
        GenericRPObject res = (GenericRPObject)_lookupLocal(_delegate);

        if (res == null){
            res = new GenericRPObject(_delegate);
        }

        return res;
    }

    protected GenericRPObject(Object _delegate) {
        super(_delegate);
    }

    public static RPReply processGenerically(RPObject obj, com.biglybt.pifimpl.remote.RPRequest request) {
        LoggerChannel channel = request.getRPLoggerChannel();
        RemoteMethodInvoker inv = RemoteMethodInvoker.create(channel);
        return inv.process(obj._getDelegate(), request);
    }

    public RPReply _process(com.biglybt.pifimpl.remote.RPRequest request) {
        return processGenerically(this, request);
    }

    public String _getName() {
        return RPUtils.getName(this._getDelegate().getClass());
    }

    protected void _setDelegate(Object _delegate) {}
    public Object _setLocal() {return this._fixupLocal();}

    public String toString() {
        return "GenericRPObject [for " + _getName() + "]@" + Integer.toHexString(System.identityHashCode(this));
    }
    
    public Class _getPluginClass() {
        if (plugin_class == null) {
            plugin_class = RPUtils.getPluginAPIInterfaceForClass(this.__delegate.getClass());
            if (plugin_class == null) {
                /* Shouldn't happen */
                throw new RuntimeException("no plugin class for " + RPUtils.describeObject(this.__delegate));
            }
        }
        return plugin_class;
    }


}
