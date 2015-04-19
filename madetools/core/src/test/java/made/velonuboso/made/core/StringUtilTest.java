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
package made.velonuboso.made.core;

import com.velonuboso.made.core.StringUtil;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rhgarcia
 */
public class StringUtilTest {
    
    public StringUtilTest() {
    }
    
    @Test
    public void doubleTicksToQuote_must_convert_ecah_double_tic_to_quote(){
        String originalString = "''safe'' and ''sound''";
        String expectedString = "\"safe\" and \"sound\"";
        String resultString = StringUtil.doubleTicksToQuote(originalString);
        assertEquals("StringUtil.doubleTicksToQuote should've converted double ticks to quotes", expectedString, resultString);
    }
    
}
