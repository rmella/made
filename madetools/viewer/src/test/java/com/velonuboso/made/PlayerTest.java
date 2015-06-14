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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.velonuboso.made.entity.EventsLog;
import com.velonuboso.made.viewer.Player;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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

    @Test
    public void UT_EventsLog_can_be_converted_to_json() throws IOException {
        EventsLog log = new EventsLog();
        
        log.setBoard(new EventsLog.BoardEntity());
        log.getBoard().setGridSize(10);
        log.getBoard().setObstacles(new EventsLog.PositionEntity[]{
            new EventsLog.PositionEntity(1, 4),
            new EventsLog.PositionEntity(2, 5)});
        
        log.setCharacters(new EventsLog.CharacterEntity[]{
            new EventsLog.CharacterEntity(0, "Zhen Suwah", Color.AQUA),
            new EventsLog.CharacterEntity(1, "Quddah Oofi", Color.BROWN)});
        
        log.setDayLogs(new EventsLog.DayLog[]{
            new EventsLog.DayLog(0, new EventsLog.EventEntity[]{
                new EventsLog.EventEntity(0, new Integer[]{0}, "IsBorn (0, 0, 3)"),
                new EventsLog.EventEntity(1, new Integer[]{1}, "IsBorn (0, 1, 50)")
            }),
            new EventsLog.DayLog(1, new EventsLog.EventEntity[]{
                new EventsLog.EventEntity(2, new Integer[]{0}, "Move (1, 0, 19)"),
                new EventsLog.EventEntity(3, new Integer[]{1}, "Move (1, 1, 65)")
            })
        });
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try{
            String str = gson.toJson(log);
            System.out.println(str);
        }catch(Exception e){
            Assert.fail("Should've converted the object to a json correctly");
        }
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
