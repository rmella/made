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
    private State state;
    private Date dateFrom;
    private String event;
    private MadeAgent target;

    public MadeState(Environment env, State state, Date dateFrom, String event, MadeAgent target) {
        this.env = env;
        this.state = state;
        this.dateFrom = dateFrom;
        this.event = event;
        this.target = target;
    }

    /**
     * @return the env
     */
    public Environment getEnv() {
        return env;
    }

    /**
     * @param env the env to set
     */
    public void setEnv(Environment env) {
        this.env = env;
    }

    /**
     * @return the state
     */
    public State getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * @return the dateFrom
     */
    public Date getDateFrom() {
        return dateFrom;
    }

    /**
     * @param dateFrom the dateFrom to set
     */
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * @return the event
     */
    public String getEvent() {
        return event;
    }

    /**
     * @param event the event to set
     */
    public void setEvent(String event) {
        this.event = event;
    }

    /**
     * @return the target
     */
    public MadeAgent getTarget() {
        return target;
    }

    /**
     * @param target the target to set
     */
    public void setTarget(MadeAgent target) {
        this.target = target;
    }
    
    
    
}
