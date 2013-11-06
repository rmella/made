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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author Ruben Hector Garcia Ortega <raiben@gmail.com>
 */
public class BasicRunner extends Thread {

    private Environment env;
    private Random r;
    private ArrayList<MadeAgent> agents;
    private Date fictional;
    private Date real;
    private int interval;

    public BasicRunner(Environment env, boolean randomize, Date fictional, Date real, int interval) {
        this.env = env;
        r = randomize ? new Random() : new Random(0);
        agents = new ArrayList<MadeAgent>();
        
        this.fictional = fictional;
        this.real = real;
        this.interval = interval;
    }

    @Override
    public void run() {
        super.run();

        int na = env.getNumberOfInitialAgents();

        for (int i = 0; i < na; i++) {
            MadeAgent a = new MadeAgent(env, r);
            agents.add(a);
        }
        
        env.initFictionalDate(fictional, real, interval);
        for (MadeAgent a:agents){
            a.start();
        }
    }
}
