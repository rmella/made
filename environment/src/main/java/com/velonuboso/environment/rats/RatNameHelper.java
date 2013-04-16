/*
 * Copyright 2013 Rubén Héctor García <raiben@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.velonuboso.environment.rats;
import com.velonuboso.made.core.Gender;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;


/**
 *
 * @author Ruben Hector Garcia Ortega <raiben@gmail.com>
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
                    FileUtils.readFileToString(new File(url.getFile()));
        } catch (IOException ex) {
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

}
