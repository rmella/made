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
package com.velonuboso.made.core;

import com.velonuboso.made.core.interfaces.Environment;
import com.velonuboso.made.core.MadeAgent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Ruben Hector Garcia Ortega <raiben@gmail.com>
 */
public class MadeState {
    
    private Environment env;
    private String name;
    private boolean isInitial;
    private boolean isFinal;
    private Date dateFrom;
    private String event;
    private MadeAgent target;
    
    
    public MadeState(Environment env, String name, boolean isInitial, boolean isFinal, Date dateFrom, String event, MadeAgent target) {
        this.name = name;
        this.isInitial = isInitial;
        this.isFinal = isFinal;
        this.dateFrom = dateFrom;
        this.event = event;
        this.target = target;
    }

    public MadeState getNextState(MadeAgent source, MadeAgent related){
        return env.getNextState(this, source, related);
    }
    
    @Override
    public boolean equals(Object obj) {
        boolean ret = obj instanceof MadeState? 
                (((MadeState) obj).getName().compareTo(this.getName())==0): 
                false;
        return ret;
    }
    
    public String getName(){
        return name;
    }

    public boolean isInitial(){
        return isInitial;
    }

    public boolean isFinal(){
        return isFinal;
    }

    public Date getDateFrom(){
        return dateFrom;
    }

    public String getEvent(){
        return event;
    }

    public MadeAgent getTarget(){
        return target;
    }
    
}
