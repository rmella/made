/*
 * Copyright 2014 Rubén Héctor García <raiben@gmail.com>.
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

package com.velonuboso.made2.core.rat;

import com.velonuboso.made2.core.common.Gender;
import com.velonuboso.made2.core.common.Gender;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;


/**
 *
 * @author raiben@gmail.com
 */
public class RatNameHelper {

    private static RatNameHelper instance;
    private ArrayList<String> maleNames;
    private ArrayList<String> femaleNames;
    private ArrayList<String> surnames;
    private ArrayList<String> nicknames;
    
    private RatNameHelper() {
        maleNames = namesFromList("rat_male_names.dat");
        femaleNames = namesFromList("rat_female_names.dat");
        surnames = namesFromList("rat_surnames.dat");
        nicknames = namesFromList("rat_nicknames.dat");
    }

    public static RatNameHelper getInstance() {
        if (instance == null){
            instance = new RatNameHelper();
        }
        return instance;
    }

    private ArrayList<String> namesFromList(String resourceName) {
        
        ArrayList<String> ret = new ArrayList<String>();
        
        URL url = this.getClass().getClassLoader().getResource(resourceName);
        
        String str = "";
        try {
            str =
                    getText(url);
        } catch (Exception ex) {
            Logger.getLogger(RatNameHelper.class.getName()).log(Level.SEVERE, null,
                    ex);
        }
        
        String[] names = str.split("\n");
        ret.addAll(Arrays.asList(names));
        
        return ret;
    }
    
    public String getRandomName (Random r, Gender gender){
        String ret = "";
        if (gender == Gender.MALE){
            int index = r.nextInt(maleNames.size());
            ret = maleNames.get(index);
        }else{
            int index = r.nextInt(femaleNames.size());
            ret = femaleNames.get(index);
        }
        return ret;
    }
    public String getRandomSurname (Random r){
        String ret = "";
        int index = r.nextInt(surnames.size());
        ret = surnames.get(index);
        return ret;
    }
    public String getRandomNickname (Random r){
        String ret = "";
        int index = r.nextInt(nicknames.size());
        ret = nicknames.get(index);
        return ret;
    }

    public static String getText(URL url) throws Exception {
        URLConnection connection = url.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                    connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null)
            response.append(inputLine+"\n");

        in.close();

        return response.toString();
    }

}
