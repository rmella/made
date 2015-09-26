/*
 * Copyright (C) 2015 Rubén Héctor García (raiben@gmail.com)
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
package com.velonuboso.made.core.customization;

import com.google.gson.Gson;
import com.velonuboso.made.core.common.util.InitializationException;
import com.velonuboso.made.core.customization.entity.NarrationRuleEntity;
import com.velonuboso.made.core.customization.implementation.Customization;
import java.io.File;
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
public class CustomizationTest {
   
    private Customization customization;
    
    public CustomizationTest() {
    }
    
    @Before
    public void before(){
        customization = new Customization();
    }
    
    @Test(expected = InitializationException.class)
    public void UT_Customization_loadFromFile_must_throw_exception_when_file_is_not_found()
            throws InitializationException{
        String fileName = "asdfg";
        customization.loadFromFile(new File(fileName));
        fail("Should've thrown InitializationException since the file "+ fileName+" does not exist");
    }
    
    @Test(expected = InitializationException.class)
    public void UT_Customization_loadFromFile_must_throw_exception_when_file_format_is_invalid()
            throws InitializationException{
        String fileName = ClassLoader.getSystemClassLoader().getResource("invalid_narration_rules.txt").getFile();
        customization.loadFromFile(new File(fileName));
        fail("Should've thrown InitializationException since the file "+ fileName+" has invalid format");
    }
    
    @Test
    public void UT_Customization_loadFromFile_can_load_a_valid_file_with_narration_rules()
            throws InitializationException{
        NarrationRuleEntity[] expectedNarrationRules = new NarrationRuleEntity[2];
        expectedNarrationRules[0] = new NarrationRuleEntity("MyPredicate", 2, "Something happened.");
        expectedNarrationRules[1] = new NarrationRuleEntity("MyPredicate2", 1, "Something happened too.");
        
        String fileName = ClassLoader.getSystemClassLoader().getResource("valid_narration_rules.json").getFile();
        customization.loadFromFile(new File(fileName));
        
        assertEquals("Shouldn't retrieved two narrationRules",
                customization.getNarrationRules().size(), expectedNarrationRules.length);
        assertEquals("First narration rule should've been retrieved correctly",
                customization.getNarrationRules().get(0), expectedNarrationRules[0]);
        assertEquals("Second narration rule should've been retrieved correctly",
                customization.getNarrationRules().get(1), expectedNarrationRules[1]);
    }
}
