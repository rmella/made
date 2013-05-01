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
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Ruben Hector Garcia Ortega <raiben@gmail.com>
 */
public class Diary {

    private MadeAgent agent = null;
    private Environment env = null;
    private DateFormat format = null;
    private ArrayList<Line> lines = null;

    public Diary(MadeAgent ag) {
        agent = ag;
        env = agent.getEnvironment();
        format = env.getDateFormat();
    }

    public void AddLine(Date date, MadeState state, String event, MadeAgent target) {
        Line diaryLine = new Line(
                format.format(date),
                state.getState().toString(),
                event,
                target.getSimpleFullName() + " (" + target.getIdentifier() + ")");
        lines.add(diaryLine);
    }

    public class Line {

        private String date;
        private String State;
        private String event;
        private String target;

        public Line(String date, String State, String event, String target) {
            this.date = date;
            this.State = State;
            this.event = event;
            this.target = target;
        }

        public String getDate() {
            return date;
        }

        public String getEvent() {
            return event;
        }

        public String getState() {
            return State;
        }

        public String getTarget() {
            return target;
        }
    }
}
