/*
 * Copyright 2013 Ruben.
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
package com.velonuboso.made.core.interfaces;

import com.velonuboso.made.core.common.Position;
import java.util.HashSet;

/**
 *
 * @author Ruben
 */
public interface MadeAgentInterface {

    public void addline(int days, String action);

    public int getDays();

    public HashSet<String> getLabels();

    public Position getPosition();

    public int getProfile();

    // -------------------------------------------------------------------------
    public String getSheet();

    boolean isAlive();

    public void justLive();

    public String getStringLog();

    public void addLabel(String label);

    public int getId();

}
