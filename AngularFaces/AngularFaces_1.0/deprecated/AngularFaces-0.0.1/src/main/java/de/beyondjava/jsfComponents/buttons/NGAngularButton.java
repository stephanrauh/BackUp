/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents.buttons;

import javax.faces.component.FacesComponent;

import org.primefaces.component.commandbutton.CommandButton;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@FacesComponent("de.beyondjava.AngularButton")
public class NGAngularButton extends CommandButton {
   public static final String COMPONENT_FAMILY = "de.beyondjava.AngularButton";

   @Override
   public String getFamily() {
      return COMPONENT_FAMILY;
   }

   @Override
   public String getOnclick() {
      String ngFunction = (String) getAttributes().get("ng-function");
      if (!ngFunction.contains("(")) {
         ngFunction += "()";
      }
      String s = "var $scope = angular.element(\"body\").scope(); $scope.$apply(function() { $scope." + ngFunction
            + "; }); return false;";
      return s;
   }

   @Override
   public String getType() {
      return "button";
   }

}
