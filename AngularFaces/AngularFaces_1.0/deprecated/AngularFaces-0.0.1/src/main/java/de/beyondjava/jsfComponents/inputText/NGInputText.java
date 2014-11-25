package de.beyondjava.jsfComponents.inputText;

import java.io.IOException;
import java.util.List;

import javax.faces.component.*;
import javax.faces.context.FacesContext;
import javax.faces.event.*;

import org.primefaces.component.column.Column;
import org.primefaces.component.outputlabel.OutputLabel;

import de.beyondjava.jsfComponents.common.*;
import de.beyondjava.jsfComponents.message.NGMessage;

/**
 * Add AngularJS behaviour to a standard Primefaces InputText.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@FacesComponent("de.beyondjava.InputText")
public class NGInputText extends org.primefaces.component.inputtext.InputText implements SystemEventListener,
      NGUIComponent {
   public static final String COMPONENT_FAMILY = "javax.faces.Input";

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

   /**
    * This constructor subscribes to the PreRenderViewEvent. Catching the
    * PreRenderViewEvent allows AngularFaces to modify the JSF tree by adding a
    * label and a message.
    */
   public NGInputText() {
      FacesContext context = FacesContext.getCurrentInstance();
      UIViewRoot root = context.getViewRoot();
      root.subscribeToViewEvent(PreRenderViewEvent.class, this);
   }

   @Override
   public void encodeBegin(FacesContext context) throws IOException {
      if ("text".equals(getType())) {
         Class<?> type = ELTools.getType(this);
         if ((int.class == type) || (Integer.class == type) || (long.class == type) || (Long.class == type)
               || (double.class == type) || (Double.class == type) || (byte.class == type) || (Byte.class == type)
               || (short.class == type) || (Short.class == type) || (float.class == type) || (Float.class == type)) {
            setType("number");
         }
      }
      super.encodeBegin(context);
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
    * if no label is provided by the XHTML file, try to guess it from the
    * ng-model attribute.
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

   @Override
   public int getMaxlength() {
      int maxlength = super.getMaxlength();
      if (maxlength <= 0) {
         NGBeanAttributeInfo info = ELTools.getBeanAttributeInfos(this);
         maxlength = (int) info.getMaxSize();
      }
      return maxlength;
   }

   @Override
   public int getSize() {
      int size = super.getSize();
      if (size <= 0) {
         NGBeanAttributeInfo info = ELTools.getBeanAttributeInfos(this);
         size = (int) info.getMaxSize();
      }
      return super.getSize();
   }

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
         if (!(getParent() instanceof Column)) {
            insertLabelBeforeThisInputField();
            insertMessageBehindThisInputField();
         }
      }
   }

   @Override
   public void setId(String id) {
      if (!id.startsWith("j_idt")) {
         isDefaultId = false;
      }
      super.setId(id);
   }

   @Override
   public void setValue(Object value) {
      super.setValue(value);
   }
}
