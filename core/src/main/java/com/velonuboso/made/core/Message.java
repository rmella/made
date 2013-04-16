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

import java.util.Date;
import java.util.List;

/**
 *
 * @author Ruben Hector Garcia Ortega <raiben@gmail.com>
 */
public class Message {
    
    private Integer identifier;
    private MadeAgent source;
    private MadeAgent target;
    private Date date;
    private String type;
    private String[] args;

    public Message(Integer identifier, MadeAgent source, MadeAgent target, Date date, String type, String ... args) {
        this.identifier = identifier;
        this.source = source;
        this.target = target;
        this.date = date;
        this.type = type;
        this.args = args;
    }

    /**
     * @return the identifier
     */
    public Integer getIdentifier() {
        return identifier;
    }

    /**
     * @param identifier the identifier to set
     */
    public void setIdentifier(Integer identifier) {
        this.identifier = identifier;
    }

    /**
     * @return the source
     */
    public MadeAgent getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(MadeAgent source) {
        this.source = source;
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

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the args
     */
    public String[] getArgs() {
        return args;
    }

    /**
     * @param args the args to set
     */
    public void setArgs(String[] args) {
        this.args = args;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    
    
    
}
