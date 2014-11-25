package de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine;

import java.util.*;
import java.util.logging.Logger;

import org.w3c.dom.*;

public class XHtmlDiff {
   private static final Logger LOGGER = Logger
         .getLogger("de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine.XmlDiff");

   private static void fix(ArrayList<Node> changed) {
      ArrayList<Node> temp = new ArrayList<Node>();

      // loop nodes to removed nodes without id and resolve to their parent with
      // id
      Iterator<Node> nodeIterator = changed.iterator();
      while (nodeIterator.hasNext()) {
         Element element = (Element) nodeIterator.next();
         String id = element.getAttribute("id");
         if (id.isEmpty()) {
            LOGGER.info("Fixing a node without ID");
            Element parent = (Element) element.getParentNode();

            while (parent != null) {
               String parentId = parent.getAttribute("id");
               if (!parentId.isEmpty()) {
                  break;
               }
               parent = (Element) parent.getParentNode();
            }
            if (null != parent) {
               LOGGER.info("replaced node by node with id=" + parent.getAttribute("id"));
            }
            else {
               LOGGER.severe("cannot replace node");
            }
            nodeIterator.remove();
            temp.add(parent);
         }
      }

      changed.addAll(temp);
      temp.clear();

      // check if a node is child/parent from another need
      // we only need to the render the highest one
      for (Node outer : changed) {
         for (Node inner : changed) {
            if (isParentNode(outer, inner)) {
               temp.add(inner);
            }
         }
      }

      for (Node toRemove : temp) {
         changed.remove(toRemove);
      }
   }

   public static ArrayList<Node> getDifferenceOfDocuments(Document oldDocument, Document newDocument,
         ArrayList<String> deletions, ArrayList<String> changes) {
      ArrayList<Node> diff = XmlDiff.getDifferenceOfDocuments(oldDocument, newDocument, deletions, changes);

      // 1. schauen ob die Nodes eine Id haben und wenn ja die Parents loopen
      // bis meine eine Node mit ID hat
      // 2. durch den ersten Schritt kann passieren das man jetzt Childs von den
      // neuen Parent bereits als DIff hat
      // also sortieren wir nochmal aus
      fix(diff);

      return diff;
   }

   public static ArrayList<Node> getDifferenceOfNodes(Node oldDocument, Node newDocument, ArrayList<String> deletions,
         ArrayList<String> changes) {
      ArrayList<Node> diff = XmlDiff.getDifferenceOfNodes(oldDocument, newDocument, deletions, changes);

      // 1. schauen ob die Nodes eine Id haben und wenn ja die Parents loopen
      // bis meine eine Node mit ID hat
      // 2. durch den ersten Schritt kann passieren das man jetzt Childs von den
      // neuen Parent bereits als DIff hat
      // also sortieren wir nochmal aus
      fix(diff);

      return diff;
   }

   private static boolean isParentNode(Node parent, Node child) {
      Node childParent = child.getParentNode();

      while (childParent != null) {
         if (childParent == parent) {
            return true;
         }
         childParent = childParent.getParentNode();
      }

      return false;
   }
}
