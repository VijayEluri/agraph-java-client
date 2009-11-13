
// ***** BEGIN LICENSE BLOCK *****
// Version: MPL 1.1
//
// The contents of this file are subject to the Mozilla Public License Version
// 1.1 (the "License"); you may not use this file except in compliance with
// the License. You may obtain a copy of the License at
// http://www.mozilla.org/MPL/
//
// Software distributed under the License is distributed on an "AS IS" basis,
// WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
// for the specific language governing rights and limitations under the
// License.
//
// The Original Code is the AllegroGraph Java Client interface.
//
// The Original Code was written by Franz Inc.
// Copyright (C) 2006 Franz Inc.  All Rights Reserved.
//
// ***** END LICENSE BLOCK *****

package com.franz.agbase.impl;

import com.franz.agbase.AllegroGraph;
import com.franz.agbase.ValueObject;


/**
 * This super class implements the common  methods of ValueObject instances.
 * 
 */
public abstract class ValueObjectImpl implements ValueObject
{
    protected AllegroGraph owner;

    protected ValueObjectImpl() { super(); }
    
    protected static boolean canReference ( long id ) {
    	return id>-1;
    }
    
   
    
    /* (non-Javadoc)
	 * @see com.franz.agbase.ValueObject#compareTo(java.lang.Object)
	 */
    public int compareTo ( Object to )
    {
    	return (toString()).compareTo(to.toString());
    }


}

