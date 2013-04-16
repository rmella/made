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
package com.velonuboso.made.core.helpers;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FreeMarker printer
 *
 * @author Ruben Hector Garcia Ortega <raiben@gmail.com>
 */
public class FreemarkerPrinter {

    /**
     * target directory where the output files will be stored
     */
    private File directory;
    /**
     * freemarker configuration
     */
    private Configuration cfg;
    /**
     * the template associated to this printer
     */
    private Template template;

    /**
     * Constructor
     *
     * @param templateName the name of the template in the source folder that
     * will be used everytime the print method is called
     * @param targetDirectory target directory where the output files will be
     * stored
     */
    public FreemarkerPrinter(String templateName, String targetDirectory) {
        directory = new File(targetDirectory);

        // Freemarker configuration object
        cfg = new Configuration();

        try {
            //Load template from source folder
            Template template = cfg.getTemplate(templateName);
        } catch (IOException ex) {
            Logger.getLogger(FreemarkerPrinter.class.getName()).log(Level.WARNING, null, ex);
        }
    }

    /**
     * Prints the given data using the template in a file with the given
     * filename and the previously configured directory
     *
     * @param data key-value type map as is used by freemarker
     * @param filename the output file name
     */
    public void print(Map<String, Object> data, String filename) {
        Writer file = null;
        try {
            File target = new File(directory.getAbsolutePath() + File.pathSeparator + filename);
            file = new FileWriter(target);
            try {
                template.process(data, file);
            } catch (Exception ex) {
                Logger.getLogger(FreemarkerPrinter.class.getName()).log(Level.WARNING, null, ex);
            }
            file.flush();
            file.close();
        } catch (IOException ex) {
            Logger.getLogger(FreemarkerPrinter.class.getName()).log(Level.WARNING, null, ex);
        } finally {
            try {
                file.close();
            } catch (IOException ex) {
                Logger.getLogger(FreemarkerPrinter.class.getName()).log(Level.WARNING, null, ex);
            }
        }
    }
}
