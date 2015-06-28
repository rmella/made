/*
 * Copyright (C) 2015 rhgarcia
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

package com.velonuboso.made.core.common.util;

import java.util.List;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class StringUtil {

    private static final String QUOTE = "\"";
    private static final String DOUBLE_TICKS = "''";
    
    public static String doubleTicksToQuote(final String originalString) {
        final String newString = originalString == null? "": originalString;
        return newString.replace(DOUBLE_TICKS, QUOTE);
    }

    public static String replaceArguments(final String naturalLanguageTemplate, final List<String> arguments) {
        return String.format(naturalLanguageTemplate, arguments.toArray());
   }

    public static String cleanArgument(final String argument) {
        String newArgument = argument.trim();
        if (newArgument.startsWith(QUOTE) && newArgument.endsWith(QUOTE)){
            newArgument = removeFirstAndLastCharacters(newArgument);
        }
        
        return newArgument;
    }

    private static String removeFirstAndLastCharacters(String newArgument) {
        return newArgument.substring(1, newArgument.length()-1);
    }
}
