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

package com.velonuboso.made;

import com.velonuboso.made.viewer.Player;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */

public class PlayerTest {
    
    public PlayerTest() {
    }

    @Test
    public void UT_Main_must_throw_exception_when_args_is_null() {
        String[] args = null;
        boolean exceptionCalled = CheckExceptionIsCalled(args);
        
        assertTrue("Should've thrown exception when arguments is null",
                exceptionCalled);
    }
    
    @Test
    public void UT_Main_must_throw_exception_when_args_is_empty() {
        String[] args = new String[0];
        boolean exceptionCalled = CheckExceptionIsCalled(args);
        
        assertTrue("Should've thrown exception when no argument is provided",
                exceptionCalled);
    }
    
    @Test
    public void UT_Main_must_throw_exception_when_first_arg_is_a_invalid_file() {
        String[] args = new String[]{"unexistingFile.json"};
        boolean exceptionCalled = CheckExceptionIsCalled(args);
        
        assertTrue("Should've thrown exception when the first argument is an invalid file",
                exceptionCalled);
    }

    /*
    @Test
    public void UT_Sample() {
        try{
            String fileName = ClassLoader.getSystemClassLoader().getResource("sample.json").getFile();
            String[] args = new String[]{fileName};
            Player.main(args);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    */
    
    private boolean CheckExceptionIsCalled(String[] args) {
        try{
            Player.main(args);
            return false;
        }catch(Exception e){
            return true;
        }
    }
}
