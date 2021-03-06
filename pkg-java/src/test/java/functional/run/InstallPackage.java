/****************************************************************************/
/*  File:       InstallPackage.java                                         */
/*  Author:     F. Georges                                                  */
/*  Company:    H2O Consulting                                              */
/*  Date:       2009-11-02                                                  */
/*  Tags:                                                                   */
/*      Copyright (c) 2009-2013 Florent Georges (see end of file.)          */
/* ------------------------------------------------------------------------ */


package functional.run;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.expath.pkg.repo.*;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;

import javax.xml.transform.Source;

import static org.junit.Assert.*;

/**
 * Functional tests for package install.
 *
 * @author Florent Georges
 */
public class InstallPackage
{
    public void testInstall(final Path repo_dir)
            throws Exception
    {
        testOne(repo_dir, FunctionalTest.HELLO_XAR_OLD, PACKAGES_TXT_CONTENT_OLD, PACKAGES_XML_CONTENT_OLD, "hello-1.1/hello");
        testOne(repo_dir, FunctionalTest.HELLO_XAR_NEW, PACKAGES_TXT_CONTENT_NEW, PACKAGES_XML_CONTENT_NEW, "hello-1.2/content");
    }

    private void testOne(final Path repo_dir, String xar, String txt_content, String xml_content, String content_dir)
            throws Exception
    {
        // the SUT
        Storage    storage  = new FileSystemStorage(repo_dir);
        Repository repo     = new Repository(storage);
        Path       pkg      = Paths.get(".").resolve(xar).toAbsolutePath().normalize();
        // do it
        repo.installPackage(new XarFileSource(pkg), true, new FakeInteract());
        final Path expathPkgDir = repo_dir.resolve(".expath-pkg");

        // .expath-pkg/packages.txt
        final Path packages_txt = expathPkgDir.resolve("packages.txt");
        assertTrue("the file .expath-pkg/packages.txt exist", Files.exists(packages_txt));
        assertEquals("the file .expath-pkg/packages.txt content", txt_content, readFile(packages_txt));

        // .expath-pkg/packages.xml
        final Path packages_xml = expathPkgDir.resolve("packages.xml");
        assertTrue("the file .expath-pkg/packages.xml exist", Files.exists(packages_xml));

        final Source expectedPackagesXml = Input.fromString(xml_content).build();
        final Source actualPackagesXml = Input.fromFile(packages_xml.toFile()).build();

        final Diff diff = DiffBuilder.compare(expectedPackagesXml)
            .withTest(actualPackagesXml)
            .normalizeWhitespace()
            .checkForSimilar()
            .build();

        assertFalse("the file .expath-pkg/packages.xml content is different: " + diff.toString(), diff.hasDifferences());

        // content dir
        final Path c_dir = repo_dir.resolve(content_dir);
        assertTrue("the content dir exist", Files.exists(c_dir));
        // TODO: Write more assertions...
    }

    private String readFile(Path f)
            throws IOException
    {
        return new String(Files.readAllBytes(f));
    }

    private static final String PACKAGES_TXT_CONTENT_OLD =
            "hello-1.1 http://www.example.org/lib/hello 1.1\n";
    private static final String PACKAGES_XML_CONTENT_OLD =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><packages xmlns=\"http://expath.org/ns/repo/packages\">\n"
            + "<package name=\"http://www.example.org/lib/hello\" dir=\"hello-1.1\" version=\"1.1\"/>\n"
            + "</packages>\n";
    private static final String PACKAGES_TXT_CONTENT_NEW =
            "hello-1.1 http://www.example.org/lib/hello 1.1\n"
            + "hello-1.2 http://www.example.org/lib/hello 1.2\n";
    private static final String PACKAGES_XML_CONTENT_NEW =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><packages xmlns=\"http://expath.org/ns/repo/packages\">\n"
            + "<package name=\"http://www.example.org/lib/hello\" dir=\"hello-1.1\" version=\"1.1\"/>\n"
            + "<package name=\"http://www.example.org/lib/hello\" dir=\"hello-1.2\" version=\"1.2\"/>\n"
            + "</packages>\n";

    private static class FakeInteract
            implements UserInteractionStrategy
    {
        public void messageInfo(String msg) throws PackageException {
            System.out.println("INFO: " + msg);
        }
        public void messageError(String msg) throws PackageException {
            System.out.println("ERROR: " + msg);
        }
        public void logInfo(String msg) throws PackageException {
            System.out.println("LOG: " + msg);
        }
        public boolean ask(String prompt, boolean dflt) throws PackageException {
            System.out.println("ASK: " + prompt + " / " + dflt);
            return dflt;
        }
        public String ask(String prompt, String dflt) throws PackageException {
            System.out.println("ASK: " + prompt + " / " + dflt);
            return dflt;
        }
    }
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
