/****************************************************************************/
/*  File:       DepVersions.java                                            */
/*  Author:     F. Georges - H2O Consulting                                 */
/*  Date:       2011-01-27                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2011 Florent Georges (see end of file.)               */
/* ------------------------------------------------------------------------ */


package org.expath.pkg.repo.deps;

/**
 * A dependency version using {@code @version}.
 * 
 * @author Florent Georges
 */
class DepVersions
        extends DependencyVersion
{
    public DepVersions(String versions)
    {
        myVersions = versions.split("\\s+");
    }

    @Override
    public boolean isCompatible(String version)
    {
        for (String v : myVersions) {
            if (v.equals(version)) {
                return true;
            }
        }
        return false;
    }

    private String[] myVersions;
}


/* ------------------------------------------------------------------------ */
/*  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS COMMENT.               */
/*                                                                          */
/*  The contents of this file are subject to the Mozilla Public License     */
/*  Version 1.0 (the "License"); you may not use this file except in        */
/*  compliance with the License. You may obtain a copy of the License at    */
/*  http://www.mozilla.org/MPL/.                                            */
/*                                                                          */
/*  Software distributed under the License is distributed on an "AS IS"     */
/*  basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.  See    */
/*  the License for the specific language governing rights and limitations  */
/*  under the License.                                                      */
/*                                                                          */
/*  The Original Code is: all this file.                                    */
/*                                                                          */
/*  The Initial Developer of the Original Code is Florent Georges.          */
/*                                                                          */
/*  Contributor(s): none.                                                   */
/* ------------------------------------------------------------------------ */
