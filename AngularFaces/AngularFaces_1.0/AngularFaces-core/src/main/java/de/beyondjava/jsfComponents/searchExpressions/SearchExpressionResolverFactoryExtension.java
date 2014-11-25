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
package de.beyondjava.jsfComponents.searchExpressions;

import org.primefaces.expression.SearchExpressionResolverFactory;

public class SearchExpressionResolverFactoryExtension {
    private static boolean initialized = false;

    public static void init() {
        if (!initialized) {
            initialized = true;
            SearchExpressionResolverFactory.registerResolver("@first", new FirstExpressionResolver());
            SearchExpressionResolverFactory.registerResolver("@last", new LastExpressionResolver());
            SearchExpressionResolverFactory.registerResolver("@body", new BodyExpressionResolver());
            SearchExpressionResolverFactory.registerResolver("@head", new HeadExpressionResolver());
            // SearchExpressionResolverFactory.registerResolver("@content", new ContentExpressionResolver());
            // SearchExpressionResolverFactory.registerResolver("@class", new ClassExpressionResolver());
        }
    }
}
