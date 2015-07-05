/*
 * Copyright (C) 2015 rhgarcia
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
package com.velonuboso.made.core.customization.implementation;

import com.google.gson.Gson;
import com.velonuboso.made.core.common.util.InitializationException;
import com.velonuboso.made.core.customization.api.ICustomization;
import com.velonuboso.made.core.customization.entity.NarrationRuleEntity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rubén Héctor García (raiben@gmail.com)
 */
public class Customization implements ICustomization {

    private List<NarrationRuleEntity> narrationRules;
    
    public Customization() {
        narrationRules = new ArrayList<NarrationRuleEntity>();
    }

    public void loadFromFile(final File file) throws InitializationException {
        try {
            final Gson gson = new Gson();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            NarrationRuleEntity[] narrationRules = gson.fromJson(reader, NarrationRuleEntity[].class);
            this.narrationRules = Arrays.asList(narrationRules);
            reader.close();
        } catch (Exception ex) {
            throw new InitializationException(ex);
        }
    }

    @Override
    public List<NarrationRuleEntity> getNarrationRules() {
        return narrationRules;
    }

}
