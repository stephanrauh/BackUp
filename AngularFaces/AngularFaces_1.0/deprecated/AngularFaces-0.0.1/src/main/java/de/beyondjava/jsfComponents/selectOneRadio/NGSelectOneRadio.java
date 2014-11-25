/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents.selectOneRadio;

import java.util.List;

import javax.faces.component.*;
import javax.faces.context.FacesContext;
import javax.faces.event.*;

import org.primefaces.component.column.Column;
import org.primefaces.component.outputlabel.OutputLabel;

import de.beyondjava.jsfComponents.common.*;
import de.beyondjava.jsfComponents.message.NGMessage;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@FacesComponent("de.beyondjava.SelectOneRadio")
public class NGSelectOneRadio extends org.primefaces.component.selectonemenu.SelectOneMenu implements
      SystemEventListener, NGUIComponent {
   public static final String COMPONENT_FAMILY = "org.primefaces.component";

   /**
    * @return the componentFamily
    */
   public static String getComponentFamily() {
      return COMPONENT_FAMILY;
   }

   /**
    * if the ID has not been set by the application, we define our own default
    * id (which equals the ngModel attribute)
    */
   private boolean isDefaultId = true;

   /**
    * Prevents endless loop during calls from NGUIComponentTools. Such a
    * variable should never be needed, no doubt about it. Guess I didn\"t find
    * the best algorithm yet. :)
    */
   private boolean preventRecursion = false;

   public NGSelectOneRadio() {
      FacesContext context = FacesContext.getCurrentInstance();
      UIViewRoot root = context.getViewRoot();
      root.subscribeToViewEvent(PreRenderViewEvent.class, this);
   }

   @Override
   public String getClientId(FacesContext context) {
      if (preventRecursion) {
         return super.getClientId(context);
      }
      return NGUIComponentTools.getClientId(context, this);
   }

   @Override
   public String getFamily() {
      return COMPONENT_FAMILY;
   }

   @Override
   public String getId() {
      if (isDefaultId) {
         setId(ELTools.getNGModel(this));
      }
      return super.getId();
   }

   /**
    * Try to guess the label from the ng-model attribute if the label attribute
    * has been omitted.
    */
   @Override
   public String getLabel() {
      String label = super.getLabel();
      if (null == label) {
         String ngModel;
         ngModel = ELTools.getNGModel(this);
         return NGWordUtiltites.labelFromCamelCase(ngModel);
      }
      return label;
   }

   /**
    * Add a label in front of the current component.
    */
   private void insertLabelBeforeThisInputField() {
      OutputLabel l = new OutputLabel();
      l.setFor(getId());
      l.setValue(getLabel());
      List<UIComponent> tree = getParent().getChildren();
      for (int i = 0; i < tree.size(); i++) {
         if (tree.get(i) == this) {
            tree.add(i, l);
            break;
         }
      }
   }

   /**
    * Add an error or hint message component behind the current component.
    */
   private void insertMessageBehindThisInputField() {
      NGMessage l = new NGMessage();
      l.setFor(getId());
      l.setDisplay("text");
      l.setTarget(this);
      List<UIComponent> tree = getParent().getChildren();
      for (int i = 0; i < tree.size(); i++) {
         if (tree.get(i) == this) {
            tree.add(i + 1, l);
            break;
         }
      }
   }

   @Override
   public boolean isListenerForSource(Object source) {
      return (source instanceof UIViewRoot);
   }

   /**
    * If the bean attribute is annotated by NotNull, mark this component as
    * mandatory.
    */
   @Override
   public boolean isRequired() {
      NGBeanAttributeInfo info = ELTools.getBeanAttributeInfos(this);
      if (info.isRequired()) {
         return true;
      }
      return super.isRequired();
   }

   /**
    * Prevents endless loop during calls from NGUIComponentTools. Such a
    * variable should never be needed, no doubt about it. Guess I didn\"t find
    * the best algorithm yet. :)
    */
   @Override
   public boolean preventRecursion() {
      return preventRecursion = true;
   }

   /**
    * Prevents endless loop during calls from NGUIComponentTools. Such a
    * variable should never be needed, no doubt about it. Guess I didn\"t find
    * the best algorithm yet. :)
    */
   @Override
   public boolean preventRecursion(boolean reset) {
      return preventRecursion = false;
   }

   /**
    * Catching the PreRenderViewEvent allows AngularFaces to modify the JSF tree
    * by adding a label and a message.
    */
   @Override
   public void processEvent(SystemEvent event) throws AbortProcessingException {
      if (!FacesContext.getCurrentInstance().isPostback()) {
         boolean tableMode = false;
         UIComponent parent = getParent();
         if ((parent instanceof Column)) {
            tableMode = true;
         }
         if (null != parent) {
            UIComponent grandpa = parent.getParent();
            if ((grandpa instanceof Column)) {
               tableMode = true;
            }

         }
         if (!tableMode) {
            insertLabelBeforeThisInputField();
            insertMessageBehindThisInputField();
         }
      }
   }

}
