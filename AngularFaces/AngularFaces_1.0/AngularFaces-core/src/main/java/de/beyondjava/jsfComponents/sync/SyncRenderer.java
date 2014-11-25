/**
 *  (C) 2013-2014 Stephan Rauh http://www.beyondjava.net
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.beyondjava.jsfComponents.sync;

import java.io.IOException;
import java.lang.reflect.*;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.*;
import javax.faces.render.FacesRenderer;

import com.google.gson.Gson;

import de.beyondjava.jsfComponents.common.ELTools;

/**
 * Add AngularJS behaviour to a standard Primefaces InputText.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@FacesRenderer(componentFamily = "org.primefaces.component", rendererType = "de.beyondjava.Sync")
public class SyncRenderer extends org.primefaces.component.inputtext.InputTextRenderer {

    @Override
    public void decode(FacesContext context, UIComponent component) {
        String direction = (String) component.getAttributes().get("direction");
        if ((null == direction) || "both".equals(direction) || "clientToServer".equals(direction)) {

            String rootProperty = ELTools.getCoreValueExpression(component);

            Map<String, String> parameterMap = context.getExternalContext().getRequestParameterMap();
            String json = parameterMap.get(component.getClientId());
            Object bean = ELTools.evalAsObject("#{" + rootProperty + "}");
            if (rootProperty.contains(".")) {
                try {
                    Object fromJson = new Gson().fromJson(json, bean.getClass());
                    String rootBean = rootProperty.substring(0, rootProperty.lastIndexOf("."));
                    Object root = ELTools.evalAsObject("#{" + rootBean + "}");
                    String nestedBeanName = rootProperty.substring(rootProperty.lastIndexOf('.') + 1);
                    String setterName = "set" + nestedBeanName.substring(0, 1).toUpperCase()
                            + nestedBeanName.substring(1);

                    try {
                        if (root != null) {
                            Method setter = root.getClass().getDeclaredMethod(setterName, bean.getClass());
                            setter.invoke(root, fromJson);
                        }

                    }
                    catch (NoSuchMethodException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    catch (SecurityException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    catch (IllegalAccessException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    catch (IllegalArgumentException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    catch (InvocationTargetException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                catch (NumberFormatException error) {
                    FacesContext.getCurrentInstance().addMessage(
                            null,
                            new FacesMessage(FacesMessage.SEVERITY_FATAL,
                                    "A number was expected, but something else was sent (" + rootProperty + ")",
                                    "A number was expected, but something else was sent (" + rootProperty + ")"));

                }
                catch (IllegalArgumentException error) {
                    FacesContext.getCurrentInstance().addMessage(
                            null,
                            new FacesMessage(FacesMessage.SEVERITY_FATAL, "Can't parse the data sent from the client ("
                                    + rootProperty + ")", "Can't parse the data sent from the client (" + rootProperty
                                    + ")"));

                }
                catch (Exception anyError) {
                    FacesContext.getCurrentInstance().addMessage(
                            null,
                            new FacesMessage(FacesMessage.SEVERITY_FATAL,
                                    "A technical error occured when trying to read the data sent from the client ("
                                            + rootProperty + ")",
                                    "A technical error occured when trying to read the data sent from the client ("
                                            + rootProperty + ")"));
                }
            }

        }
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.append("\r\n\r\n\r\n");
        String direction = (String) component.getAttributes().get("direction");
        String rootProperty = ELTools.getCoreValueExpression(component);
        String jsVarName = rootProperty;
        int lastPeriod = rootProperty.lastIndexOf('.');
        if (lastPeriod >= 0) {
            jsVarName = rootProperty.substring(lastPeriod + 1);
        }
        if ((null == direction) || "serverToClient".equals(direction) || "both".equals(direction)) {
            Object serverObject = ELTools.evalAsObject("#{" + rootProperty + "}");
            String serverAsJSon = new Gson().toJson(serverObject);
            // System.out.println(serverAsJSon);

            writer.append("<script type=\"text/javascript\">");

            String functionName = "sync" + jsVarName.replace(".", "_");
            writer.append("var " + functionName + " = function()\r\n {\r\n");
            String line = "   injectJSonIntoScope('" + jsVarName + "', '" + serverAsJSon + "');\r\n";
            writer.append(line);
            writer.append("};\r\n");
            writer.append("addSyncPushFunction(" + functionName + ");\r\n");
            writer.append("</script>\r\n");
        }
        writer.append("\r\n\r\n\r\n");
        if ((null == direction) || "both".equals(direction) || "clientToServer".equals(direction)) {
            ValueExpression ve = ELTools.createValueExpression("{{getJSonFromScope(\"" + jsVarName + "\")}}");
            component.setValueExpression("value", ve);
            super.encodeBegin(context, component);
        }
        writer.append("\r\n\r\n\r\n");
    }
}
