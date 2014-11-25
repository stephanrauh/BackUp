/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.*;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.w3c.dom.Node;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class DiffenceEngineTest {

   /**
    * Test method for
    * {@link de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine.DiffenceEngine#determineNecessaryChanges(java.lang.String, org.w3c.dom.Node)}
    * .
    * 
    * @throws IOException
    */
   @Test
   public void testDetermineNecessaryChanges1() throws IOException {
      final DiffenceEngine diffenceEngine = new DiffenceEngine();
      File dir = new File("E:/this/AngularFaces/src/test/resources/DifferenceEngine");

      final File partialChange = new File(dir, "partialChange1.xml");
      if (partialChange.exists()) {
         String newHTML = FileUtils.readFileToString(partialChange);
         String lastKnownHTML = FileUtils.readFileToString(new File(dir, "html1.xml"));
         Node lastKnownCorrespondingNode = DOMUtils.stringToDOM(lastKnownHTML).getFirstChild();
         ArrayList<String> deletions = new ArrayList<String>();
         ArrayList<String> changes = new ArrayList<String>();
         ArrayList<Node> necessaryChanges = diffenceEngine.determineNecessaryChanges(newHTML,
               lastKnownCorrespondingNode, changes, deletions);
         assertNotNull(necessaryChanges);
         assertEquals(1, necessaryChanges.size());
         assertEquals(0, deletions.size());
         String diff1 = diffenceEngine.domToString(necessaryChanges.get(0));
         assertEquals("<input id=\"formID:cityID\" name=\"formID:cityID\" type=\"text\" value=\"Jugenheim\"/>", diff1);
      }
   }

   @Test
   public void testDetermineNecessaryChanges8() throws IOException {
      final DiffenceEngine diffenceEngine = new DiffenceEngine();
      File dir = new File("E:/this/AngularFaces/src/test/resources/DifferenceEngine");

      final File partialChange = new File(dir, "partialChange8.xml");
      if (partialChange.exists()) {
         String newHTML = FileUtils.readFileToString(partialChange);
         String lastKnownHTML = FileUtils.readFileToString(new File(dir, "html8.xml"));
         Node lastKnownCorrespondingNode = DOMUtils.stringToDOM(lastKnownHTML).getFirstChild();
         ArrayList<String> deletions = new ArrayList<String>();
         ArrayList<String> changes = new ArrayList<String>();
         ArrayList<Node> necessaryChanges = diffenceEngine.determineNecessaryChanges(newHTML,
               lastKnownCorrespondingNode, deletions, changes);
         assertNotNull(necessaryChanges);
         assertEquals(1, necessaryChanges.size());
         assertEquals(0, deletions.size());
         assertEquals(0, changes.size());
         String diff1 = diffenceEngine.domToString(necessaryChanges.get(0));
         assertEquals("<input id=\"formID:cityID\" name=\"formID:cityID\" type=\"text\" value=\"Oppenheim\"/>", diff1);
      }
   }

}
